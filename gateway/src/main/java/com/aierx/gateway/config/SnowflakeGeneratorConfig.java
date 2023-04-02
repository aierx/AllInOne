package com.aierx.gateway.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeGeneratorConfig {



    @Bean
    public SnowflakeGenerator createSnowflakeGenerator(){
        return new SnowflakeGenerator();
    }
}
