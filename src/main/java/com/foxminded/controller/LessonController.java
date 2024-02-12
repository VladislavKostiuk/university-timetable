package com.foxminded.controller;

import com.foxminded.dto.LessonDto;
import com.foxminded.service.LessonService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/lessons")
    public String showAll(Model model) {
        List<LessonDto> allLessons = lessonService.getAllLessons();
        model.addAttribute("allLessons", allLessons);
        return "entityPages/lessonPage";
    }

}
