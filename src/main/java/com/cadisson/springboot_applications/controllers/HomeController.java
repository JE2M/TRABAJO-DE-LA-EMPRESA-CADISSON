package com.cadisson.springboot_applications.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {

    @GetMapping("/profesor")
    public String inicio() {
        return "Layout/home";
    }
}