package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.service.CourseService;
import com.foxminded.service.DbInitService;
import com.foxminded.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping("/")
    public String showMenu(Model model) {
        List<CourseDto> allCourses = courseService.getAllCourses();
        model.addAttribute("allCourses", allCourses);
        return "mainPage";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "authPages/loginPage";
    }
}

