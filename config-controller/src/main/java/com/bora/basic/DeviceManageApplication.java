package com.bora.basic;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * mybatisPlus 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 创建SQL解析器集合
        List<ISqlParser> sqlParserList = new ArrayList<>();

        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }
}
