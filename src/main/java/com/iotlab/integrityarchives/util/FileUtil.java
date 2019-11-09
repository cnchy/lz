package com.iotlab.integrityarchives.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;

public class FileUtil {

    public static void writeToFile(String path, Object object) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        if (object instanceof JSONObject) {
            fileWriter.write(JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat));
        } else {
            fileWriter.write(object.toString());
        }
        fileWriter.close();
    }

    public static JSONObject readJSONFile(String path) throws IOException {
        FileReader fileReader = new FileReader(path);;
        StringBuilder stringBuilder = new StringBuilder();
        int ch = 0;
        while((ch = fileReader.read()) != -1) {
            stringBuilder.append((char)ch);
        }
        return JSON.parseObject(stringBuilder.toString());
    }
}
