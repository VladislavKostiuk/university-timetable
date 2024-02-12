package com.foxminded.controller;

import com.foxminded.dto.TimetableDto;
import com.foxminded.service.TimetableService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TimetableController {
    private final TimetableService timetableService;

    @GetMapping("/timetables")
    public String showAll(Model model) {
        List<TimetableDto> allTimetables = timetableService.getAllTimetables();
        model.addAttribute("allTimetables", allTimetables);
        return "entityPages/timetablePage";
    }
}

