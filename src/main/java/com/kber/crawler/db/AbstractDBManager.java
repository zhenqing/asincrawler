package com.kber.crawler.db;

import com.kber.commons.model.PrimaryKey;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractDBManager implements com.kber.commons.DBManager {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected Dao dao;
    /**
     * 初始化数据源，准备好连接池
     */
    public abstract void initDataSource() throws Exception;

    public Dao getDao() {
        return dao;
    }

    public <T extends PrimaryKey> T save(T t, Class<T> clazz) {
        try {
            T exist = this.readById(t.getPK(), clazz);
            if (exist != null) {
                logger.debug("记录{}已经存在，无需再次写入", t.toString());
                return exist;
            }

            return dao.fastInsert(t);
        } catch (Exception e) {
            logger.error("先根据主键{}读取是否存在，随后插入记录{}到{}对应的表中失败:", t.getPK(), t.toString(), clazz, e);
            return t;
        }
    }

    public <T extends PrimaryKey> void update(T t, Class<T> clazz) {
        try {
            T exist = this.readById(t.getPK(), clazz);
            if (exist == null) {
                logger.debug("记录{}尚不存在，需直接插入数据库", t.getPK());
                dao.fastInsert(t);
            } else {
                dao.update(t);
            }
        } catch (Exception e) {
            logger.error("先根据主键{}读取是否存在，随后更新记录{}到{}对应的表中失败:", t.getPK(), t.toString(), clazz, e);
        }
    }

    public <T extends PrimaryKey> void updateQuietly(T t) {
        try {
            dao.update(t);
        } catch (Exception e) {
            logger.error("更新记录{}到{}对应数据表中时出现异常:", t.getPK(), t.getClass().getSimpleName(), e);
        }
    }

    public <T extends PrimaryKey> void update(List<T> list, Class<T> clazz) {
        for (T t : list) {
            this.update(t, clazz);
        }
    }

    public <T extends PrimaryKey> void save(List<T> list, Class<T> clazz) {
        for (T t : list) {
            this.save(t, clazz);
        }
    }

    public synchronized <T extends PrimaryKey> void fastInsert(Collection<T> collection) {
        this.dao.fastInsert(collection);
    }

    public <T> T readById(String pkId, Class<T> clazz) {
        return dao.fetch(clazz, pkId);
    }

    public <T> List<T> readAll(Class<T> clazz) {
        return dao.query(clazz, null);
    }

    public <T> int clearAll(Class<T> clazz) {
        return dao.clear(clazz);
    }

    public <T extends PrimaryKey> Map<String, T> initCache(Class<T> clazz) {
        List<T> list = readAll(clazz);
        Map<String, T> map = new HashMap<>();
        for (T t : list) {
            map.put(t.getPK(), t);
        }
        return map;
    }

    public void execute(String statement) {
        Sql sql = Sqls.create(statement);
        dao.execute(sql);
    }

    public <T> List<T> query(Class<T> clazz, Condition cond) {
        return dao.query(clazz, cond);
    }

    public <T> boolean exist(Class<T> clazz) {
        return dao.exists(clazz);
    }

    public <T> int count(Class<T> clazz, Condition cond) {
        return dao.count(clazz, cond);
    }

    public boolean exist(String tableName) {
        return dao.exists(tableName);
    }

    public <T> void deleteById(String pk, Class<T> clazz) {
        dao.delete(clazz, pk);
    }
}