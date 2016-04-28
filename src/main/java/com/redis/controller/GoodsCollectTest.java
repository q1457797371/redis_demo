package com.redis.controller;

import java.util.Set;

import com.redis.utils.RedisLink;

/**
 * ģ���û��ղ���Ʒ��
 * Ȼ��ͳ����Ʒ���ղ�������������ʲô��Ʒ�ղص������
 * ͳ��ĳһ�����ղ�����Щ��Ʒ
 * @author wjj0065
 *
 */
public class GoodsCollectTest extends RedisLink{
    public static void main(String[] args) {
    	GoodsCollectTest a = new GoodsCollectTest();
        System.out.println(jedis.flushDB()); 
		personCollect(1,100);
		personCollect(1,103);
		personCollect(1,13303);
		personCollect(1,123);
		personCollect(1,1023);
		personCollect(3,100);
		personCollect(4,1020);
		personCollect(4,100);
		personCollect(4,100);
		personCollect(6,140);
		personCollect(6,1440);
		personCollect(6,140);
		personCollect(6,1410);
		avgPersonCollectNumCount();
		a.close();
		System.out.println("redis�������");
	}
    /**
     * ��Ա��Ʒ�ղ�ͳ��
     */
    private static void avgPersonCollectNumCount() {
    	Set<String> keySets=jedis.keys("userId:*");
    	for (String key : keySets) {
    		System.out.println(jedis.type(key));
    		Long cNum=jedis.hlen(key);
			System.out.println("��Ա��ţ�"+key+"==========;�ղ�����"+cNum);
		}
	}
	/**
     * ģ���û��ղ�ĳһ����Ʒ
     * @param userId
     * @param goodsId
     */
	private static void personCollect(int userId, int goodsId) {
		shardedJedis.hset("userId:"+userId,"goodsId_"+goodsId , "0");
	}
}
