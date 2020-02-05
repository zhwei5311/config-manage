package com.bora.device.test;

import com.alibaba.fastjson.JSONObject;
import com.bora.basic.DeviceManageApplication;
import com.bora.basic.dal.config.DruidDataSource;
import com.bora.common.data.CodeGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DeviceManageApplication.class})
public class GeneratorTest {

    @Autowired
    private DruidDataSource druidDataSource;

    @Test
    public void test(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url",druidDataSource.getUrl());
        jsonObject.put("driverClassName",druidDataSource.getDriverClassName());
        jsonObject.put("username",druidDataSource.getUsername());
        jsonObject.put("password",druidDataSource.getPassword());
        CodeGenerator codeGenerator = new CodeGenerator(jsonObject);
        String[] tableName = {"t_basic_template"};
        String[] tablePrefix = {"t_"};
        try {
            codeGenerator.generateCode("com.bora.basic", tableName, tablePrefix,
                    "wangjialei", true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
