package com.mdq.yyjhservice.controller;

import com.mdq.yyjhservice.domain.date.TDateFormat;
import com.mdq.yyjhservice.service.date.TDateFormatService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dateformat")
public class DateFormatController {
    @Autowired
    private TDateFormatService tDateFormatService;
    //只修改日期格式表格信息(其他未改动)
    @RequestMapping("/updTDateFormatById/{flag}")
    @RequiresRoles(value={"超级管理员","日期总监"} , logical= Logical.OR)
    @RequiresPermissions(value={"Update"} ,logical = Logical.AND)
    public boolean updTDateFormatById(@PathVariable Integer flag){
        TDateFormat record=new TDateFormat();
        record.setId(1);
        record.setDateFlag(flag);
        return tDateFormatService.updTDateFormatById(record);
    }
}
