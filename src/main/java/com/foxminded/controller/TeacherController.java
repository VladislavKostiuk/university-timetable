package com.foxminded.controller;

import com.foxminded.dto.TeacherDTO;
import com.foxminded.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/teachers")
    public String showAll(Model model) {
        List<TeacherDTO> allTeachers = teacherService.getAllTeachers();
        model.addAttribute("allTeachers", allTeachers);
        return "entityPages/teacherPage";
    }
}
