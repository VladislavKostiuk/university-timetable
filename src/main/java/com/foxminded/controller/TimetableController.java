package com.foxminded.controller;

import com.foxminded.dto.TimetableDTO;
import com.foxminded.entity.Timetable;
import com.foxminded.service.TimetableService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class TimetableController {
    private TimetableService timetableService;

    @GetMapping("/timetables")
    public String showAll(Model model) {
        List<TimetableDTO> allTimetables = timetableService.getAllTimetables();
        model.addAttribute("allTimetables", allTimetables);
        return "entityPages/timetablePage";
    }
}
