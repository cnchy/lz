package com.iotlab.integrityarchives.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.iotlab.integrityarchives.util.FileUtil;
import org.apache.commons.io.output.WriterOutputStream;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PersonConsulationsManageService {

    private static final String filePath = System.getProperty("user.dir") + "/config/consulation.json";

    public void setConsulation(Date dateStart, Date dateEnd, Boolean on, String attention) throws IOException {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("dateStart", dateStart);
        jsonObject.put("dateEnd", dateEnd);
        jsonObject.put("on", on);
        jsonObject.put("attention", attention);
        FileUtil.writeToFile(filePath, jsonObject);
    }

    public JSONObject getConsulation() throws IOException {
        return FileUtil.readJSONFile(filePath);
    }

    public Boolean isOpenTime() throws IOException, ParseException {
        JSONObject jsonObject = FileUtil.readJSONFile(filePath);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateStart = simpleDateFormat.parse((String) jsonObject.get("dateStart"));
        Date dateEnd = simpleDateFormat.parse((String) jsonObject.get("dateEnd"));
        return (dateStart != null && dateEnd != null && isDateDuration(new Date(), dateStart, dateEnd)) || (Boolean) jsonObject.get("on");
    }

    public Boolean isDateDuration(Date date, Date dateStart, Date dateEnd) {
        return date.getTime() >= dateStart.getTime() && date.getTime() <= dateEnd.getTime();
    }
}
