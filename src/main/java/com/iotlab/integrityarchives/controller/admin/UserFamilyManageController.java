
package com.iotlab.integrityarchives.controller.admin;

import com.iotlab.integrityarchives.common.controller.BaseController;
import com.iotlab.integrityarchives.dto.ResponseCode;
import com.iotlab.integrityarchives.entity.UserFamily;
import com.iotlab.integrityarchives.service.UserFamilyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@SuppressWarnings("all")
@RequestMapping("/manage/userFamily")
@Api(tags = "用户家人信息控制API", value = "测试")
public class UserFamilyManageController extends BaseController {

    @Autowired
    private UserFamilyService userFamilyService;

    @GetMapping(value = "/findByUserId")
    public ResponseCode findByUserId(@RequestParam("userId") Integer userId) {
        return ResponseCode.success(userFamilyService.findByUserId(userId));
    }

    @GetMapping(value = "/findById")
    public ResponseCode findById(@RequestParam("id") Integer id) {
        System.out.println(userFamilyService.findById(id).getUserFamilyName());
        return ResponseCode.success(userFamilyService.findById(id));
    }

    @PostMapping("/update")
    public ResponseCode update(@RequestBody UserFamily userFamily) {
        try {
            userFamily.setLastEditTime(new Date());
            userFamilyService.update(userFamily);
            return ResponseCode.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/save")
    public ResponseCode save(@RequestBody UserFamily userFamily) {
        try {
            userFamily.setCreateTime(new Date());
            userFamily.setLastEditTime(userFamily.getCreateTime());
            userFamilyService.save(userFamily);
            return ResponseCode.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/delete")
    public ResponseCode delete(@RequestBody List<Long> ids) {
        try {
            userFamilyService.delete(ids);
            return ResponseCode.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
