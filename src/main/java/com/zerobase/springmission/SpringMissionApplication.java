package com.zerobase.springmission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringMissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMissionApplication.class, args);
    }

}
