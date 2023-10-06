package com.cerebros;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
@MapperScan(basePackages="com.cerebros.integration.mapper")
@ComponentScan(basePackages = {"com.cerebros.services","com.cerebros.integration", "com.cerebros.controller","com.cerebros.utility"})

public class CerebrosApplication {
    public static void main(String[] args) {
        SpringApplication.run(CerebrosApplication.class, args);
    }

    /**
     * This method creates a Logger that can be autowired in other classes:{@code
     *    @Autowired
     *    private Logger logger;
    }*/
    @Bean
    @Scope("prototype")
    Logger createLogger(InjectionPoint ip) {
        Class<?> classThatWantsALogger = ip.getField().getDeclaringClass();
        return LoggerFactory.getLogger(classThatWantsALogger);
    }
}