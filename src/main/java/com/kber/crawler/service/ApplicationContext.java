package com.kber.crawler.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kber.aop.ProfileModule;
import com.kber.aop.RepeatModule;
import com.kber.commons.DateModule;

/**
 * <a href="mailto:tmtlindsay@gmail.com">Lindsay Zhao</a> 12/21/2016 5:43 PM
 */
public class ApplicationContext {
    private ApplicationContext() {
    }

    private static final ApplicationContext instance = new ApplicationContext();

    public static ApplicationContext getInstance() {
        return instance;
    }
    private static final Injector injector = Guice.createInjector(
            new RepeatModule(),
            new ProfileModule(),
            new DateModule());

    /**
     * 根据类型获取被Guice管理的对象实例
     * @param type 类型
     */
    public static <T> T getBean(Class<T> type) {
        return injector.getInstance(type);
    }
}
