package com.up72.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 集合比较类
 *
 * 比较两个集合的数据是否相同，不考虑顺序
 */
public class CollectionCompare<T> {

    public CollectionCompare() {
    }

    public CollectionCompare(List<T> list) {
        this.obj = list;
    }

    private Collection<T> obj;

    public Collection<T> getObj() {
        return obj;
    }

    public void setObj(Collection<T> obj) {
        this.obj = obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CollectionCompare that = (CollectionCompare) o;
        if (obj.size() != that.getObj().size()) {
            return false;
        }
        return obj.containsAll(that.getObj());
    }

    @Override
    public int hashCode() {
        return Objects.hash(obj);
    }
}

