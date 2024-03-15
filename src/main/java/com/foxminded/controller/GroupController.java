package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.service.GroupService;
import com.foxminded.service.TimetableService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final TimetableService timetableService;

    @GetMapping
    public String showAll(Model model) {
        List<GroupDto> allGroups = groupService.getAllGroups();
        model.addAttribute("allGroups", allGroups);
        return "entityPages/groupPage";
    }

    @GetMapping("/group-creation")
    public String showCreatePage() {
        return "createPages/createGroupPage";
    }

    @GetMapping("/group-update/{groupId}")
    public String showUpdatePage(@PathVariable("groupId") Long groupId, Model model) {
        GroupDto group = groupService.getGroupById(groupId);
        model.addAttribute("group", group);
        return "updatePages/updateGroupPage";
    }

    @PostMapping("/group-creation")
    public String createGroup(@RequestParam("name") String name) {
        GroupDto newGroup = new GroupDto(0L, name);

        if (checkIfGroupExists(newGroup)) {
            return "redirect:/groups/group-creation?error=true";
        }
        groupService.addGroup(newGroup);

        return "redirect:/groups";
    }

    @PostMapping("/group-update")
    public String updateGroup(@RequestParam("groupId") Long groupId,
                               @RequestParam("name") String name) {
        GroupDto updatedGroup = new GroupDto(groupId, name);

        if (checkIfGroupExists(updatedGroup)) {
            return "redirect:/groups/group-update/" + groupId + "?error=true";
        }

        groupService.updateGroup(updatedGroup);
        return "redirect:/groups";
    }

    @PostMapping("/group-deletion/{groupId}")
    public String deleteGroup(@PathVariable("groupId") Long groupId) {
        GroupDto groupDto = groupService.getGroupById(groupId);
        groupService.deleteGroupById(groupId);
        timetableService.getAllTimetables().stream()
                .filter(timetable -> timetable.qualifyingName().equals(groupDto.name()))
                .forEach(timetable -> timetableService.deleteTimetableById(timetable.id()));
        return "redirect:/groups";
    }

    private boolean checkIfGroupExists(GroupDto newGroup) {
        List<GroupDto> allGroups = groupService.getAllGroups();
        for (var group : allGroups) {
            if (group.name().equals(newGroup.name())) {
                return true;
            }
        }

        return false;
    }
}

