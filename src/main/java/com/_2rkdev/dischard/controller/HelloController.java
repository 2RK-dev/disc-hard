package com._2rkdev.dischard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hello")
public class HelloController {

    @GetMapping()
    public String hello() {
        return "Hello World. We are the 2RK Dev Team !!!";
    }
}
