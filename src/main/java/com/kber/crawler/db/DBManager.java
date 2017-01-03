package com.kber.crawler.db;



import com.kber.commons.model.PrimaryKey;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据库管理接口
 * @author <a href="mailto:nathanael4ever@gmail.com">Nathanael Yang</a> Dec 16, 2014 7:57:45 AM
 */
public interface DBManager {
    /**
     * 获取{@link Dao}，用于某些需要执行复杂Sql的场景
     */
    Dao getDao();

    /**
     * 保存一条记录到数据库
     * @param t		entity
     * @param clazz	entity对应的Class
     */
    <T extends PrimaryKey> T save(T t, Class<T> clazz);

    /**
     * Update an existing entry in database, however, in case it doesn't exist yet, insert it
     * @param t			entity instance
     * @param clazz		clazz of current entity
     */
    <T extends PrimaryKey> void update(T t, Class<T> clazz);

    /**
     * Update a single, existing entry in database without check, thus when the entry doesn't exist, exception will be thrown out directly
     * Warning: This method update 'quietly', which means it will eat exception silently as nothing happened. Keep this in mind when you call it.
     * @param t			entity instance
     */
    <T extends PrimaryKey> void updateQuietly(T t);

    /**
     * @see #update(PrimaryKey, Class)
     */
    <T extends PrimaryKey> void update(List<T> list, Class<T> clazz);

    /**
     * 保存多条记录到数据库，对每条进行逐一处理：先查询其是否存在，如不存在才写入
     * @param list	entity集合
     * @param clazz	entity对应的Class
     */
    <T extends PrimaryKey> void save(List<T> list, Class<T> clazz);

    /**
     * 快速插入多条记录到数据库，基于批处理的方式，因此只要其中有一条不满足写入条件，整个写入会终止
     * @param collection	entity集合
     */
    <T extends PrimaryKey> void fastInsert(Collection<T> collection);

    /**
     * 根据主键id读取数据库中对应记录
     * @param pkId		主键值
     * @param clazz		entity对应的Class
     */
    <T> T readById(String pkId, Class<T> clazz);

    /**
     * 读取一张表所有记录到对应的实体集合
     * @param clazz	entity对应的Class
     * @return	实体集合
     */
    <T> List<T> readAll(Class<T> clazz);

    /**
     * 根据给定的查询条件，读取表中所有记录到对应的实体集合
     * @param clazz	entity对应的Class
     * @param cond	查询约束条件
     * @return	实体集合
     */
    <T> List<T> query(Class<T> clazz, Condition cond);

    /**
     * 清空一张表中所有记录
     * @param clazz	entity对应的Class
     */
    <T> int clearAll(Class<T> clazz);

    /**
     * 将一张表全部读取、载入缓存
     * @param clazz	entity对应的Class
     * @return	缓存，主键为key，实体为value
     */
    <T extends PrimaryKey> Map<String, T> initCache(Class<T> clazz);

    <T> boolean exist(Class<T> clazz);

    boolean exist(String tableName);

    void execute(String statement);

    <T> int count(Class<T> clazz, Condition cond);

    /**
     * Delete an entry in database table by primary key
     */
    <T> void deleteById(String pk, Class<T> clazz);
}
