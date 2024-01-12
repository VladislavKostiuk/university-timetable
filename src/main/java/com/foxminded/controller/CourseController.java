package com.foxminded.controller;

import com.foxminded.dto.CourseDTO;
import com.foxminded.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses")
    public String showAll(Model model) {
        List<CourseDTO> allCourses = courseService.getAllCourses();
        model.addAttribute("allCourses", allCourses);
        return "entityPages/coursePage";
    }
}
