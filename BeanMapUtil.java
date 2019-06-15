package com.up72.util;

import com.up72.sjfeng.model.Enterprise;
import com.up72.sjfeng.model.bank.EnterpriseBankApplyRegistInfo;
import org.apache.commons.beanutils.BeanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 类与Map转换工具类
 *
 * @ClassName BeanMapUtil
 * @Description 类与Map转换工具类
 * @Author 周录鹏
 * @Date 2019/1/15 15:58
 * @Version 1.0
 **/
public class BeanMapUtil {

    /**
     * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!"class".equals(key)) {

                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * map转换java类
     * @param map
     * @param beanClass
     * @return
     */
    public static Object transMap2Object(Map<String, Object> map, Class<?> beanClass) {
        try {
            if (map == null) {
                return null;
            }
            Object obj = beanClass.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                Method setter = property.getWriteMethod();
                if (setter != null) {
                    setter.invoke(obj, map.get(property.getName()));
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object transMap2Object2(Map<String, Object> map, Class<?> beanClass) {
        try {
            if (map == null) {
                return null;
            }
            Object obj = beanClass.newInstance();
            BeanUtils.populate(obj, map);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        EnterpriseBankApplyRegistInfo obj = new EnterpriseBankApplyRegistInfo();
        obj.setOpenBank("北京银行");
        obj.setIdType("J");
        obj.setCustName("哈哈");
        obj.setAcctNo("21123132");
        Map<String, Object> map = transBean2Map(obj);
        System.out.println(map);
        EnterpriseBankApplyRegistInfo obj2 = (EnterpriseBankApplyRegistInfo) transMap2Object(map, EnterpriseBankApplyRegistInfo.class);
        obj2.setPlatNo("joirjoeiwrjoiewrjeiw");
        obj2.setTransId("r3ur903ur902ru923ur932ur");
        System.out.println(transBean2Map(obj2));
    }
}
