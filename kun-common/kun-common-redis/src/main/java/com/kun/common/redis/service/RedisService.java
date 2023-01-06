package com.kun.common.redis.service;

import cn.hutool.core.collection.CollUtil;
import com.kun.common.core.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.domain.geo.GeoLocation;
import org.springframework.data.redis.domain.geo.Metrics;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 服务类
 *
 * @author gzc
 * @since 2022/9/30 20:42
 */
@RequiredArgsConstructor
@SuppressWarnings("all")
public class RedisService {

    public final RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }


    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void set(final String key, Long value, final Long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 添加经纬度信息
     *
     * @param key    Redis键
     * @param x      经度
     * @param y      纬度
     * @param member 成员
     * @return 添加的元素数量
     */
    public Long addGeo(final String key, final double x, final double y, final String member) {
        return redisTemplate.opsForGeo().add(key, new Point(x, y), member);
    }

    /**
     * 获取经纬度信息
     *
     * @param key    Redis键
     * @param member 成员
     * @return 经纬度对象
     */
    public Point getGeo(final String key, final String member) {
        Map<String, Point> geoList = getGeoList(key, member);
        return CollUtil.isNotEmpty(geoList) ? geoList.get(member) : null;
    }

    /**
     * 获取多个经纬度信息
     *
     * @param key     Redis键
     * @param members 成员数组
     * @return 成员对应的经纬度信息集合
     */
    public Map<String, Point> getGeoList(final String key, final String... members) {
        List<Point> list = redisTemplate.opsForGeo().position(key, members);
        Map<String, Point> map = new HashMap<>(members.length);
        if (CollUtil.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                map.put(members[i], list.get(i));
            }
        }
        return map;
    }

    /**
     * 计算两个成员经纬度之间的直线距离
     *
     * @param key     Redis键
     * @param member1 成员1
     * @param member2 成员2
     * @return 距离(单位米)
     */
    public Double distanceGeo(final String key, final String member1, final String member2) {
        Distance distance = redisTemplate.opsForGeo().distance(key, member1, member2, Metrics.METERS);
        return distance != null ? distance.getValue() : null;
    }

    /**
     * 查询圆心范围内的所有成员
     *
     * @param key      Redis键
     * @param member   成员
     * @param distance 圆的半径距离(米)
     * @return 所有包含在圆内的成员信息（直线距离由近到远）
     */
    public List<GeoResult<GeoLocation<String>>> radiusGeo(final String key, final String member, final long distance) {
        GeoResults<GeoLocation<String>> results = redisTemplate.opsForGeo().radius(key, member, distance);
        if (results != null) {
            // 平均距离
            Distance averageDistance = results.getAverageDistance();
            List<GeoResult<GeoLocation<String>>> content = results.getContent();
//            List<GeoResult<RedisGeoCommands.GeoLocation<String>>> resultsContent = results.getContent();
            return content;
//            if (CollUtil.isNotEmpty(resultsContent)) {
//                for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResult : resultsContent) {
//                    // 直线距离
//                    Distance resultDistance = geoLocationGeoResult.getDistance();
//                    RedisGeoCommands.GeoLocation<String> content = geoLocationGeoResult.getContent();
//                    // 成员名称
//                    String name = content.getName();
//                    // 成员坐标
//                    Point point = content.getPoint();
//                }
//            }
        }
        return new ArrayList<>();
    }

}
