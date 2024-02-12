package com.foxminded.controller;

import com.foxminded.dto.GroupDTO;
import com.foxminded.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/groups")
    public String showAll(Model model) {
        List<GroupDTO> allGroups = groupService.getAllGroups();
        model.addAttribute("allGroups", allGroups);
        return "entityPages/groupPage";
    }
}
