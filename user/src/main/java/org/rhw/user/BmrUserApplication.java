package org.rhw.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 短链接后管应用
 * */

//标记一个类为 Spring Boot 应用的入口点
@SpringBootApplication
//启用服务注册与发现功能
@EnableDiscoveryClient
//启用 Spring Cloud OpenFeign 客户端功能
@EnableFeignClients("org.rhw.user.remote")
//指定 MyBatis 的 Mapper 接口所在的包路径，自动扫描并生成代理对象。
@MapperScan("org.rhw.user.dao.mapper")


public class BmrUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmrUserApplication.class, args);
    }
}
