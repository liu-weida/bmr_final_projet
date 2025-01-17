package org.rhw.bmr.user;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

//添加持久层接口扫描器,要具体到某个包的扫描路径
@MapperScan("org.rhw.bmr.user.dao.mapper")
public class BmrUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmrUserApplication.class, args);
    }
}
