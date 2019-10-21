package com.iotlab.integrityarchives.service.impl;


import com.iotlab.integrityarchives.common.service.impl.BaseServiceImpl;
import com.iotlab.integrityarchives.dao.UserInfoDao;
import com.iotlab.integrityarchives.dto.UserInfoResult;
import com.iotlab.integrityarchives.entity.CleanArchive;
import com.iotlab.integrityarchives.entity.PersonDecla;
import com.iotlab.integrityarchives.entity.UserFamily;
import com.iotlab.integrityarchives.entity.UserInfo;
import com.iotlab.integrityarchives.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@SuppressWarnings("all")
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;


    @Override
    @Transactional
    public void save(UserInfo userInfo) {
        try {
            //passwordHelper.encryptPassword(user); //加密
            userInfo.setCreateTime(new Date());
            userInfo.setLastEditTime(userInfo.getCreateTime());
            userInfoDao.insert(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            // throw new GlobalException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void update(UserInfo userInfo) {
        if (userInfo.getId() != 0) {
            try {
                userInfo.setLastEditTime(new Date());
                this.updateNotNull(userInfo);
            } catch (Exception e) {
                e.printStackTrace();
                //throw new GlobalException(e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        if (!ids.isEmpty()) {
            try {
                //通用mapper自带的批量删除,通过主键来删除  userId对应实体中的属性
                this.batchDelete(ids, "userId", UserInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
                // throw new GlobalException(e.getMessage());
            }
        }
    }


    @Override
    public List<UserInfo> findListByWord(String word) {
        return userInfoDao.findListByWord(word);
    }

    @Override
    public UserInfo ExportUserInfo(Integer userId) {
        return userInfoDao.findUserInfoByuserId(userId);
    }

    @Override
    public UserInfoResult ExportUserInfoResult(Integer userId) {
        return userInfoDao.PrintUserInfo(userId);
    }


    /**
     * word文件导出
     */
    public Map<String, Object> exportUserInfoToWordFile(UserInfo userInfo) {

        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<UserFamily> userFamilyList = userInfo.getUserFamilyList();
        dataMap.put("name", userInfo.getName());
        dataMap.put("gender", userInfo.getGender());
        dataMap.put("dateOfBirth", userInfo.getDateOfBirth());
        dataMap.put("nation", userInfo.getNation());
        dataMap.put("nativePlace", userInfo.getNativePlace());
        dataMap.put("chushengdi", userInfo.getPlaceOfBirth());
        dataMap.put("rudangdate", userInfo.getDateOfJoinParty());
        dataMap.put("canjiadate", userInfo.getDateOfJoinWork());
        dataMap.put("zhuanchang", userInfo.getFamiliarMajorAndSpecialty());
        dataMap.put("jiangcheng", userInfo.getRewardsAndPunishment());
        dataMap.put("niandukaohe", userInfo.getAnnualAssessmentResults());
        dataMap.put("zhuanye", userInfo.getTechnicalPosition());
        dataMap.put("jiankang", userInfo.getPhysicalCondition());
        dataMap.put("quanrizhi", userInfo.getFullTimeDegree());
        dataMap.put("yuanxiao", userInfo.getFullTimeGraduatedUniversityAndMajor());
        dataMap.put("zaizhi", userInfo.getPartTimeDegree());
        dataMap.put("biyeyuanxiao", userInfo.getPartTimeGraduatedUniversityAndMajor());
        dataMap.put("xianren", userInfo.getCurrentPosition());
        dataMap.put("jianli", userInfo.getResume());

        List<Map<String, Object>> listInfo = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        for (UserFamily userFamily : userFamilyList) {
            map.put("chengwei", userFamily.getAppellation());
            map.put("name", userFamily.getUserFamilyName());
            map.put("age", userFamily.getAge());
            map.put("zhengzhi", userFamily.getPoliticsStatus());
            map.put("zhiwu", userFamily.getWorkUnitAndPosition());
            listInfo.add(map);
        }
        dataMap.put("listInfo", listInfo);


        return dataMap;
    }

    /**
     * word文件导出
     */
    public Map<String, Object> exportUserInfoResultToWordFile(UserInfoResult userInfoResult) {
        CleanArchive cleanArchive = userInfoResult.getCleanArchive();
        PersonDecla personDecla = userInfoResult.getPersonDecla();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("name", userInfoResult.getName());
        dataMap.put("gender", userInfoResult.getGender());
        dataMap.put("dateOfBirth", userInfoResult.getDateOfBirth());
        dataMap.put("nation", userInfoResult.getNation());
        dataMap.put("nativePlace", userInfoResult.getNativePlace());
        dataMap.put("chushengdi", userInfoResult.getPlaceOfBirth());
        dataMap.put("rudangdate", userInfoResult.getDateOfJoinParty());
        dataMap.put("canjiadate", userInfoResult.getDateOfJoinWork());
        dataMap.put("jiankang", userInfoResult.getPhysicalCondition());
        dataMap.put("quanrizhi", userInfoResult.getFullTimeDegree());
        dataMap.put("zhuanye", userInfoResult.getTechnicalPosition());
        dataMap.put("xianren", userInfoResult.getCurrentPosition());
        dataMap.put("renmian", personDecla.getRenmian());
        dataMap.put("renshi", personDecla.getRenshi());
        dataMap.put("yinbu", personDecla.getYinbu());
        dataMap.put("xunshi", personDecla.getXunshi());
        dataMap.put("kaizhan", personDecla.getKaizhan());
        dataMap.put("dangfeng", personDecla.getDangfeng());
        dataMap.put("shoushou", cleanArchive.getShoushou());
        dataMap.put("geren", cleanArchive.getGeren());
        dataMap.put("peiou", cleanArchive.getPeiou());
        dataMap.put("zaiqi", cleanArchive.getShifou());
        dataMap.put("shifou", cleanArchive.getShifou());
        dataMap.put("niandu", cleanArchive.getNiandu());
        dataMap.put("yinsi", cleanArchive.getNiandu());
        return dataMap;
    }


}