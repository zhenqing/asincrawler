package com.kber.crawler.db;

import com.kber.crawler.utils.Constants;
import com.kber.crawler.utils.Tools;
import org.apache.commons.dbcp2.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

import javax.sql.DataSource;


public class DBUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBUtils.class);
    public static DataSource buildSqliteDataSource(String jdbcUrl) throws ClassNotFoundException {
        SQLiteConfig config = new SQLiteConfig();
        config.setJournalMode(SQLiteConfig.JournalMode.WAL);
        config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);

        return buildSqliteDataSource(jdbcUrl, config);
    }

    public static DataSource buildSqliteDataSource(String jdbcUrl, SQLiteConfig sqLiteConfig) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        ConnectionFactory connFactory = new DriverManagerConnectionFactory(jdbcUrl, sqLiteConfig.toProperties());
        PoolableConnectionFactory poolConnFactory = new PoolableConnectionFactory(connFactory, null);
        ObjectPool<PoolableConnection> connPool = new GenericObjectPool<>(poolConnFactory);
        poolConnFactory.setPool(connPool);
        return new PoolingDataSource<>(connPool);
    }

    /**
     * 创建数据库表(判定其不存在的前提下)
     */
    public static void createTables(Dao dao, String... tables) {
        long start = System.currentTimeMillis();
        for (String tableName : tables) {
            if (!dao.exists(tableName)) {
                dao.execute(dao.sqls().create(tableName));
            }
        }
        LOGGER.info("数据库表{}初始化完成。耗时:{}", StringUtils.join(tables, Constants.COMMA), Tools.formatCostTime(start));
    }
}
