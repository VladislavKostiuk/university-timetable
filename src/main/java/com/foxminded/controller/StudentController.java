package com.foxminded.controller;

import com.foxminded.dto.StudentDto;
import com.foxminded.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/students")
    public String showAll(Model model) {
        List<StudentDto> allStudents = studentService.getAllStudents();
        model.addAttribute("allStudents", allStudents);
        return "entityPages/studentPage";
    }
}

