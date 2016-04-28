package com.redis.controller;

import java.util.Set;

import com.redis.utils.RedisLink;

/**
 * 模拟用户收藏商品。
 * 然后统计商品的收藏数。并且排序。什么商品收藏的人最多
 * 统计某一个人收藏了那些商品
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
		System.out.println("redis操作完毕");
	}
    /**
     * 会员商品收藏统计
     */
    private static void avgPersonCollectNumCount() {
    	Set<String> keySets=jedis.keys("userId:*");
    	for (String key : keySets) {
    		System.out.println(jedis.type(key));
    		Long cNum=jedis.hlen(key);
			System.out.println("会员编号："+key+"==========;收藏数："+cNum);
		}
	}
	/**
     * 模拟用户收藏某一个商品
     * @param userId
     * @param goodsId
     */
	private static void personCollect(int userId, int goodsId) {
		shardedJedis.hset("userId:"+userId,"goodsId_"+goodsId , "0");
	}
}
