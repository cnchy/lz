package com.iotlab.integrityarchives.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.iotlab.integrityarchives.dto.ResponseCode;
import com.iotlab.integrityarchives.enums.StatusEnums;
import com.iotlab.integrityarchives.service.PersonConsulationsManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@ResponseBody
@Controller
@RequestMapping("/manage")
public class personConsulationsManageController {

    @Autowired
    private PersonConsulationsManageService personConsulationsManageService;

    @PostMapping("/setConsulation")
    public ResponseCode setConsulation(
            @RequestParam("dateStart") Date dateStart,
            @RequestParam("dateEnd") Date dateEnd,
            @RequestParam("attention") String attention,
            @RequestParam("on") Boolean on
            ) {
        try {
            personConsulationsManageService.setConsulation(dateStart, dateEnd, on, attention);
            return new ResponseCode(200, "");
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseCode(500, e.toString());
        }
    }

    @GetMapping("/getConsulation")
    public ResponseCode getConsulation() {
        try {
            JSONObject jsonObject = personConsulationsManageService.getConsulation();
            return new ResponseCode(StatusEnums.SUCCESS, jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseCode(500, e.toString());
        }
    }
}
