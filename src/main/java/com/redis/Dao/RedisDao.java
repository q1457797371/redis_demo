package com.redis.Dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.geo.GeoRadiusParam;

/**
 * redis操作dao
 *
 * @author zxl0047 2015-10-16
 */
public interface RedisDao<K, V> extends ValueOperations<K, V>, HashOperations<K, K, V> {
	/**
	 * 删除key
	 * 
	 * @param key
	 */
	public void delete(K key);

	/**
	 * 获取给定字符串所能匹配到的所有键
	 * 
	 * @param keys
	 * @return
	 */
	public Set<K> keysAll(K keys);

	Boolean hasKey(K key);

	void delete(Collection<K> key);

	DataType type(K key);

	K randomKey();

	void rename(K oldKey, K newKey);

	Boolean renameIfAbsent(K oldKey, K newKey);

	Boolean expire(K key, long timeout, TimeUnit unit);

	Boolean expireAt(K key, Date date);

	Boolean persist(K key);

	Boolean move(K key, int dbIndex);

	byte[] dump(K key);

	void restore(K key, byte[] value, long timeToLive, TimeUnit unit);

	Long getExpire(K key);

	Long getExpire(K key, TimeUnit timeUnit);

	void watch(K keys);

	void watch(Collection<K> keys);

	void unwatch();

	// geo

	public Long geoadd(String key, double longitude, double latitude, String member);

	public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap);

	public Double geodist(String key, String member1, String member2);

	public Double geodist(String key, String member1, String member2, GeoUnit unit);

	public List<String> geohash(String key, String... members);

	public List<GeoCoordinate> geopos(String key, String... members);

	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius,
			GeoUnit unit);

	public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit,
			GeoRadiusParam param);

	public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit);

	public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit,
			GeoRadiusParam param);

	public Jedis getJedis();
}
