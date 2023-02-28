package com.jsh.erp;

import com.jsh.erp.controller.ActuatorController;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@Slf4j
@SpringBootApplication
@MapperScan("com.jsh.erp.datasource.mappers")
@ServletComponentScan
@EnableScheduling
public class ErpApplication implements ApplicationListener<ApplicationReadyEvent> {
    public static void main(String[] args) throws IOException {
//        ConfigurableApplicationContext context =
        SpringApplication.run(ErpApplication.class, args);
//        Environment environment = context.getBean(Environment.class);
//        System.out.println("启动成功，后端服务API地址：http://" + ComputerInfo.getIpAddr() + ":"
//                + environment.getProperty("server.port") + "/jshERP-boot/doc.html");
//        System.out.println("您还需启动前端服务，启动命令：yarn run serve 或 npm run serve，测试用户：jsh，密码：123456");
    }

    @Autowired
    private ActuatorController actuator;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 设置服务状态
        actuator.setReady(true);
        log.info("Service Ready!");
    }
}
