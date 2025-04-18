package com._2rkdev.dischard;

import org.springframework.boot.SpringApplication;

public class TestDiscHardApplication {

    public static void main(String[] args) {
        SpringApplication.from(DiscHardApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
