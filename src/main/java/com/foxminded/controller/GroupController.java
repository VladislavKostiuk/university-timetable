package com.foxminded.controller;

import com.foxminded.dto.GroupDto;
import com.foxminded.service.GroupService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/groups")
    public String showAll(Model model) {
        List<GroupDto> allGroups = groupService.getAllGroups();
        model.addAttribute("allGroups", allGroups);
        return "entityPages/groupPage";
    }
}

