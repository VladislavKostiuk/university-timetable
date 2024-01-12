package com.foxminded.controller;

import com.foxminded.dto.StudentDTO;
import com.foxminded.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/students")
    public String showAll(Model model) {
        List<StudentDTO> allStudents = studentService.getAllStudents();
        model.addAttribute("allStudents", allStudents);
        return "entityPages/studentPage";
    }
}
