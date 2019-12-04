package com.spring.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@RestController
@EnableHystrix
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .header("sys", "user")
                        .filters(f -> f.hystrix(config -> config.setName("fallbackcmd").setFallbackUri("forward:/defaultfallback")))
                        // lb://service-canteen  注册中心 连接服务名称
                         .uri("lb://service-canteen"))
                        //.uri("http://github.com"))
                .route(p -> p
                        .header("sys", "api")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://baidu.com"))
                //.route(p -> p.path("/*").filters(f->f.addRequestHeader("Hystrix","熔断")))
                .build();
    }

    @GetMapping("/defaultfallback")
    public Map<String,String> defaultfallback() {
        //System.out.println("降级操作...");
        Map<String, String> map = new HashMap<>();
        map.put("resultCode", "false");
        map.put("resultMessage", "服务异常");
        map.put("resultObj", "这里测试网关服务熔断");
        return map;
    }

}
