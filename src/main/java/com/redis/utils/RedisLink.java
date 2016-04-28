package com.redis.utils;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
/**
 * redis自动链接共有方法
 * @author wjj0065
 *
 */
public class RedisLink {
	/**
	 * 非切片额客户端连接
	 */
	public static Jedis jedis;
    /**
     * 非切片连接池
     */
	public static JedisPool jedisPool;
    /**
     * 切片额客户端连接
     */
	public static ShardedJedis shardedJedis;
    /**
     * 切片连接池
     */
	public static ShardedJedisPool shardedJedisPool;
    
    public RedisLink() 
    { 
        initialPool(); 
        initialShardedPool();
        shardedJedis = shardedJedisPool.getResource(); 
        jedis = jedisPool.getResource(); 
        
        
    } 
 
    /**
     * 初始化非切片池
     */
    public void initialPool() 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        //config.setMaxActive(20); 2.1.0版本
        config.setMaxIdle(5); 
        //config.setMaxWait(1000l); 
        config.setTestOnBorrow(false); 
        
        jedisPool = new JedisPool(config,"192.168.200.153",6379);
    }
    
    /** 
     * 初始化切片池 
     */ 
    public void initialShardedPool() 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        //config.setMaxActive(20); 
        config.setMaxIdle(5); 
        //config.setMaxWait(1000l); 
        config.setTestOnBorrow(false); 
        // slave链接 
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
        shards.add(new JedisShardInfo("192.168.200.153", 6379, "db0")); 

        // 构造池 
        shardedJedisPool = new ShardedJedisPool(config, shards); 
    } 

    public void close() {  
        jedisPool.returnResource(jedis);
        shardedJedisPool.returnResource(shardedJedis);
    } 
}