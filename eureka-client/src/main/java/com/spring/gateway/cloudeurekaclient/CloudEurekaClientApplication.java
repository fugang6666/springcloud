package com.spring.gateway.cloudeurekaclient;

import com.spring.gateway.cloudeurekaclient.Controller.SenderTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class CloudEurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudEurekaClientApplication.class, args);
    }


    @Value("${server.port}")
    String port;
    @Autowired
    private SenderTest senderTest;

    @RequestMapping("/hi")
    public String home(@RequestParam(value = "name", defaultValue = "forezp") String name) throws InterruptedException {
      /*  for (int i = 0; i < 50; i++) {
            senderTest.send(i);
            Thread.sleep(300);
        }*/

        senderTest.send1();
        senderTest.send2();
        return "hi " + name + " ,i am from port:" + port;
    }

}
