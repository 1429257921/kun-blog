package com.kun.blog.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kun.blog.anno.AnonymousAccess;
import com.kun.blog.entity.po.ChatMessagesType;
import com.kun.blog.enums.RedisKeyConstant;
import com.kun.blog.enums.RunCircleHomeCalculateEnum;
import com.kun.blog.service.IChatMessagesTypeService;
import com.kun.common.core.exception.BizException;
import com.kun.common.log.anno.APIMessage;
import com.kun.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.domain.geo.GeoLocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 测试
 *
 * @author gzc
 * @since 2022/12/16 18:05
 **/
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("test")
public class TestController {
    private final IChatMessagesTypeService iChatMessagesTypeService;
    private final RedisService redisService;
    private final RedisTemplate<String, String> redisTemplate;

    @AnonymousAccess
    @APIMessage(value = "视频检测回调", printReqParam = true, reqLogInsertDB = false)
    @PostMapping("callback")
    public void callbackTest(@RequestParam(value = "checksum", required = false) String checksum,
                             @RequestParam(value = "content", required = false) String content) {
        log.info("视频检测回调接口1请求参数->checksum->{}, content->{}", checksum, content);
    }

    @AnonymousAccess
    @APIMessage(value = "视频检测回调2", printReqParam = false, reqLogInsertDB = false)
    @RequestMapping("callbackAAA")
    public void callbackTest2(Object jsonStr) {
        log.info("视频检测回调接口2请求参数->{}", JSON.toJSONString(jsonStr));
    }

    @AnonymousAccess
    @APIMessage(value = "测试批量插入", printReqParam = false, reqLogInsertDB = false)
    @GetMapping("pageList")
    public ResponseEntity<Page<String>> pageList(@RequestParam("index") int index,
                                                 @RequestParam("size") int size,
                                                 @RequestParam("userId") int userId) {
        CompletableFuture.runAsync(this::cacheData);
        SetOperations setOperations = redisService.redisTemplate.opsForSet();
        Page<String> page = new Page<>(index, size);
        String visitedKey = RedisKeyConstant.SOCIAL_CURRENT_USER_VISITED + userId;
        // 第一页时删除访问数据
        if (index == 1) {
            redisService.deleteObject(visitedKey);
        }

        // TODO 缓存动态的定时任务只会每天凌晨往redis更新数据，
        //  所以我们需要在发布动态、发布约聊订单、发布兴趣活动的时候就要去主动往对应的redis set集合中插入一条数据
        Set<String> result = new HashSet<>(size);

        // 动态
        Set<String> diff1 = setOperations.difference(RedisKeyConstant.SOCIAL_SUPERIOR_DYNAMIC, visitedKey);
        Set<String> diff2 = setOperations.difference(RedisKeyConstant.SOCIAL_GENERAL_DYNAMICS, visitedKey);
        Set<String> diff3 = setOperations.difference(RedisKeyConstant.SOCIAL_OLD_DYNAMIC, visitedKey);
        // 兴趣活动
        Set<String> diff4 = setOperations.difference(RedisKeyConstant.SOCIAL_ACTIVITY, visitedKey);
        // 约聊
        Set<String> diff5 = setOperations.difference(RedisKeyConstant.SOCIAL_CHAT, visitedKey);

        // 封装当前页的数据总数
        for (int i = 0; i < size; i++) {
            double d1 = dong(diff1, diff2, diff3, result);
            double d2 = xing(diff4, result);
            double d3 = yue(diff5, result);
            List<Double> originalRateList = new ArrayList<>();
            originalRateList.add(d1);
            originalRateList.add(d2);
            originalRateList.add(d3);
            int randomIndex = RunCircleHomeCalculateEnum.getRandomIndex(originalRateList);
            RunCircleHomeCalculateEnum runCircleHomeCalculateEnum = RunCircleHomeCalculateEnum.get(randomIndex);

            if (runCircleHomeCalculateEnum == RunCircleHomeCalculateEnum.DYNAMIC) {
                String id = getRandomDong(diff1, diff2, diff3, result);
                if (StrUtil.isNotBlank(id)) {
                    result.add(id);
                }
            } else if (runCircleHomeCalculateEnum == RunCircleHomeCalculateEnum.ACTIVITY) {
                String id = getRandomXing(diff4, result);
                if (StrUtil.isNotBlank(id)) {
                    result.add(id);
                }
            } else if (runCircleHomeCalculateEnum == RunCircleHomeCalculateEnum.CHAT) {
                String id = getRandomYue(diff5, result);
                if (StrUtil.isNotBlank(id)) {
                    result.add(id);
                }
            } else {

            }
        }
        if (CollUtil.isNotEmpty(result)) {
            redisService.setCacheSet(visitedKey, result);
        }

        page.setRecords(ListUtil.toList(result));
        return ResponseEntity.ok(page);
    }


    private String getRandomDong(Set<String> diff1, Set<String> diff2, Set<String> diff3, Set<String> result) {
        String id = "";
        // 70%优质，20%普通，10%旧
        // 动态
        Set<String> sub1 = new HashSet<>(CollUtil.subtractToList(diff1, result));
        Set<String> sub2 = new HashSet<>(CollUtil.subtractToList(diff2, result));
        Set<String> sub3 = new HashSet<>(CollUtil.subtractToList(diff3, result));

        int randomInt = RandomUtil.randomInt(0, 100);
        if (CollUtil.isEmpty(sub1) && CollUtil.isEmpty(sub2) && CollUtil.isEmpty(sub3)) {
            return "";
        }
        if (CollUtil.isEmpty(sub1) && CollUtil.isNotEmpty(sub2) && CollUtil.isNotEmpty(sub3)) {
            randomInt = RandomUtil.randomInt(70, 100);
        }
        if (CollUtil.isEmpty(sub1) && CollUtil.isEmpty(sub2) && CollUtil.isNotEmpty(sub3)) {
            randomInt = RandomUtil.randomInt(90, 100);
        }
        if (CollUtil.isNotEmpty(sub1) && CollUtil.isEmpty(sub2) && CollUtil.isEmpty(sub3)) {
            randomInt = RandomUtil.randomInt(0, 70);
        }
        if (CollUtil.isEmpty(sub1) && CollUtil.isNotEmpty(sub2) && CollUtil.isEmpty(sub3)) {
            randomInt = RandomUtil.randomInt(70, 90);
        }
        if (CollUtil.isNotEmpty(sub1) && CollUtil.isNotEmpty(sub2) && CollUtil.isEmpty(sub3)) {
            randomInt = RandomUtil.randomInt(0, 90);
        }
        if (CollUtil.isNotEmpty(sub1) && CollUtil.isEmpty(sub2) && CollUtil.isNotEmpty(sub3)) {
            int randomInt333 = RandomUtil.randomInt(0, 80);
            if (randomInt333 > 0 && randomInt333 < 65) {
                randomInt = RandomUtil.randomInt(0, 70);
            } else {
                randomInt = RandomUtil.randomInt(90, 100);
            }
        }

        if (randomInt >= 0 && randomInt < 70) {
            // 优质
            String[] array = sub1.toArray(new String[sub1.size()]);
            int randomIntRes = RandomUtil.randomInt(0, array.length);
            id = array[randomIntRes];
            System.out.println("获取优质动态->" + id);
        } else if (randomInt >= 70 && randomInt < 90) {
            // 普通
            String[] array = sub2.toArray(new String[sub2.size()]);
            int randomIntRes = RandomUtil.randomInt(0, array.length);
            id = array[randomIntRes];
            System.out.println("获取普通动态->" + id);
        } else {
            // 旧
            String[] array = sub3.toArray(new String[sub3.size()]);
            int randomIntRes = RandomUtil.randomInt(0, array.length);
            id = array[randomIntRes];
            System.out.println("获取旧动态->" + id);
        }
        return id;
    }

    private double dong(Set<String> diff1, Set<String> diff2, Set<String> diff3, Set<String> result) {
        // 动态
        Set<String> sub1 = new HashSet<>(CollUtil.subtractToList(diff1, result));
        Set<String> sub2 = new HashSet<>(CollUtil.subtractToList(diff2, result));
        Set<String> sub3 = new HashSet<>(CollUtil.subtractToList(diff3, result));

        if (CollUtil.isEmpty(sub1) && CollUtil.isEmpty(sub2) && CollUtil.isEmpty(sub3)) {
            return 0;
        }
        return RunCircleHomeCalculateEnum.DYNAMIC.getProbability();
    }


    private double xing(Set<String> diff4, Set<String> result) {
        // 兴趣活动
        Set<String> sub4 = new HashSet<>(CollUtil.subtractToList(diff4, result));
        if (CollUtil.isEmpty(sub4)) {
            return 0;
        }
        return RunCircleHomeCalculateEnum.ACTIVITY.getProbability();
    }

    private double yue(Set<String> diff5, Set<String> result) {
        // 约聊
        Set<String> sub5 = new HashSet<>(CollUtil.subtractToList(diff5, result));
        if (CollUtil.isEmpty(sub5)) {
            return 0;
        }
        return RunCircleHomeCalculateEnum.CHAT.getProbability();
    }

    private String getRandomXing(Set<String> diff4, Set<String> result) {
        Set<String> sub4 = new HashSet<>(CollUtil.subtractToList(diff4, result));
        String[] array = sub4.toArray(new String[sub4.size()]);
        int randomIntRes = RandomUtil.randomInt(0, array.length);
        String s = array[randomIntRes];
        System.out.println("获取兴趣活动->" + s);
        return s;
    }

    private String getRandomYue(Set<String> diff5, Set<String> result) {
        Set<String> sub5 = new HashSet<>(CollUtil.subtractToList(diff5, result));
        String[] array = sub5.toArray(new String[sub5.size()]);
        int randomIntRes = RandomUtil.randomInt(0, array.length);
        String s = array[randomIntRes];
        System.out.println("获取约聊订单->" + s);
        return s;
    }

    private void cacheData() {
        // 动态
        // 优质
        Set<String> hashSet1 = new HashSet<>();
        hashSet1.add("0-1");
        hashSet1.add("0-2");
        hashSet1.add("0-3");
        hashSet1.add("0-4");
        hashSet1.add("0-5");
        hashSet1.add("0-6");
        redisService.setCacheSet(RedisKeyConstant.SOCIAL_SUPERIOR_DYNAMIC, hashSet1);
        redisService.expire(RedisKeyConstant.SOCIAL_SUPERIOR_DYNAMIC, 10L, TimeUnit.MINUTES);
        // 普通
        Set<String> hashSet2 = new HashSet<>();
        hashSet2.add("0-1000");
        hashSet2.add("0-1001");
        hashSet2.add("0-1002");
        hashSet2.add("0-1003");
        hashSet2.add("0-1004");
        hashSet2.add("0-1005");
        redisService.setCacheSet(RedisKeyConstant.SOCIAL_GENERAL_DYNAMICS, hashSet2);
        redisService.expire(RedisKeyConstant.SOCIAL_GENERAL_DYNAMICS, 10L, TimeUnit.MINUTES);
        // 旧动态
        Set<String> hashSet3 = new HashSet<>();
        hashSet3.add("0-8888");
        hashSet3.add("0-8889");
        hashSet3.add("0-8890");
        hashSet3.add("0-8891");
        hashSet3.add("0-8892");
        hashSet3.add("0-8893");
        hashSet3.add("0-8894");
        redisService.setCacheSet(RedisKeyConstant.SOCIAL_OLD_DYNAMIC, hashSet3);
        redisService.expire(RedisKeyConstant.SOCIAL_OLD_DYNAMIC, 10L, TimeUnit.MINUTES);

        // 兴趣活动
        Set<String> hashSet4 = new HashSet<>();
        hashSet4.add("1-2000");
        hashSet4.add("1-2001");
        hashSet4.add("1-2002");
        hashSet4.add("1-2003");
        hashSet4.add("1-2004");
        redisService.setCacheSet(RedisKeyConstant.SOCIAL_ACTIVITY, hashSet4);
        redisService.expire(RedisKeyConstant.SOCIAL_ACTIVITY, 10L, TimeUnit.MINUTES);

        // 约聊
        Set<String> hashSet5 = new HashSet<>();
        hashSet5.add("2-5000");
        hashSet5.add("2-5001");
        hashSet5.add("2-5002");
        hashSet5.add("2-5003");
        hashSet5.add("2-5004");
        hashSet5.add("2-5005");
        hashSet5.add("2-5006");
        hashSet5.add("2-5007");
        hashSet5.add("2-5008");
        hashSet5.add("2-5009");
        hashSet5.add("2-5010");
        hashSet5.add("2-5011");
        hashSet5.add("2-5012");
        hashSet5.add("2-5013");
        hashSet5.add("2-5014");
        hashSet5.add("2-5015");
        hashSet5.add("2-5016");
        redisService.setCacheSet(RedisKeyConstant.SOCIAL_CHAT, hashSet5);
        redisService.expire(RedisKeyConstant.SOCIAL_CHAT, 10L, TimeUnit.MINUTES);
    }

    @AnonymousAccess
    @APIMessage(value = "测试批量插入", printReqParam = false, reqLogInsertDB = false)
    @GetMapping("testSet/{index}")
    public ResponseEntity<Set<String>> testSet(@PathVariable Integer index) {
        cacheSet();
        // 第一页时删除访问数据
        if (index == 1) {
            redisService.deleteObject("VISITOR");
        }
        Set<String> result = new HashSet<>(11);
        // 第二页
        SetOperations setOperations = redisService.redisTemplate.opsForSet();
//        Set<String> visitorSet = redisService.getCacheSet("VISITOR");
        Set<String> difference1 = setOperations.difference("AAA", "VISITOR");
        Set<String> difference2 = setOperations.difference("BBB", "VISITOR");

        for (int i = 0; i < 2; i++) {
            Set<String> subtract1 = new HashSet<>(CollUtil.subtractToList(difference1, result));
            Set<String> subtract2 = new HashSet<>(CollUtil.subtractToList(difference2, result));

            // 为空则直接返回
            if (CollUtil.isEmpty(subtract1) && CollUtil.isEmpty(subtract2)) {
                break;
            }
            if (CollUtil.isEmpty(subtract1) && CollUtil.isNotEmpty(subtract2)) {
                String[] array = subtract2.toArray(new String[subtract2.size()]);
                int randomInt = RandomUtil.randomInt(0, array.length);
                result.add(array[randomInt]);
                continue;
            }
            if (CollUtil.isEmpty(subtract2) && CollUtil.isNotEmpty(subtract1)) {
                String[] array = subtract1.toArray(new String[subtract1.size()]);
                int randomInt = RandomUtil.randomInt(0, array.length);
                result.add(array[randomInt]);
                continue;
            }
            int randomInt111 = RandomUtil.randomInt(0, 2);
            if (randomInt111 == 0) {
                String[] array = subtract1.toArray(new String[subtract1.size()]);
                int randomInt = RandomUtil.randomInt(0, array.length);
                result.add(array[randomInt]);
            } else {
                String[] array = subtract2.toArray(new String[subtract2.size()]);
                int randomInt = RandomUtil.randomInt(0, array.length);
                result.add(array[randomInt]);
            }
        }
        if (CollUtil.isNotEmpty(result)) {
            redisService.setCacheSet("VISITOR", result);
            redisService.expire("VISITOR", 10L, TimeUnit.MINUTES);
        }
        return ResponseEntity.ok(result);
    }

    private void cacheSet() {
        HashSet<String> hashSet1 = new HashSet<>(100);
        hashSet1.add("0-1");
        hashSet1.add("0-2");
        hashSet1.add("0-3");
        hashSet1.add("0-4");
        redisService.setCacheSet("AAA", hashSet1);
        redisService.expire("AAA", 5L, TimeUnit.HOURS);

        HashSet<String> hashSet2 = new HashSet<>(100);
        hashSet2.add("1-2");
        hashSet2.add("1-6");
        hashSet2.add("1-8");
        hashSet2.add("1-102");
        redisService.setCacheSet("BBB", hashSet2);
        redisService.expire("BBB", 5L, TimeUnit.HOURS);

    }


    @Transactional(rollbackFor = Exception.class)
    @AnonymousAccess
    @APIMessage(value = "测试批量插入", printReqParam = false, reqLogInsertDB = false)
    @GetMapping("testBatchSave/{count}")
    public ResponseEntity<Object> testBatchSave(@PathVariable Integer count) {
        String key = "GEO:TEST";
        redisService.addGeo(key, 116.280002D, 39.550000D, "user1");
        redisService.addGeo(key, 116.281002D, 39.550000D, "user2");
        redisService.addGeo(key, 116.290002D, 39.550000D, "user3");
        System.out.println("user1的位置信息->" + redisService.getGeo(key, "user1"));
        System.out.println("user2的位置信息->" + redisService.getGeo(key, "user2"));
        System.out.println("user3的位置信息->" + redisService.getGeo(key, "user3"));

        Double distanceGeo1 = redisService.distanceGeo(key, "user1", "user2");
        System.out.println("user1到user2直线距离->" + distanceGeo1 + "米");
        Double distanceGeo2 = redisService.distanceGeo(key, "user1", "user3");
        System.out.println("user1到user3直线距离->" + distanceGeo2 + "米");

        List<GeoResult<GeoLocation<String>>> resultList = redisService.radiusGeo(key, "user1", 100);
        System.out.println(JSON.toJSONString(resultList, true));
        System.out.println("========================================");
        Circle circle = new Circle(redisService.getGeo(key, "user1"), 880D);
        RedisGeoCommands.GeoRadiusCommandArgs radiusArgs =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                        .includeDistance()
                        .sortAscending();
        radiusArgs.includeCoordinates();

        GeoResults<GeoLocation<String>> radius = redisService.redisTemplate.opsForGeo().radius(key, circle, radiusArgs);
        System.out.println(JSON.toJSONString(radius.getContent(), true));

//        for (GeoResult<GeoLocation<String>> result : resultList) {
//            System.out.println(result.getDistance().getValue());
//            GeoLocation<String> content = result.getContent();
//            System.out.println(content.getName());
//            System.out.println(content.getPoint());
//        }
//        GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
//        String key = "city:location";
//        Point point1 = new Point(116.28D, 39.55D);
//        Point point2 = new Point(116.28D, 39.55D);
//        Point point3 = new Point(116.58D, 39.75D);
//        Point point4 = new Point(116.78D, 39.85D);
//        geoOperations.add(key, point1, "user1");
//        geoOperations.add(key, point2, "user2");
//        geoOperations.add(key, point3, "user3");
//        geoOperations.add(key, point4, "user4");
//        // 查询距离
//        Distance distance1 = geoOperations.distance(key, "user1", "user4", Metrics.METERS);
//        assert distance1 != null;
//        System.out.println(distance1.getNormalizedValue());
//        System.out.println(distance1.getValue());
//        System.out.println("===============================");
//        Distance distance = new Distance(1000, Metrics.METERS);
//        // 查询圆心范围内的member
//        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOperations.radius(key, "user1", distance);
//        assert results != null;
//        for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoResult : results.getContent()) {
//            System.out.println(geoResult.getContent().getPoint());
//            System.out.println(geoResult.getDistance().getValue());
//            System.out.println(geoResult.getDistance().getNormalizedValue());
//            System.out.println(geoResult.getContent().getName());
//        }
//        System.out.println("============================");
//        // 将指定member的坐标转为hash字符串形式并返回
//        List<String> hashList = geoOperations.hash(key, "user1");
//        System.out.println(JSON.toJSONString(hashList));
//
//        System.out.println("============================");
//        // 返回指定member的坐标
//        List<Point> positionList = geoOperations.position(key, "user2");
//        System.out.println(JSON.toJSONString(positionList));
//
//        System.out.println("==================================");
        // 在指定范围内搜索member，并按照与指定点之间的距离排序后返回。范围可以是圆形或矩形
//        Distance distance2 = new Distance(1000D, Metrics.METERS);
//        Circle circle = new Circle(point4, distance2);
//        GeoResults<RedisGeoCommands.GeoLocation<String>> searchResult = redisService.redisTemplate.opsForGeo().search(key, circle);
//        assert searchResult != null;
//        for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoResult : searchResult.getContent()) {
//            RedisGeoCommands.GeoLocation<String> content = geoResult.getContent();
//            System.out.println(content.getPoint());
//            System.out.println(content.getName());
//        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private CompletableFuture<Void> asyncBatchSave(List<ChatMessagesType> list) {
        return CompletableFuture.runAsync(() -> {
            long l = DateUtil.currentSeconds();
            boolean b = iChatMessagesTypeService.saveBatch(list);
            if (!b) {
                throw new BizException("批量插入失败");
            }
            log.info("完成了啊->" + (DateUtil.currentSeconds() - l));
        });
    }

    public static <T> List<List<T>> splistList(List<T> list, int subNum) {
        List<List<T>> tNewList = new ArrayList<List<T>>();
        int priIndex = 0;
        int lastPriIndex = 0;
        int insertTimes = list.size() / subNum;
        List<T> subList = new ArrayList<>();
        for (int i = 0; i <= insertTimes; i++) {
            priIndex = subNum * i;
            lastPriIndex = priIndex + subNum;
            if (i == insertTimes) {
                subList = list.subList(priIndex, list.size());
            } else {
                subList = list.subList(priIndex, lastPriIndex);
            }
            if (subList.size() > 0) {
                tNewList.add(subList);
            }
        }
        return tNewList;
    }


    public static void main(String[] args) throws IOException {
        String jsonStr = "{\"checksum\":\"b12b9bff612003cd1212ab25de79264b2c90e74dfa8f6422bcab1b9e95ba3cbd\",\"content\":\"{\\\"code\\\":200,\\\"dataId\\\":\\\"0-888\\\",\\\"extras\\\":{\\\"frameCount\\\":\\\"5\\\",\\\"framePrefix\\\":\\\"http://aligreen-shanghai.oss-cn-shanghai-internal.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f0000\\\"},\\\"msg\\\":\\\"OK\\\",\\\"results\\\":[{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"porn\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"terrorism\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"frames\\\":[{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":0,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00001.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=6IOI%2B0djv6ktns5SasRML1pIeqA%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":2,\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00002.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=xp3IS2ZQxcbai3o81FvRulT%2FyCo%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":6,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00004.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=d6B08B26n6S%2BGRQ04BFeUE3yhoE%3D\\\"},{\\\"label\\\":\\\"ad\\\",\\\"offset\\\":8,\\\"qrcodeData\\\":[\\\"https://v.kuaishou.com/fv20nX\\\"],\\\"qrcodeLocations\\\":[{\\\"h\\\":153.0,\\\"qrcode\\\":\\\"https://v.kuaishou.com/fv20nX\\\",\\\"w\\\":153.0,\\\"x\\\":193.0,\\\"y\\\":615.0}],\\\"rate\\\":99.91,\\\"url\\\":\\\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00005.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=GbJoJQJ%2BybD9GYRN9rUVsnqi%2BUk%3D\\\"}],\\\"label\\\":\\\"ad\\\",\\\"rate\\\":99.91,\\\"scene\\\":\\\"ad\\\",\\\"suggestion\\\":\\\"review\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"live\\\",\\\"suggestion\\\":\\\"pass\\\"},{\\\"label\\\":\\\"normal\\\",\\\"rate\\\":99.9,\\\"scene\\\":\\\"logo\\\",\\\"suggestion\\\":\\\"pass\\\"}],\\\"taskId\\\":\\\"vi7kZX3OZ0bEy7ScDElLnu@r-1xsDKF\\\",\\\"url\\\":\\\"http://oss.paofoo.com/Userprofile/Video/2023.01.17/1673894848103.mp4?Expires=1981478848&OSSAccessKeyId=LTAI4GHVDnZ7fWcC6ctPBEKh&Signature=XM3R6ptYnmvMm9MXxwnRcYF%2Bm2o%3D\\\"}\"}";

        String code = "341df6165ef0e40a69f737cc1f59aaf6512a0c22943b5a78de4939fc7e028b63";
        String content = "{\"code\":200,\"dataId\":\"0-888\",\"extras\":{\"frameCount\":\"5\",\"framePrefix\":\"http://aligreen-shanghai.oss-cn-shanghai-internal.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f0000\"},\"msg\":\"OK\",\"results\":[{\"label\":\"normal\",\"rate\":99.9,\"scene\":\"porn\",\"suggestion\":\"pass\"},{\"label\":\"normal\",\"rate\":99.9,\"scene\":\"terrorism\",\"suggestion\":\"pass\"},{\"frames\":[{\"label\":\"ad\",\"offset\":0,\"rate\":99.91,\"url\":\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00001.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=6IOI%2B0djv6ktns5SasRML1pIeqA%3D\"},{\"label\":\"ad\",\"offset\":2,\"rate\":99.91,\"url\":\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00002.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=xp3IS2ZQxcbai3o81FvRulT%2FyCo%3D\"},{\"label\":\"ad\",\"offset\":6,\"qrcodeData\":[\"https://v.kuaishou.com/fv20nX\"],\"qrcodeLocations\":[{\"h\":153.0,\"qrcode\":\"https://v.kuaishou.com/fv20nX\",\"w\":153.0,\"x\":193.0,\"y\":615.0}],\"rate\":99.91,\"url\":\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00004.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=d6B08B26n6S%2BGRQ04BFeUE3yhoE%3D\"},{\"label\":\"ad\",\"offset\":8,\"qrcodeData\":[\"https://v.kuaishou.com/fv20nX\"],\"qrcodeLocations\":[{\"h\":153.0,\"qrcode\":\"https://v.kuaishou.com/fv20nX\",\"w\":153.0,\"x\":193.0,\"y\":615.0}],\"rate\":99.91,\"url\":\"https://aligreen-shanghai.oss-cn-shanghai.aliyuncs.com/prod/hammal/1982b830/477072_1673894848103.mp4-frames/f00005.jpg?Expires=1674131944&OSSAccessKeyId=H4sp5QfNbuDghquU&Signature=GbJoJQJ%2BybD9GYRN9rUVsnqi%2BUk%3D\"}],\"label\":\"ad\",\"rate\":99.91,\"scene\":\"ad\",\"suggestion\":\"review\"},{\"label\":\"normal\",\"rate\":99.9,\"scene\":\"live\",\"suggestion\":\"pass\"},{\"label\":\"normal\",\"rate\":99.9,\"scene\":\"logo\",\"suggestion\":\"pass\"}],\"taskId\":\"vi7kZX3OZ0bEy7ScDElLnu@r-1xsDKF\",\"url\":\"http://oss.paofoo.com/Userprofile/Video/2023.01.17/1673894848103.mp4?Expires=1981478848&OSSAccessKeyId=LTAI4GHVDnZ7fWcC6ctPBEKh&Signature=XM3R6ptYnmvMm9MXxwnRcYF%2Bm2o%3D\"}";
        JSONObject jsonObject = JSON.parseObject(content);

        String encode = "270571842353286739" + "pfVideoCheckSeed" + jsonObject.toJSONString();
        Digester sha256 = new Digester(DigestAlgorithm.SHA256);
        String encodeContent = sha256.digestHex(encode);
//        String encodeContent = HexUtil.encodeHexStr(digest);
//        String encodeContent = StrUtil.utf8Str(digest);
        System.out.println("加密后->" + encodeContent);

        System.out.println(code.equals(encodeContent));
        System.out.println("=====================================");

        System.out.println(RedisKeyConstant.SOCIAL_SUPERIOR_DYNAMIC);
        System.out.println(RedisKeyConstant.SOCIAL_GENERAL_DYNAMICS);
        System.out.println(RedisKeyConstant.SOCIAL_OLD_DYNAMIC);
        System.out.println(RedisKeyConstant.SOCIAL_ACTIVITY);
        System.out.println(RedisKeyConstant.SOCIAL_CHAT);
        if (true) {
            return;
        }

        System.out.println("开始执行");
        for (int i = 0; i < 2; i++) {
            ConcurrencyTester concurrencyTester = ThreadUtil.concurrencyTest(250, TestController::test);
            concurrencyTester.close();
        }
//        ThreadUtil.concurrencyTest(100, TestController::test2);
        System.out.println("结束执行");
    }

    private static void test() {
        try {
            String body = "{\n" + "    \"quantity\": 5,\n" + "    \"expirationDays\": 10,\n" + "    \"cardTypeId\": 1\n" + "}";
            HttpRequest post = HttpUtil.createPost("http://127.0.0.1:8801/sportServiceVipCard/insertOneBatch");
            post.header("X-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEyMiwiYXBwbGljYXRpb25OYW1lIjoiYmFja3N0YWdlIiwiaXNzIjoiMTIyIiwic3ViIjoiZ3pjIiwiaWF0IjoxNjcxNTg5MTgzLCJleHAiOjE2NzI3OTg4NDMsIm5iZiI6MTY3MTU4OTE4M30.UmBWHo5Qq1cdRMnNXkwYU1sGHBtEPguTdy4Q8yzEPkA");
            post.body(body);
            HttpResponse execute = post.execute();
            System.out.println(execute.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test2() {
        try {
            String body = "{\"batchNo\": \"221201\"}";
            HttpRequest post = HttpUtil.createPost("http://127.0.0.1:8801/sportServiceVipCard/activate");
            post.header("X-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEyMiwiYXBwbGljYXRpb25OYW1lIjoiYmFja3N0YWdlIiwiaXNzIjoiMTIyIiwic3ViIjoiZ3pjIiwiaWF0IjoxNjcxNTg5MTgzLCJleHAiOjE2NzI3OTg4NDMsIm5iZiI6MTY3MTU4OTE4M30.UmBWHo5Qq1cdRMnNXkwYU1sGHBtEPguTdy4Q8yzEPkA");
            post.body(body);
            HttpResponse execute = post.execute();
            System.out.println(execute.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
