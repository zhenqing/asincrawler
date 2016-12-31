package com.kber.crawler.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 用户自定义配置枚举
 *
 * @author <a href="mailto:nathanael4ever@gmail.com">Nathanael Yang</a> Nov 19, 2014 8:52:06 AM
 */
public class Customize {
    private final String fileName;
    private final Class<?> clazz;
    private final boolean listMode;

    public boolean listMode() {
        return listMode;
    }

    /**
     * 获取当前自定义配置对应的文件
     */
    public File file() {
        return new File(fileName);
    }

    public String fileName() {
        return this.fileName;
    }

    /**
     * 判定当前自定义配置是否已经完成: 配置文件存在
     */
    public boolean exist() {
        return this.file().exists();
    }

    /**
     * 获取当前自定义配置对应的{@link Class}
     */
    public Class<?> clazz() {
        return clazz;
    }

    public Customize(String fileName, Class<?> clazz) {
        this(fileName, clazz, false);
    }

    public Customize(String fileName, Class<?> clazz, boolean listMode) {
        this.fileName = fileName;
        this.clazz = clazz;
        this.listMode = listMode;
    }
}
