package com.kun.blog.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.kun.blog.anno.AnonymousAccess;
import com.kun.blog.entity.po.ChatMessagesType;
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
import org.springframework.data.redis.domain.geo.GeoLocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        String id = "432524200207282532";
        System.out.println(IdcardUtil.getProvinceByIdCard(id));
        System.out.println(IdcardUtil.getGenderByIdCard(id));
        System.out.println();
//        String yyMM = new SimpleDateFormat("yyMM").format(new Date());
//        System.out.println(yyMM);
//        System.out.println(yyMM.contains("221201"));
//        System.out.println("221201".contains(yyMM));
//        System.out.println("221201".substring("221201".length()-2));
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
