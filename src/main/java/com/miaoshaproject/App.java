package com.miaoshaproject;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.miaoshaproject"})
@MapperScan("com.miaoshaproject.dao")
@RestController
@EnableAsync

public class App 
{
    @RequestMapping("/home")
    public String home(){
        return "hello word!";
    }
    public static void main( String[] args )
    {

        SpringApplication.run(App.class, args);
    }

}
