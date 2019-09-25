package com.up72.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 数组线程池
 */
public class ArrayThreadPool<T> {

    private List<Future<T>> futureList = new CopyOnWriteArrayList<>();
    ExecutorService executor = Executors.newCachedThreadPool();

    public ArrayThreadPool submit(Callable<T> task) {
        Future<T> result = executor.submit(task);
        futureList.add(result);
        return this;
    }

    public List<T> get() {
        List<T> result = new LinkedList<>();
        for (Future<T> future : futureList) {
            try {
                result.add(future.get());
            } catch (Exception e) {
                result.add(null);
            }
        }
        return result;
    }

    public void stop() {
        executor.shutdown();
    }
}
