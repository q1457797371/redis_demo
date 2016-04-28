package com.redis.service;


import java.util.ArrayList;  
import java.util.Collections;  
import java.util.Comparator;  
import java.util.Date;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.LinkedHashMap;  
import java.util.LinkedList;  
import java.util.List;  
import java.util.Map;  
import java.util.Set;  
  
import org.springframework.context.ApplicationContext;  
import org.springframework.context.support.ClassPathXmlApplicationContext;  
import org.springframework.dao.DataAccessException;  
import org.springframework.data.redis.core.HashOperations;  
import org.springframework.data.redis.core.RedisOperations;  
import org.springframework.data.redis.core.RedisTemplate;  
import org.springframework.data.redis.core.SessionCallback;  
  
public class UserDateSerivce {  
  
    private RedisTemplate<String, Date> redisTemplate;  
    HashOperations<String, String, Date> ho;  
  
    public UserDateSerivce() {  
        /** 
         * 加载配置文件 
         */  
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext-redis.xml");  
        redisTemplate = (RedisTemplate) ac.getBean("redisTemplate");  
    }  
  
    /** 
     * 用户喜欢一个商品 
     *  
     * @param uId 
     * @param pId 
     */  
    public void addLove(String uId, String pId) {  
        // 用户喜欢的商品增加一个  
        final String uKey = getUKey(uId);  
        final String uHashKey = pId + "";  
        final String pKey = getPKey(pId);  
        final String pHashKey = uId + "";  
        final Date date = new Date();  
        SessionCallback<Object> call = new SessionCallback<Object>() {  
  
            public Object execute(RedisOperations ops) throws DataAccessException {  
                // TODO Auto-generated method stub  
                ops.multi();  
                ho = ops.opsForHash();  
                ho.put(uKey, uHashKey, date);  
                ho.put(pKey, pHashKey, date);  
                ops.exec();  
                return null;  
            }  
        };  
        redisTemplate.execute(call);  
        // 用户喜欢数+1  
  
    }  
  
    /** 
     * 取消喜欢 
     *  
     * @param uId 
     * @param pId 
     */  
    public void cannelLove(String uId, String pId) {  
        final String uKey = getUKey(uId);  
        final String pIdStr = pId;  
        final String pKey = getPKey(pId);  
        final String uIdStr = uId;  
        SessionCallback<Object> call = new SessionCallback<Object>() {  
  
            public Object execute(RedisOperations ops) throws DataAccessException {  
                // TODO Auto-generated method stub  
                ops.multi();  
                ho = ops.opsForHash();  
                ho.delete(uKey, pIdStr);  
                ho.delete(pKey, uIdStr);  
                ops.exec();  
                return null;  
            }  
        };  
        redisTemplate.execute(call);  
    }  
  
    /** 
     * 此用户是否喜欢此商品 
     *  
     * @param uId 
     * @param pId 
     */  
    public boolean isLoved(String uId, String pId) {  
        boolean re = false;  
        final String uKey = getUKey(uId);  
        final String uHashKey = pId + "";  
        ho = redisTemplate.opsForHash();  
        System.out.println(uKey + "  ,   " + uHashKey);  
        re = ho.hasKey(uKey, uHashKey);  
        return re;  
    }  
  
    /** 
     * 用户喜欢商品的数两 
     *  
     * @return 
     */  
    public Long getLoveCountByUid(String uId) {  
        final String uKey = getUKey(uId);  
        long re = 0;  
        ho = redisTemplate.opsForHash();  
        re = ho.size(uKey);  
        return re;  
    }  
  
    /** 
     * 商品的粉丝数 
     *  
     * @return 
     */  
    public Long getLoveCountByPid(String pId) {  
        final String pKey = getPKey(pId);  
        long re = 0;  
        ho = redisTemplate.opsForHash();  
        re = ho.size(pKey);  
        return re;  
    }  
  
    /** 
     * 删除商品 
     */  
  
    public void delP(String pId) {  
        // 删除商品的粉丝集合  
  
        // 删除用户关注集合中的此商品  
  
        final String pKey = getPKey(pId);  
        final String pIdStr = pId + "";  
        final Set<String> keys = redisTemplate.keys("uId:*:p");  
        SessionCallback<Object> call = new SessionCallback<Object>() {  
  
            public Object execute(RedisOperations ops) throws DataAccessException {  
                // TODO Auto-generated method stub  
                ho = ops.opsForHash();  
                ops.delete(pKey);  
                if (keys != null && keys.size() > 0) {  
                    for (String key : keys) {  
                        if (ho.hasKey(key, pIdStr)) {//如果此用户关注了该商品，则从关注列表中删除，这个操作不能放到multi中，  
                            //因为multi中的操作都是暂时放到命令队列中，到执行exec时统一提交，ho.hashKey()会返回null；  
                            System.out.println(pIdStr);  
                            ho.delete(key, pIdStr);  
                        }  
                    }  
                }  
                return null;  
            }  
        };  
  
        redisTemplate.execute(call);  
    }  
  
    /** 
     * 获取商品的粉丝列表 
     *  
     * @param pId 
     * @param count 
     * @return 
     */  
    public Map<String, Date> getUidsByPid(String pId, int count) {  
        Map<String, Date> map = null;  
        final String pKey = getPKey(pId);  
        SessionCallback<Map<String, Date>> call = new SessionCallback<Map<String, Date>>() {  
  
            public Map<String, Date> execute(RedisOperations ops) throws DataAccessException {  
                // TODO Auto-generated method stub  
                ops.multi();  
                ho = ops.opsForHash();  
                ops.exec();  
                return (Map<String, Date>) ho.entries(pKey);  
            }  
        };  
        map = redisTemplate.execute(call);  
        return map;  
    }  
  
    /** 
     * 获取用户的喜欢列表 
     *  
     * @param uId 
     * @return 
     */  
    private Map<String, Date> getPidsByUid(String uId) {  
        Map<String, Date> map = null;  
        final String uKey = getUKey(uId);  
        SessionCallback<Map<String, Date>> call = new SessionCallback<Map<String, Date>>() {  
  
            public Map<String, Date> execute(RedisOperations ops) throws DataAccessException {  
                // TODO Auto-generated method stub  
                ops.multi();  
                ho = ops.opsForHash();  
                ops.exec();  
                return (Map<String, Date>) ho.entries(uKey);  
            }  
        };  
        map = redisTemplate.execute(call);  
        return map;  
    }  
  
    /** 
     * 获取用户关注的商品ids 
     *  
     * @param uId 
     * @return 
     */  
    public List<String> getFocusPids(String uId) {  
        return sortIds(this.getPidsByUid(uId));  
    }  
  
    /** 
     * 获取商品的粉丝IDS 
     *  
     * @param pId 
     * @return 
     */  
    public List<String> getFansUids(String pId) {  
        return sortIds(this.getUidsByPid(pId, 0));  
    }  
  
    public static String getUKey(String uId) {  
        return "uId:" + uId + ":p";  
    }  
  
    public static String getUCountKey(String uId) {  
        return "uId:" + uId + ":pCount";  
    }  
  
    public static String getPKey(String pId) {  
        return "pId:" + pId + ":u";  
    }  
  
    public static String getPCountKey(String pId) {  
        return "pId:" + pId + ":uCount";  
    }  
  
    /** 
     * 按时间进行排序 
     *  
     * @param map 
     * @return 
     */  
    public List<String> sortIds(Map<String, Date> map) {  
        List<Map.Entry<String, Date>> list = new LinkedList<Map.Entry<String, Date>>();  
        list.addAll(map.entrySet());  
        Collections.sort(list, new Comparator<Map.Entry<String, Date>>() {  
            public int compare(Map.Entry<String, Date> date1, Map.Entry<String, Date> date2) {// 从高往低排序  
  
                if (date1.getValue().getTime() < date2.getValue().getTime())  
                    return 1;  
                if (date1.getValue().getTime() == date2.getValue().getTime())  
                    return 0;  
                else  
                    return -1;  
            }  
        });  
        List<String> ids = new ArrayList<String>();  
        for (Iterator<Map.Entry<String, Date>> ite = list.iterator(); ite.hasNext();) {  
            Map.Entry<String, Date> map1 = ite.next();  
            System.out.println("key-value: " + map1.getKey() + "," + map1.getValue());  
            ids.add(map1.getKey());  
        }  
        return ids;  
    }  
  
    /** 
     * 批量查询商品的粉丝 
     *  
     * @param pids 
     * @return key=pid value=fansCount 
     */  
    public Map<String, Long> getFansCountByPids(List<String> pids) {  
        Map<String, Long> map = new HashMap<String, Long>();  
  
        ho = redisTemplate.opsForHash();  
        for (String pId : pids) {  
            String pidKey = this.getPKey(pId);  
            Long size = ho.size(pidKey);  
            map.put(pId, size);  
        }  
        return map;  
    }  
    /** 
     * 获取粉丝数最多的count个商品id 
     * @param count 
     * @return 
     */  
    public Map<String,Long> getHot(int count)  
    {  
        Map<String,Long>map=new HashMap<String,Long>();  
        ho=redisTemplate.opsForHash();  
        Set<String> keys=redisTemplate.keys("pId:*:u");  
        if(keys!=null)  
        {  
            for(String key:keys)  
            {  
                map.put(key, ho.size(key));  
            }  
        }  
        return sortByCount(map,count);  
    }  
      
    public Map<String,Long>  sortByCount(Map<String,Long> map,int count)  
    {  
        Map<String,Long> re=new LinkedHashMap<String, Long>();  
        List<Map.Entry<String, Long>> list = new ArrayList<Map.Entry<String, Long>>();  
        list.addAll(map.entrySet());  
        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {  
            public int compare(Map.Entry<String, Long> date1, Map.Entry<String, Long> date2) {// 从高往低排序  
  
                if (date1.getValue()< date2.getValue())  
                    return 1;  
                if (date1.getValue() == date2.getValue())  
                    return 0;  
                else  
                    return -1;  
            }  
        });  
        for (Iterator<Map.Entry<String, Long>> ite = list.iterator(); ite.hasNext();) {  
            Map.Entry<String, Long> map1 = ite.next();  
            System.out.println("key-value: " + map1.getKey() + "," + map1.getValue());  
            re.put(map1.getKey() , map1.getValue());  
            if(re.size()==count)  
            {  
                return re;  
            }  
        }  
        return re;  
    }  
    public static void main(String[] args) {  
        UserDateSerivce us = new UserDateSerivce();  
  
         testAdds(us);  
  
        // System.out.println(us.getLoveCountByPid(1));  
        // System.out.println(us.getLoveCountByUid(3));  
        // testCannelFocus(us);  
        // testDel(us, uId, pId, list1, list2);  
        // testGetFansCountByPids(us);  
        //us.testMulti();  
        Map<String, Long> map=us.getHot(2);  
        System.out.println(map);  
    }  
  
    /** 
     * 测试取消关注 
     *  
     * @param us 
     */  
    private static void testCannelFocus(UserDateSerivce us) {  
        String uId = "0";  
        String pId = "0";  
        List<String> list1 = us.getFocusPids(uId);  
        System.out.println("用户" + uId + " 的关注有" + list1);  
        System.out.println("用户" + uId + "取消对商品" + pId + "的关注");  
        us.cannelLove(uId, pId);  
        List<String> list2 = us.getFocusPids(uId);  
        System.out.println("用户" + uId + " 的关注有" + list2);  
        System.out.println(us.getLoveCountByPid("0"));  
    }  
  
    /** 
     * 测试删除商品 
     *  
     * @param us 
     * @param uId 
     * @param pId 
     * @param list1 
     * @param list2 
     */  
    private static void testDel(UserDateSerivce us, String uId, String pId, List<String> list1, List<String> list2) {  
        System.out.println("商品" + pId + " 的粉丝有" + list1);  
        System.out.println("用户" + uId + " 的关注有" + list2);  
        System.out.println("现在删除商品" + pId);  
        us.delP(pId);  
        System.out.println("删完之后");  
        List<String> list3 = us.getFansUids(pId);  
        List<String> list4 = us.getFocusPids(uId);  
        System.out.println("商品" + pId + " 的粉丝有" + list3);  
        System.out.println("用户" + uId + " 的关注有" + list4);  
    }  
  
    /** 
     * 测试添加商品 
     *  
     * @param us 
     */  
    private static void testAdds(UserDateSerivce us) {  
        us.addLove("1", "1");  
        us.addLove("2", "1");  
        us.addLove("7", "3");  
        us.addLove("8", "4");  
        us.addLove("9", "4");  
        us.addLove("1", "2");  
        us.addLove("5", "3");  
        us.addLove("4", "1");  
    }  
  
    /** 
     * 测试批量获取商品的粉丝数 
     *  
     * @param us 
     */  
    public static void testGetFansCountByPids(UserDateSerivce us) {  
        List<String> pids = new ArrayList<String>();  
        pids.add("1");  
        pids.add("2");  
        pids.add("3");  
        pids.add("4");  
        Map<String, Long> map = us.getFansCountByPids(pids);  
        System.out.println(map);  
    }  
  
    public void testMulti() {  
        redisTemplate.watch("a");  
        redisTemplate.multi();  
        redisTemplate.exec();  
        redisTemplate.unwatch();  
    }  
}  