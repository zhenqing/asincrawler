package com.kber.crawler.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 多线程辅助类
 *
 * @author <a href="mailto:nathanael4ever@gmail.com">Nathanael Yang</a> Jan 26, 2015 9:18:42 AM
 */
public class ThreadHelper {

    /**
     * 将待处理的集合按照线程总数分解
     *
     * @param list        待处理集合
     * @param threadCount 线程总数
     */
    public static <T> List<List<T>> assign(List<T> list, int threadCount) {
        if (list == null || threadCount <= 0) {
            throw new IllegalArgumentException("You must provide a valid collection and thread count number greater than zero.");
        }

        int size = list.size();
        int step = size / threadCount;
        step = step > 0 ? step : 1;

        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            int from = i * step;
            if (from >= size) {
                result.add(new ArrayList<T>(0));
                continue;
            }

            int to = (i + 1) * step;
            if (i == threadCount - 1 && to < size) {
                to = size;
            }
            result.add(list.subList(from, to));
        }

        return result;
    }

}
