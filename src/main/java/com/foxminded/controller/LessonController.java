package com.foxminded.controller;

import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.LessonDTO;
import com.foxminded.service.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/lessons")
    public String showAll(Model model) {
        List<LessonDTO> allLessons = lessonService.getAllLessons();
        model.addAttribute("allLessons", allLessons);
        return "entityPages/lessonPage";
    }
}
