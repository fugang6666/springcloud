package com.spring.gateway.feign.controller;

import com.spring.gateway.feign.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private FeignService feignService;

    @GetMapping(value = "hello")
    public String hello(@RequestParam String name) {

        return feignService.sayHiFromClientOne(name);
    }
}
