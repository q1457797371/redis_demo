package com.redis.Dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.geo.GeoRadiusParam;

public class RedisDaoImpl<K, V> implements RedisDao<K, V> {

	private RedisTemplate<K, V> redisTemplate;
	ValueOperations<K, V> opsForValue;
	HashOperations<K, K, V> hashOperations;

	@Autowired
	public RedisDaoImpl(RedisTemplate<K, V> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
		this.opsForValue = redisTemplate.opsForValue();
		this.hashOperations = redisTemplate.opsForHash();
	}

	public Set<K> keysAll(K keys) {
		return redisTemplate.keys(keys);
	}

	
	public void delete(K key) {
		redisTemplate.delete(key);

	}

	
	public void set(K key, V value) {
		opsForValue.set(key, value);
	}

	
	public void set(K key, V value, long timeout, TimeUnit unit) {

		opsForValue.set(key, value, timeout, unit);

	}

	
	public Boolean setIfAbsent(K key, V value) {

		return opsForValue.setIfAbsent(key, value);
	}

	
	public void multiSet(Map<? extends K, ? extends V> m) {
		opsForValue.multiSet(m);
	}

	
	public Boolean multiSetIfAbsent(Map<? extends K, ? extends V> m) {
		return opsForValue.multiSetIfAbsent(m);
	}

	
	public V get(Object key) {

		return opsForValue.get(key);
	}

	
	public V getAndSet(K key, V value) {
		return opsForValue.getAndSet(key, value);
	}

	
	public List<V> multiGet(Collection<K> keys) {
		return opsForValue.multiGet(keys);
	}

	
	public Long increment(K key, long delta) {
		return opsForValue.increment(key, delta);
	}

	
	public Double increment(K key, double delta) {
		return opsForValue.increment(key, delta);
	}

	
	public Integer append(K key, String value) {
		return opsForValue.append(key, value);
	}

	
	public String get(K key, long start, long end) {
		return opsForValue.get(key, start, end);
	}

	
	public void set(K key, V value, long offset) {
		opsForValue.set(key, value, offset);
	}

	
	public Long size(K key) {
		return opsForValue.size(key);
	}

	
	public RedisOperations<K, V> getOperations() {
		return opsForValue.getOperations();
	}

	
	public Boolean setBit(K key, long offset, boolean value) {
		return opsForValue.setBit(key, offset, value);
	}

	
	public Boolean getBit(K key, long offset) {
		return opsForValue.getBit(key, offset);
	}

	
	public void delete(K key, Object... hashKeys) {
		hashOperations.delete(key, hashKeys);
	}

	
	public Boolean hasKey(K key, Object hashKey) {
		return hashOperations.hasKey(key, hashKey);
	}

	
	public V get(K key, Object hashKey) {
		return hashOperations.get(key, hashKey);
	}

	
	public List<V> multiGet(K key, Collection<K> hashKeys) {
		return hashOperations.multiGet(key, hashKeys);
	}

	
	public Long increment(K key, K hashKey, long delta) {
		return hashOperations.increment(key, hashKey, delta);
	}

	
	public Double increment(K key, K hashKey, double delta) {
		return hashOperations.increment(key, hashKey, delta);
	}

	
	public Set<K> keys(K key) {
		return hashOperations.keys(key);
	}

	
	public void putAll(K key, Map<? extends K, ? extends V> m) {
		hashOperations.putAll(key, m);
	}

	
	public void put(K key, K hashKey, V value) {
		hashOperations.put(key, hashKey, value);
	}

	
	public Boolean putIfAbsent(K key, K hashKey, V value) {
		return hashOperations.putIfAbsent(key, hashKey, value);
	}

	
	public List<V> values(K key) {
		return hashOperations.values(key);
	}

	
	public Map<K, V> entries(K key) {
		return hashOperations.entries(key);
	}

	
	public Cursor<Entry<K, V>> scan(K key, ScanOptions options) {
		return hashOperations.scan(key, options);
	}

	
	public Boolean hasKey(K key) {

		return redisTemplate.hasKey(key);
	}

	
	public void delete(Collection<K> key) {
		redisTemplate.delete(key);
	}

	
	public DataType type(K key) {
		return redisTemplate.type(key);
	}

	
	public K randomKey() {
		return redisTemplate.randomKey();
	}

	
	public void rename(K oldKey, K newKey) {
		redisTemplate.rename(oldKey, newKey);
	}

	
	public Boolean renameIfAbsent(K oldKey, K newKey) {
		return redisTemplate.renameIfAbsent(oldKey, newKey);
	}

	
	public Boolean expire(K key, long timeout, TimeUnit unit) {
		return redisTemplate.expire(key, timeout, unit);
	}

	
	public Boolean expireAt(K key, Date date) {
		return redisTemplate.expireAt(key, date);
	}

	
	public Boolean persist(K key) {
		return redisTemplate.persist(key);
	}

	
	public Boolean move(K key, int dbIndex) {
		return redisTemplate.move(key, dbIndex);
	}

	
	public byte[] dump(K key) {
		return redisTemplate.dump(key);
	}

	
	public void restore(K key, byte[] value, long timeToLive, TimeUnit unit) {
		redisTemplate.restore(key, value, timeToLive, unit);
	}

	
	public Long getExpire(K key) {
		return redisTemplate.getExpire(key);
	}

	
	public Long getExpire(K key, TimeUnit timeUnit) {
		return redisTemplate.getExpire(key, timeUnit);
	}

	
	public void watch(K keys) {
		redisTemplate.watch(keys);
	}

	
	public void watch(Collection<K> keys) {
		redisTemplate.watch(keys);
	}

	
	public void unwatch() {
		redisTemplate.unwatch();

	}

	/**
	 * 增加地图信息
	 */
	
	public Long geoadd(final String key, final double longitude,
			final double latitude, final String member) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Jedis j = (Jedis) connection.getNativeConnection();
				System.out.println(key + "---" + longitude);
				return j.geoadd(key, longitude, latitude, member);
			}
		}, true);
	}

	/**
	 * 批量增加地图信息
	 */
	
	public Long geoadd(final String key,
			final Map<String, GeoCoordinate> memberCoordinateMap) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Jedis j = (Jedis) connection.getNativeConnection();
				return j.geoadd(key, memberCoordinateMap);
			}
		});
	}

	
	public Double geodist(final String key, final String member1,
			final String member2) {
		return redisTemplate.execute(new RedisCallback<Double>() {
			
			public Double doInRedis(RedisConnection connection)
					throws DataAccessException {
				Jedis j = (Jedis) connection.getNativeConnection();
				return j.geodist(key, member1, member2);
			}
		});
	}

	
	public Double geodist(final String key, final String member1,
			final String member2, final GeoUnit unit) {
		return redisTemplate.execute(new RedisCallback<Double>() {
			
			public Double doInRedis(RedisConnection connection)
					throws DataAccessException {
				Jedis j = (Jedis) connection.getNativeConnection();
				return j.geodist(key, member1, member2, unit);
			}
		});
	}

	
	public List<String> geohash(final String key, final String... members) {
		return redisTemplate.execute(new RedisCallback<List<String>>() {
			
			public List<String> doInRedis(RedisConnection connection)
					throws DataAccessException {
				Jedis j = (Jedis) connection.getNativeConnection();
				return j.geohash(key, members);
			}
		});
	}

	
	public List<GeoCoordinate> geopos(final String key, final String... members) {
		return redisTemplate.execute(new RedisCallback<List<GeoCoordinate>>() {
			
			public List<GeoCoordinate> doInRedis(RedisConnection connection)
					throws DataAccessException {
				Jedis j = (Jedis) connection.getNativeConnection();
				return j.geopos(key, members);
			}
		});
	}

	
	public List<GeoRadiusResponse> georadius(final String key,
			final double longitude, final double latitude, final double radius,
			final GeoUnit unit) {
		return redisTemplate
				.execute(new RedisCallback<List<GeoRadiusResponse>>() {
					
					public List<GeoRadiusResponse> doInRedis(
							RedisConnection connection)
							throws DataAccessException {
						Jedis j = (Jedis) connection.getNativeConnection();
						return j.georadius(key, longitude, latitude, radius,
								unit);
					}
				});
	}

	
	public List<GeoRadiusResponse> georadius(final String key,final double longitude,
			final double latitude,final double radius,final GeoUnit unit,final GeoRadiusParam param) {
		return redisTemplate
				.execute(new RedisCallback<List<GeoRadiusResponse>>() {
					
					public List<GeoRadiusResponse> doInRedis(
							RedisConnection connection)
							throws DataAccessException {
						Jedis j = (Jedis) connection.getNativeConnection();
						return j.georadius(key, longitude, latitude, radius,
								unit,param);
					}
				});
	}

	
	public List<GeoRadiusResponse> georadiusByMember(final String key,final String member,
			final double radius,final GeoUnit unit) {
		return redisTemplate
				.execute(new RedisCallback<List<GeoRadiusResponse>>() {
					
					public List<GeoRadiusResponse> doInRedis(
							RedisConnection connection)
							throws DataAccessException {
						Jedis j = (Jedis) connection.getNativeConnection();
						return j.georadiusByMember(key, member, radius,unit);
					}
				});
	}

	
	public List<GeoRadiusResponse> georadiusByMember(final String key,final String member,
			final double radius,final GeoUnit unit,final GeoRadiusParam param) {
		return redisTemplate
				.execute(new RedisCallback<List<GeoRadiusResponse>>() {
					
					public List<GeoRadiusResponse> doInRedis(
							RedisConnection connection)
							throws DataAccessException {
						Jedis j = (Jedis) connection.getNativeConnection();
						return j.georadiusByMember(key, member, radius,unit,param);
					}
				});
	}

	/**
	 * 取得本地�?edis对象
	 */
	public Jedis getJedis() {

		return (Jedis) redisTemplate.getConnectionFactory().getConnection()
				.getNativeConnection();
	}

}
