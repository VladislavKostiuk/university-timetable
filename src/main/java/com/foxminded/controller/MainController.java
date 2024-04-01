package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.service.CourseService;
import com.foxminded.service.DbInitService;
import com.foxminded.service.StudentService;
import com.foxminded.service.TeacherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
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
    private final TeacherService teacherService;
    private final CourseService courseService;

    @GetMapping("/")
    public String showMenu(Model model) {
        List<CourseDto> allCourses = courseService.getAllCourses();
        List<TeacherDto> allTeachers = teacherService.getAllTeachers();
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("allTeachers", allTeachers);
        return "mainPage";
    }

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest httpServletRequest) throws ServletException {
        httpServletRequest.logout();
        return "authPages/loginPage";
    }
}

