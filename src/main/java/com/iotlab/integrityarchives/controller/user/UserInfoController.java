package com.iotlab.integrityarchives.controller.user;

import com.iotlab.integrityarchives.common.controller.BaseController;
import com.iotlab.integrityarchives.dto.ResponseCode;
import com.iotlab.integrityarchives.dto.UserInfoResult;
import com.iotlab.integrityarchives.entity.UserFamily;
import com.iotlab.integrityarchives.entity.UserInfo;
import com.iotlab.integrityarchives.service.UserInfoService;
import com.iotlab.integrityarchives.util.FilePathUtil;

import com.iotlab.integrityarchives.util.PrintUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@SuppressWarnings("all")
@RequestMapping("/user/userInfo")
@Api(tags = "干部信息控制API", value = "测试")
public class UserInfoController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping(value = "/findByUserId")
    public ResponseCode findByUserId(@RequestParam("userId") Integer userId) {
        return ResponseCode.success(userInfoService.ExportUserInfo(userId));
    }

    @PostMapping("/update")
    public ResponseCode update(@RequestBody UserInfo userInfo, @RequestParam("image") MultipartFile file) {
        try {
            userInfo.setLastEditTime(new Date());
            userInfo.setAvatar(FilePathUtil.PathUtil(file,userInfo.getName()));
            userInfoService.update(userInfo);
            return ResponseCode.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/testupload")
    public void tesetUpload(@RequestParam("fileName") MultipartFile file) {
       // System.out.println(ImageUtil.imagePath(file));
    }


    @GetMapping("/print")
    public void printUserInfo(@RequestParam("userId") Integer userId, HttpServletResponse response) {
        //Map<String, Object> dataMap = new HashMap<String, Object>();
        UserInfo userInfo = userInfoService.ExportUserInfo(userId);
        System.out.print("userInfo信息为："+userInfo);
        List<UserFamily> userFamilyList = userInfo.getUserFamilyList();
        try {
            Map<String, Object> dataMap = userInfoService.exportUserInfoToWordFile(userInfo);
           /* PrintUtil.exportMillCertificateWord(response, dataMap, "d:/", "领导干部个人廉政档案信息表.ftl", userInfo.getName());*/
            PrintUtil.exportMillCertificateWord(response, dataMap, "d:/", "干部信息表.ftl", userInfo.getName(),1);
        } catch (IOException e) {
            e.printStackTrace();
         System.out.println("遇到错误了");
        }

    }


    @GetMapping("/printResult")
    public void printUserInfoResult(@RequestParam("userId") Integer userId, HttpServletResponse response) {
        //Map<String, Object> dataMap = new HashMap<String, Object>();
        UserInfoResult userInfoResult = userInfoService.ExportUserInfoResult(userId);
        try {
            Map<String, Object> dataMap = userInfoService.exportUserInfoResultToWordFile(userInfoResult);
            PrintUtil.exportMillCertificateWord(response, dataMap, "d:/", "领导干部个人廉政档案信息表.ftl", userInfoResult.getName(),2);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("遇到错误了");
        }

    }



}