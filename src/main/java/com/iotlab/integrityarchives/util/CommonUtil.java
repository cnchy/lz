package com.iotlab.integrityarchives.util;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Base64;

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

    public static String image2Base64(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();

        InputStream is = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();;

        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        try{
            Response response = call.execute();
            byte[] buf = new byte[2048];
            int len = 0;
            is = response.body().byteStream();
            while ((len = is.read(buf)) != -1) {
                outStream.write(buf, 0, len);
            }
            final Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(outStream.toByteArray());
        } catch (IOException ex) {
            return "";
        }

    }

}
