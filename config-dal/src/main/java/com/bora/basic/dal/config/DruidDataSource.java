package com.bora.basic.dal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/***
 *Ticket: 
 * @author 汪家磊
 * @email wangjialei@boranet.com.cn
 * @Date: 2020-02-05 14:19:20
 *****/
@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidDataSource {

    private String driverClassName;
    private String username;
    private String password;
    private String url;

}
