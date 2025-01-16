package org.rhw.bmr.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.rhw.bmr.project.dao.mapper")
public class BmrProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(BmrProjectApplication.class, args);
    }
}
