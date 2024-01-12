package com.foxminded.controller;

import com.foxminded.service.DbInitService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class MainController {
    private final DbInitService dbInitService;
    @GetMapping("/")
    public String showMenu() {
        return "mainPage";
    }

    @PostMapping("/init")
    public String initDb() {
        dbInitService.init();
        return "redirect:/";
    }
}
