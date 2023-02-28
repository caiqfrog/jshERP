package com.jsh.erp.controller;

import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * health ping
 * @author caiqfrog
 */
@RestController
public class ActuatorController {

    @Setter
    private boolean ready = false;

    @GetMapping(value = "/ping")
    public String ping(HttpServletResponse response) {
        response.setStatus(ready ? HttpStatus.OK.value() : HttpStatus.SERVICE_UNAVAILABLE.value());
        return "pong";
    }

}
