package com.alex.hosp.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.alex.hosp.mapper")
public class HospConfig {
}
