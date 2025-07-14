//package org.fai.study.projectsem4.service.Impl;
//
//import org.fai.study.projectsem4.service.interfaces.IBaseRedisService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.concurrent.TimeUnit;
//
//public class BaseRedisService implements IBaseRedisService {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    private final HashOperations<String,String,Object> hashOperations;
//    public BaseRedisService(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//        this.hashOperations = redisTemplate.opsForHash();
//    }
//
//    @Override
//    public void set(String key, String value) {
//            redisTemplate.opsForValue().set(key, value);
//    }
//
//    @Override
//    public void setTimeLive(String key, long time) {
//            redisTemplate.expire(key,time, TimeUnit.DAYS);
//    }
//
//    @Override
//    public void HashSet(String key, String field, Object value) {
//        hashOperations.put(key,field,value);
//    }
//
//    @Override
//    public boolean hashExists(String key, String field) {
//        return hashOperations.hasKey(key,field);
//    }
//
//    @Override
//    public Object get(String key) {
//        return redisTemplate.opsForValue().get(key);
//    }
//}
