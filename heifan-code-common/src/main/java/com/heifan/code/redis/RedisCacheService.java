package com.heifan.code.redis;

import org.springframework.data.redis.connection.RedisZSetCommands;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis功能定义
 *
 * @author 于工头
 * @create 2022/1/30
 **/
public interface RedisCacheService {

    /**
     * 查找所有符合给定模式 pattern 的 key ， 比如说：
     * KEYS * 匹配数据库中所有 key 。
     * KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。
     * KEYS h*llo 匹配 hllo 和 heeeeello 等。
     * KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。
     * 特殊符号用 \ 隔开。
     * {@link = http://redisdoc.com/database/keys.html}
     * 可用版本： >= 1.0.0
     * 时间复杂度： O(N)， N 为数据库中 key 的数量。
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 检查key是否存在
     * {@link = http://redisdoc.com/database/exists.html}
     * 可用版本： >= 1.0.0
     * 时间复杂度： O(1)
     *
     * @param key
     * @return
     */
    Boolean exists(String key);

    /**
     * 设置过期时间
     *
     * @param redisKey
     * @param expire
     * @param timeUnit
     * @return
     */
    Boolean expire(String redisKey, long expire, TimeUnit timeUnit);

    /**
     * 设置过期的字符串
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    Boolean setStr(String key, String value, Long timeout);

    /**
     * 设置value自增数量
     *
     * @param key
     * @return
     */
    Long increment(String key);

    /**
     * 设置不过期的字符串
     *
     * @param key
     * @param value
     * @return
     */
    Boolean setStr(String key, String value);

    /**
     * 获取字符串key的value
     *
     * @param key
     * @return
     */
    String getStr(String key);

    /**
     * 获取字符串key的value
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getStr(String key, Class<T> clazz);

    /**
     * @param list
     * @param clazz
     * @param <T>
     * @param <R>
     * @return
     */
    <T, R> List<T> mGet(List<R> list, Class<T> clazz);

    /**
     * 根据key删除缓存
     *
     * @param key
     * @return
     */
    Long del(String key);

    /**
     * 判断member是否在set中
     *
     * @param key
     * @param value
     * @return
     */
    Boolean sIsMember(String key, String value);

    /**
     * 获取set 中的元素
     *
     * @param key
     * @return
     */
    Long sCard(String key);

    /**
     * set 新增value
     *
     * @param key
     * @param values
     * @return
     */
    Boolean sAdd(String key, String values);

    /**
     * set 移除value
     *
     * @param key
     * @param values
     * @return
     */
    Boolean sRem(String key, String values);

    /**
     * hmSet
     *
     * @param key
     * @param m
     */
    void hSet(String key, Map m);

    Boolean hSet(final String key, final String field, final String value);

    Boolean hExists(final String key, final String field);

    <T> T hGet(String key, String hashKey, Class<T> clazz);

    /**
     * hget
     *
     * @param key
     * @param hashKey
     * @return
     */
    Object hGet(String key, String hashKey);

    Boolean zAdd(final String key, final double score, final String value);

    Long zAdd(final String key, Set<RedisZSetCommands.Tuple> tuples);

    Double zScore(final String key, final String value);

    Double zIncrBy(final String key, final double increment, final String value);

    Long zCard(final String key);

    Long zCount(final String key, final RedisZSetCommands.Range range);

    <T> Set<T> zRange(final String key, final long start, final long end, Class<T> clazz);

    Set<byte[]> zRange(final String key, final long start, final long end);

    Set<byte[]> zRevRange(final String key, final long start, final long end);

    Set<byte[]> zRangeByScore(final String key, final double min, final double max);

    Set<RedisZSetCommands.Tuple> zRangeWithScores(final String key, final long start, final long end);

    Set<RedisZSetCommands.Tuple> zRevRangeWithScores(final String key, final long start, final long end);

    Set<byte[]> zRevRangeByScore(final String key, final double min, final double max);

    Set<byte[]> zRevRangeByScore(final String key, final double min, final double max, long offset, long count);

    Set<RedisZSetCommands.Tuple> zRangeWithScores(final String key, final double min, final double max, long offset, long count);

    Long zRank(final String key, final String value);

    Long zRevRank(final String key, final double score, final String value);

    Long zRem(final String key, final String value);

    Long zMRem(final String key, final List<String> fields);

    Long zRemRangeByScore(final String key, final RedisZSetCommands.Range range);

    Set<byte[]> zRangeByLex(final String key, final RedisZSetCommands.Range range);

    Long zUnionStore(final String destKey, List<String> sets);

    Long zInterStore(final String destKey, final byte[]... sets);

    Boolean setNx(String key, int seconds);
}
