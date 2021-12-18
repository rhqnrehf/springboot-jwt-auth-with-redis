package edu.jwt.jwt_auth.controller;

import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/private")
public class RestController {

    @GetMapping("/hello")
    public String test(){

        return "TEST";
    }


}
