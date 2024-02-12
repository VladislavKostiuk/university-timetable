package com.foxminded.controller;

import com.foxminded.dto.SubjectDTO;
import com.foxminded.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/subjects")
    public String showAll(Model model) {
        List<SubjectDTO> allSubjects = subjectService.getAllSubjects();
        model.addAttribute("allSubjects", allSubjects);
        return "entityPages/subjectPage";
    }
}
