package com.spring.gateway.feign.service.impl;

import com.spring.gateway.feign.service.FeignService;
import org.springframework.stereotype.Component;

@Component
public class SchedualServiceHiHystric implements FeignService {

    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry"+name;
    }
}
