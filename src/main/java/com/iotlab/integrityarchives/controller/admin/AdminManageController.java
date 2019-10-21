package com.iotlab.integrityarchives.controller.admin;

import com.iotlab.integrityarchives.common.controller.BaseController;
import com.iotlab.integrityarchives.dto.QueryPage;
import com.iotlab.integrityarchives.dto.ResponseCode;
import com.iotlab.integrityarchives.entity.Admin;
import com.iotlab.integrityarchives.enums.EnableStatusEnum;
import com.iotlab.integrityarchives.service.AdminService;
import com.iotlab.integrityarchives.util.Md5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author created by Zhangdazhuang
 * @version v.0.1
 * @Description TODO
 * @date 2019/4/17        管理员CRUD    用户CRUD   信息CRUD
 * @备注 超级管理员  唯一    1               1        1
 * 系统管理员  多个    0               1        1
 * 用户              0               0        1
 **/
@RestController
@SuppressWarnings("all")
@RequestMapping("/manage/admin")
@Api(tags = "管理员控制API", value = "测试")
public class AdminManageController extends BaseController {

    @Autowired
    private AdminService adminService;

    /**
     * 通过id查询到指定管理员
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "通过id查询到指定管理员", notes = "返回Admin实体类")
    @GetMapping(value = "/findById")
    public ResponseCode findById(@RequestParam("id") Integer id) {
        return ResponseCode.success(adminService.findById(id));
    }

    /**
     * 查询所有的管理员
     *
     * @return
     */
    @GetMapping(value = "/findAll")
    public ResponseCode findAll() {
        System.out.println("进入访问管理员列表路径");
        return ResponseCode.success(adminService.findAll());
    }

    /**
     * 分页查询
     *
     * @param queryPage
     * @param admin
     * @return
     */
    @PostMapping(value = "/findByPage")
    public ResponseCode findByPage(QueryPage queryPage, Admin admin) {
        //System.out.println("进入到后台方法");
        //System.out.println(admin);
        return ResponseCode.success(super.selectByPageNumSize(queryPage, () -> adminService.findByPage(admin)));
    }


    /**
     * 保存管理员
     *
     * @param admin
     * @return
     */
    @PostMapping(value = "/save")
    public ResponseCode save(@RequestBody Admin admin) {
        try {
            admin.setEnableStatus(EnableStatusEnum.PASS.getCode());
            admin.setCreateTime(new Date());
            admin.setLastEditTime(admin.getCreateTime());
            admin.setAdminPasswd(Md5Util.MD5Encode(admin.getAdminPasswd(), "utf8"));
            //判断管理员工号是否存在
            if (adminService.countAdminNumber(admin.getAdminNumber()) >= 1) {
            return ResponseCode.RepeaterrorType();
            }
            adminService.save(admin);
            return ResponseCode.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseCode update(@RequestBody Admin admin) {
        try {
            admin.setLastEditTime(new Date());
            //判断管理员工号是否存在
            List<String> numberList=adminService.numberList();
            //得到原来的number
            String oldNumber=adminService.findoldNumberById(admin.getId());
            //得到现在的number
            String currentNumber=admin.getAdminNumber();

            if(admin.getAdminPasswd() != null && !admin.getAdminPasswd().isEmpty())
                admin.setAdminPasswd(Md5Util.MD5Encode(admin.getAdminPasswd(), "utf8"));
            if(currentNumber.equals(oldNumber)){
                adminService.update(admin);
                return ResponseCode.success();
            }else if(numberList.contains(currentNumber)){
                return ResponseCode.RepeaterrorType();
            }else{    adminService.update(admin);
                return ResponseCode.success();}




        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping(value = "/delete")
    public ResponseCode delete(@RequestBody List<Long> ids) {
        try {
            adminService.delete(ids);
            return ResponseCode.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
