package org.fai.study.projectsem4.service.interfaces;

public interface IBaseRedisService {
    void set(String key, String value);
    void setTimeLive(String key, long time);
    void HashSet(String key, String field,Object value);
    boolean hashExists(String key, String field);
    Object get(String key);
}
