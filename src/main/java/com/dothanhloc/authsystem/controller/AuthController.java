package com.dothanhloc.authsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AuthController {
    @GetMapping("path")
    public String showRegisterPage() {
        return "register";
    }
    
}
