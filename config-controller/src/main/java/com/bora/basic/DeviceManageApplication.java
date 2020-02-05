package com.bora.basic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/***
 *Ticket: 
 * @author 汪家磊
 * @email wangjialei@boranet.com.cn
 * @Date: 2020-01-09 12:56:19
 *****/
@MapperScan("com.bora.basic.dal.dao")
@SpringBootApplication
public class DeviceManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeviceManageApplication.class,args);
    }
}
