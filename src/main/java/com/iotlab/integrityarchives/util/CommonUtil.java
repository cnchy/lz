package com.iotlab.integrityarchives.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CommonUtil {

    public static Object checkNull(Object obj) {
        Class<? extends Object> clazz = obj.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 可访问私有变量
            field.setAccessible(true);
            // 获取属性类型
            String type = field.getGenericType().toString();
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if ("class java.lang.String".equals(type)) {
                // 将属性的首字母大写
                String methodName = field.getName().replaceFirst(field.getName().substring(0, 1),
                        field.getName().substring(0, 1).toUpperCase());
                try {
                    Method methodGet = clazz.getMethod("get" + methodName);
                    // 调用getter方法获取属性值
                    String str = (String) methodGet.invoke(obj);
                    if (StringUtils.isBlank(str) || str.equals("null")) {
                        // 如果为null的String类型的属性则重新复制为空字符串
                        field.set(obj, field.getType().getConstructor(field.getType()).newInstance(""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

}
