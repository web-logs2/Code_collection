package com.heifan.code.redis.impl;

import com.alibaba.fastjson.JSON;
import com.heifan.code.redis.RedisCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Redis功能实现
 *
 * @author 于工头
 * @create 2022/1/30
 **/
@Slf4j
@Service("RedisCacheServiceImpl")
public class RedisCacheServiceImpl implements RedisCacheService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Set<String> keys(String pattern) {
        return (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
            Set<byte[]> sets = connection.keys(stringRedisSerializer.serialize(pattern));
            Set<String> keys = new LinkedHashSet<>();
            if (null == sets) {
                return keys;
            }
            sets.forEach(item -> keys.add(stringRedisSerializer.deserialize(item)));
            return keys;
        });
    }

    @Override
    public Boolean exists(String key) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
            return connection.exists(stringRedisSerializer.serialize(key));
        });
    }

    @Override
    public Boolean expire(String redisKey, long expire, TimeUnit timeUnit) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
            long rawTimeout = TimeoutUtils.toMillis(expire, timeUnit);
            return connection.pExpire(stringRedisSerializer.serialize(redisKey),rawTimeout);
        });
    }

    @Override
    public Boolean setStr(String key, String value, Long timeout) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
            return connection.setEx(
                    stringSerializer.serialize(key),
                    timeout,
                    stringSerializer.serialize(value));
        });
    }

    @Override
    public Long increment(String key) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
            return connection.incr(stringSerializer.serialize(key));
        });
    }


    @Override
    public Boolean setStr(String key, String value) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
            return connection.set(
                    stringSerializer.serialize(key),
                    stringSerializer.serialize(value));
        });
    }

    @Override
    public String getStr(String key) {
        return (String) redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
            byte[] value = connection.get(
                    stringSerializer.serialize(key));
            return (String) stringSerializer.deserialize(value);
        });
    }

    @Override
    public <T> T getStr(String key, Class<T> clazz) {
        return (T) redisTemplate.execute((RedisCallback<T>) connection -> {
            RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
            byte[] bytes = connection.get(stringRedisSerializer.serialize(key));
            if (null == bytes) {
                return null;
            }
            String json = stringRedisSerializer.deserialize(bytes);
            if (null == json) {
                return null;
            }
            return JSON.parseObject(json, clazz);
        });
    }

    @Override
    public <T, R> List<T> mGet(List<R> list, Class<T> clazz) {
        return (List<T>) redisTemplate.execute((RedisCallback<List<T>>) connection -> {
            byte[][] bkeys = new byte[list.size()][];
            for (int i = 0; i < list.size(); i++) {
                bkeys[i] = list.get(i).toString().getBytes();
            }
            RedisSerializer<String> stringRedisSerializer = redisTemplate.getStringSerializer();
            List<byte[]> byteList = connection.mGet(bkeys);
            List<T> result = new ArrayList<>();
            for (int i = 0; i < byteList.size(); i++) {
                result.add(JSON.parseObject(stringRedisSerializer.deserialize(byteList.get(i)), clazz));
            }
            return result;

        });
    }


    @Override
    public Long del(String key) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
            return connection.del(
                    stringSerializer.serialize(key));
        });
    }

    @Override
    public Boolean sIsMember(String key, String value) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer stringRedisSerializer = redisTemplate.getStringSerializer();
            return connection.sIsMember(stringRedisSerializer.serialize(key),
                    stringRedisSerializer.serialize(value));
        });
    }

    @Override
    public Long sCard(String key) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer stringRedisSerializer = redisTemplate.getStringSerializer();
            return connection.sCard(stringRedisSerializer.serialize(key));
        });
    }

    @Override
    public Boolean sAdd(String key, String values) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer stringRedisSerializer = redisTemplate.getStringSerializer();
            Long result = connection.sAdd(stringRedisSerializer.serialize(key), stringRedisSerializer.serialize(values));
            if (null == result || result == 0) {
                return false;
            }
            return true;
        });
    }

    @Override
    public Boolean sRem(String key, String values) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer stringRedisSerializer = redisTemplate.getStringSerializer();
            Long result = connection.sRem(stringRedisSerializer.serialize(key), stringRedisSerializer.serialize(values));
            if (null == result || result == 0) {
                return false;
            }
            return true;
        });
    }

    @Override
    public void hSet(String key, Map map) {
        redisTemplate.execute((RedisCallback) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            String mapKey;
            Object value;
            Map<byte[], byte[]> hashes = new LinkedHashMap(map.size());
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                mapKey = entry.getKey();
                value = entry.getValue();
                hashes.put(stringRedisSerializer.serialize(mapKey),
                        stringRedisSerializer.serialize(JSON.toJSONString(value)));
            }
            connection.hMSet(stringRedisSerializer.serialize(key),
                    hashes
            );
            return null;
        });
    }

    @Override
    public Object hGet(String key, String hashKey) {
        return redisTemplate.execute((RedisCallback) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] bytes = connection.hGet(stringRedisSerializer.serialize(key),
                    stringRedisSerializer.serialize(hashKey));
            if (null == bytes) {
                return null;
            }
            return stringRedisSerializer.deserialize(bytes);
        });
    }


    /**
     * 可用版本： >= 2.0.0
     * 时间复杂度： O(1)
     * 将哈希表 hash 中域 field 的值设置为 value 。
     * 如果给定的哈希表并不存在， 那么一个新的哈希表将被创建并执行 HSET 操作。
     * 如果域 field 已经存在于哈希表中， 那么它的旧值将被新值 value 覆盖。
     *
     * @param key
     * @param field
     * @param value
     * @return Boolean
     * @Title: hSet
     */
    @Override
    public Boolean hSet(final String key, final String field, final String value) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keys = stringRedisSerializer.serialize(key);
            byte[] fields = stringRedisSerializer.serialize(field);
            byte[] values = stringRedisSerializer.serialize(value);
            return connection.hSet(keys, fields, values);
        });
    }


    @Override
    public Boolean hExists(final String key, final String field) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keys = stringRedisSerializer.serialize(key);
            byte[] fields = stringRedisSerializer.serialize(field);
            return connection.hExists(keys, fields);
        });
    }

    @Override
    public <T> T hGet(String key, String hashKey, Class<T> clazz) {
        return (T) redisTemplate.execute((RedisCallback<T>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] bytes = connection.hGet(stringRedisSerializer.serialize(key),
                    stringRedisSerializer.serialize(hashKey));
            String json = stringRedisSerializer.deserialize(bytes);
            if (null == json) {
                return null;
            }
            return JSON.parseObject(json, clazz);
        });
    }

    /**
     * 可用版本： >= 1.2.0 时间复杂度: O(M*log(N))， N 是有序集的基数， M 为成功添加的新成员的数量。 将一个或多个 member
     * 元素及其 score 值加入到有序集 key 当中。 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score
     * 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。 score 值可以是整数值或双精度浮点数。 如果 key
     * 不存在，则创建一个空的有序集并执行 ZADD 操作。 当 key 存在但不是有序集类型时，返回一个错误。
     *
     * @param key
     * @param score
     * @param value
     * @return
     */
    @Override
    public Boolean zAdd(final String key, final double score, final String value) {
        return (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            byte[] valueByte = stringRedisSerializer.serialize(value);
            Boolean zAdd = connection.zAdd(keyByte, score, valueByte);
            if (zAdd == false) {
                // 兼容value、score相同的情况也会返回false
                Double zScore = connection.zScore(keyByte, valueByte);
                if (null != zScore) {
                    return true;
                }
            }
            return zAdd;
        });
    }

    @Override
    public Long zAdd(final String key, Set<RedisZSetCommands.Tuple> tuples) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            return connection.zAdd(keyByte, tuples);
        });
    }

    /**
     * 可用版本： >= 1.2.0 时间复杂度: O(1) 返回有序集 key 中，成员 member 的 score 值。 如果 member 元素不是有序集
     * key 的成员，或 key 不存在，返回 nil 。
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Double zScore(final String key, final String value) {
        return (Double) redisTemplate.execute((RedisCallback<Double>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            byte[] valueByte = stringRedisSerializer.serialize(value);
            return connection.zScore(keyByte, valueByte);
        });
    }

    /**
     * 可用版本： >= 1.2.0 时间复杂度: O(log(N)) 为有序集 key 的成员 member 的 score 值加上增量 increment 。
     * 可以通过传递一个负数值 increment ，让 score 减去相应的值，比如 ZINCRBY key -5 member ，就是让 member 的
     * score 值减去 5 。 当 key 不存在，或 member 不是 key 的成员时， ZINCRBY key increment member
     * 等同于 ZADD key increment member 。 当 key 不是有序集类型时，返回一个错误。 score 值可以是整数值或双精度浮点数。
     *
     * @param key
     * @param increment
     * @param value
     * @return
     */
    @Override
    public Double zIncrBy(final String key, final double increment, final String value) {
        return (Double) redisTemplate.execute((RedisCallback<Double>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            byte[] valueByte = stringRedisSerializer.serialize(value);
            return connection.zIncrBy(keyByte, increment, valueByte);
        });
    }

    /**
     * 可用版本： >= 1.2.0 时间复杂度: O(1) 返回有序集 key 的基数。
     *
     * @param key
     * @return
     */
    @Override
    public Long zCard(final String key) {
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> stringRedisSerializer
                        = redisTemplate.getStringSerializer();
                byte[] keyByte = stringRedisSerializer.serialize(key);
                return connection.zCard(keyByte);
            }
        });
    }

    /**
     * 可用版本： >= 2.0.0 时间复杂度: O(log(N))， N 为有序集的基数。 返回有序集 key 中， score 值在 min 和 max
     * 之间(默认包括 score 值等于 min 或 max )的成员的数量。
     *
     * @param key
     * @param range
     * @return
     */
    @Override
    public Long zCount(final String key, final RedisZSetCommands.Range range) {
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> stringRedisSerializer
                        = redisTemplate.getStringSerializer();
                byte[] keyByte = stringRedisSerializer.serialize(key);
                return connection.zCount(keyByte, range);
            }
        });
    }

    @Override
    public <T> Set<T> zRange(final String key, final long start, final long end, Class<T> clazz) {
        return (Set<T>) redisTemplate.execute((RedisCallback<Set<T>>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            Set<byte[]> bytes = connection.zRange(keyByte, start, end);
            Set<T> set = new HashSet<>(bytes.size());
            if (null != bytes) {
                for (byte[] b : bytes) {
                    Object value = stringRedisSerializer.deserialize(b);
                    if (null != value) {
                        set.add(JSON.parseObject(value.toString(), clazz));
                    }
                }
            }
            return set;
        });
    }

    /**
     * 可用版本： >= 1.2.0 时间复杂度: O(log(N)+M)， N 为有序集的基数，而 M 为结果集的基数。 返回有序集 key
     * 中，指定区间内的成员。 其中成员的位置按 score 值递增(从小到大)来排序。 具有相同 score 值的成员按字典序(lexicographical
     * order )来排列。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public Set<byte[]> zRange(final String key, final long start, final long end) {
        return (Set<byte[]>) redisTemplate.execute((RedisCallback<Set<byte[]>>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            return connection.zRange(keyByte, start, end);
        });
    }


    /**
     * 可用版本： >= 1.2.0 时间复杂度: O(log(N)+M)， N 为有序集的基数，而 M 为结果集的基数。 返回有序集 key
     * 中，指定区间内的成员。 其中成员的位置按 score 值递减(从大到小)来排列。 具有相同 score 值的成员按字典序的逆序(reverse
     * lexicographical order)排列。 除了成员按 score 值递减的次序排列这一点外， ZREVRANGE 命令的其他方面和 ZRANGE
     * key start stop [WITHSCORES] 命令一样。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public Set<byte[]> zRevRange(final String key, final long start, final long end) {
        return (Set<byte[]>) redisTemplate.execute((RedisCallback<Set<byte[]>>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            return connection.zRevRange(keyByte, start, end);
        });
    }

    /**
     * 可用版本： >= 1.0.5 时间复杂度: O(log(N)+M)， N 为有序集的基数， M 为被结果集的基数。 返回有序集 key 中，所有
     * score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。 具有相同
     * score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    @Override
    public Set<byte[]> zRangeByScore(final String key, final double min, final double max) {
        return (Set<byte[]>) redisTemplate.execute((RedisCallback<Set<byte[]>>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            return connection.zRangeByScore(keyByte, min, max);
        });
    }

    /**
     * 根据索引区间获取元素
     * score 值递增(从小到大)
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public Set<RedisZSetCommands.Tuple> zRangeWithScores(final String key, final long start, final long end) {
        return (Set<RedisZSetCommands.Tuple>) redisTemplate.execute((RedisCallback<Set<RedisZSetCommands.Tuple>>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            return connection.zRangeWithScores(keyByte, start, end);
        });
    }

    /**
     * 根据索引区间获取元素
     * score 值递增(从大到小)
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public Set<RedisZSetCommands.Tuple> zRevRangeWithScores(final String key, final long start, final long end) {
        return (Set<RedisZSetCommands.Tuple>) redisTemplate.execute((RedisCallback<Set<RedisZSetCommands.Tuple>>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            return connection.zRevRangeWithScores(keyByte, start, end);
        });
    }

    /**
     * 可用版本： >= 2.2.0 时间复杂度: O(log(N)+M)， N 为有序集的基数， M 为结果集的基数。 返回有序集 key 中， score
     * 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。 具有相同
     * score 值的成员按字典序的逆序(reverse lexicographical order )排列。 除了成员按 score
     * 值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE key min max
     * [WITHSCORES] [LIMIT offset count] 命令一样。
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    @Override
    public Set<byte[]> zRevRangeByScore(final String key, final double min, final double max) {
        return (Set<byte[]>) redisTemplate.execute((RedisCallback<Set<byte[]>>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            return connection.zRevRangeByScore(keyByte, min, max);
        });
    }

    /**
     * 可用版本： >= 2.2.0 时间复杂度: O(log(N)+M)， N 为有序集的基数， M 为结果集的基数。 返回有序集 key 中， score
     * 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。 具有相同
     * score 值的成员按字典序的逆序(reverse lexicographical order )排列。 除了成员按 score
     * 值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE key min max
     * [WITHSCORES] [LIMIT offset count] 命令一样。
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    @Override
    public Set<byte[]> zRevRangeByScore(final String key, final double min, final double max, long offset, long count) {
        return (Set<byte[]>) redisTemplate.execute((RedisCallback<Set<byte[]>>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            return connection.zRevRangeByScore(keyByte, min, max, offset, count);
        });
    }

    @Override
    public Set<RedisZSetCommands.Tuple> zRangeWithScores(final String key, final double min, final double max, long offset, long count) {
        return (Set<RedisZSetCommands.Tuple>) redisTemplate.execute((RedisCallback<Set<RedisZSetCommands.Tuple>>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            return connection.zRangeByScoreWithScores(keyByte, min, max, offset, count);
        });
    }

    /**
     * 可用版本： >= 2.0.0 时间复杂度: O(log(N)) 返回有序集 key 中成员 member 的排名。其中有序集成员按 score
     * 值递增(从小到大)顺序排列。 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。 使用 ZREVRANK key member
     * 命令可以获得成员按 score 值递减(从大到小)排列的排名。
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Long zRank(final String key, final String value) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            byte[] valueByte = stringRedisSerializer.serialize(value);
            return connection.zRank(keyByte, valueByte);
        });
    }

    /**
     * 可用版本： >= 2.0.0 时间复杂度: O(log(N)) 返回有序集 key 中成员 member 的排名。其中有序集成员按 score
     * 值递减(从大到小)排序。 排名以 0 为底，也就是说， score 值最大的成员排名为 0 。 使用 ZRANK key member 命令可以获得成员按
     * score 值递增(从小到大)排列的排名。
     *
     * @param key
     * @param score
     * @param value
     * @return
     */
    @Override
    public Long zRevRank(final String key, final double score, final String value) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            byte[] valueByte = stringRedisSerializer.serialize(value);
            return connection.zRevRank(keyByte, valueByte);
        });
    }

    /**
     * 可用版本： >= 1.2.0 时间复杂度: O(M*log(N))， N 为有序集的基数， M 为被成功移除的成员的数量。 移除有序集 key
     * 中的一个或多个成员，不存在的成员将被忽略。 当 key 存在但不是有序集类型时，返回一个错误。
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Long zRem(final String key, final String value) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            byte[] valueByte = stringRedisSerializer.serialize(value);
            return connection.zRem(keyByte, valueByte);
        });
    }

    /**
     * 可用版本： >= 1.2.0 时间复杂度: O(M*log(N))， N 为有序集的基数， M 为被成功移除的成员的数量。 移除有序集 key
     * 中的一个或多个成员，不存在的成员将被忽略。 当 key 存在但不是有序集类型时，返回一个错误。
     *
     * @param key
     * @param fields 删除多数据
     * @return
     */
    @Override
    public Long zMRem(final String key, final List<String> fields) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keys = stringRedisSerializer.serialize(key);
            List<byte[]> values = new ArrayList<>(fields.size());
            for (String field : fields) {
                byte[] value = stringRedisSerializer.serialize(field);
                values.add(value);
            }
            byte[][] valueArr = new byte[values.size()][];
            return connection.zRem(keys,
                    values.toArray(valueArr));

        });
    }

    /**
     * 可用版本： >= 1.2.0 时间复杂度： O(log(N)+M)， N 为有序集的基数，而 M 为被移除成员的数量。 移除有序集 key 中，所有
     * score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。 自版本2.1.6开始， score 值等于 min 或 max
     * 的成员也可以不包括在内，详情请参见 ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]
     * 命令。
     *
     * @param key
     * @param range
     * @return
     */
    @Override
    public Long zRemRangeByScore(final String key, final RedisZSetCommands.Range range) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            return connection.zRemRangeByScore(keyByte, range);
        });
    }

    /**
     * 可用版本： >= 2.8.9 时间复杂度：O(log(N)+M)， 其中 N 为有序集合的元素数量， 而 M 则是命令返回的元素数量。 如果 M
     * 是一个常数（比如说，用户总是使用 LIMIT 参数来返回最先的 10 个元素）， 那么命令的复杂度也可以看作是 O(log(N)) 。
     * 当有序集合的所有成员都具有相同的分值时， 有序集合的元素会根据成员的字典序（lexicographical ordering）来进行排序，
     * 而这个命令则可以返回给定的有序集合键 key 中， 值介于 min 和 max 之间的成员。 如果有序集合里面的成员带有不同的分值，
     * 那么命令返回的结果是未指定的（unspecified）。
     *
     * @param key
     * @param range
     * @return
     */
    @Override
    public Set<byte[]> zRangeByLex(final String key, final RedisZSetCommands.Range range) {
        return (Set<byte[]>) redisTemplate.execute((RedisCallback<Set<byte[]>>) connection -> {
            RedisSerializer stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] keyByte = stringRedisSerializer.serialize(key);
            return connection.zRangeByLex(keyByte, range);
        });
    }

    /**
     * 可用版本：>= 2.0.0 时间复杂度: O(N)+O(M log(M))， N 为给定有序集基数的总和， M 为结果集的基数。
     * 计算给定的一个或多个有序集的并集，其中给定 key 的数量必须以 numkeys 参数指定，并将该并集(结果集)储存到 destination 。
     * 默认情况下，结果集中某个成员的 score 值是所有给定集下该成员 score 值之 和 。
     *
     * @param destKey
     * @param sets
     * @return
     */
    @Override
    public Long zUnionStore(final String destKey, List<String> sets) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] destKeyByte = stringRedisSerializer.serialize(destKey);
            byte[][] arr = new byte[sets.size()][];
            return connection.zUnionStore(destKeyByte, sets.parallelStream()
                    .map(val -> stringRedisSerializer.serialize(val)).collect(Collectors.toList()).toArray(arr));
        });
    }

    /**
     * 可用版本： >= 2.0.0 时间复杂度: O(N*K)+O(M*log(M))， N 为给定 key 中基数最小的有序集， K 为给定有序集的数量， M
     * 为结果集的基数。 计算给定的一个或多个有序集的交集，其中给定 key 的数量必须以 numkeys 参数指定，并将该交集(结果集)储存到
     * destination 。 默认情况下，结果集中某个成员的 score 值是所有给定集下该成员 score 值之和.
     *
     * @param destKey
     * @param sets
     * @return
     */
    @Override
    public Long zInterStore(final String destKey, final byte[]... sets) {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer stringRedisSerializer
                    = redisTemplate.getStringSerializer();
            byte[] destKeyByte = stringRedisSerializer.serialize(destKey);
            return connection.zInterStore(destKeyByte, sets);
        });
    }

    @Override
    public Boolean setNx(String redisKey, int seconds) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(redisKey, "1", seconds, TimeUnit.SECONDS);
        if (result == null) {
            return false;
        }
        return result;
    }

}
