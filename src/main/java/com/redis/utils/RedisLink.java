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
 * redis�Զ����ӹ��з���
 * @author wjj0065
 *
 */
public class RedisLink {
	/**
	 * ����Ƭ��ͻ�������
	 */
	public static Jedis jedis;
    /**
     * ����Ƭ���ӳ�
     */
	public static JedisPool jedisPool;
    /**
     * ��Ƭ��ͻ�������
     */
	public static ShardedJedis shardedJedis;
    /**
     * ��Ƭ���ӳ�
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
     * ��ʼ������Ƭ��
     */
    public void initialPool() 
    { 
        // �ػ������� 
        JedisPoolConfig config = new JedisPoolConfig(); 
        //config.setMaxActive(20); 2.1.0�汾
        config.setMaxIdle(5); 
        //config.setMaxWait(1000l); 
        config.setTestOnBorrow(false); 
        
        jedisPool = new JedisPool(config,"192.168.200.153",6379);
    }
    
    /** 
     * ��ʼ����Ƭ�� 
     */ 
    public void initialShardedPool() 
    { 
        // �ػ������� 
        JedisPoolConfig config = new JedisPoolConfig(); 
        //config.setMaxActive(20); 
        config.setMaxIdle(5); 
        //config.setMaxWait(1000l); 
        config.setTestOnBorrow(false); 
        // slave���� 
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
        shards.add(new JedisShardInfo("192.168.200.153", 6379, "db0")); 

        // ����� 
        shardedJedisPool = new ShardedJedisPool(config, shards); 
    } 

    public void close() {  
        jedisPool.returnResource(jedis);
        shardedJedisPool.returnResource(shardedJedis);
    } 
}