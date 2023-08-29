package com.jan1ooo.apicep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ApiCepApplication {

    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        SpringApplication.run(ApiCepApplication.class, args);
    }

    public static void close(int code){
        SpringApplication.exit(ctx, () -> code);
    }

}
