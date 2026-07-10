package com.company.employeemanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String homePage() {
         System.out.println("home page accessed");
        return "home";
    }

    @GetMapping("/register-page")
    public String registerPage() {
        System.out.println("register page accessed");
        return "register";
    }

    @GetMapping("/login-page")
    public String loginPage() {
        System.out.println("Login page accessed");

        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }
}
