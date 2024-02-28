package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.SubjectDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.service.CourseService;
import com.foxminded.service.GroupService;
import com.foxminded.service.SubjectService;
import com.foxminded.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subjects")
public class SubjectController {
    private final SubjectService subjectService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final GroupService groupService;

    @GetMapping
    public String showAll(Model model) {
        List<SubjectDto> allSubjects = subjectService.getAllSubjects();
        model.addAttribute("allSubjects", allSubjects);
        return "entityPages/subjectPage";
    }

    @GetMapping("/subject-creation")
    public String showCreatePage(Model model) {
        initModel(model);
        return "createPages/createSubjectPage";
    }

    @GetMapping("/subject-update/{subjectId}")
    public String showUpdatePage(@PathVariable("subjectId") Long subjectId, Model model) {
        SubjectDto subject = subjectService.getSubjectById(subjectId);
        model.addAttribute("subject", subject);
        initModel(model);
        return "updatePages/updateSubjectPage";
    }

    @PostMapping("/subject-creation")
    public String createSubject(@RequestParam("course") Long courseId,
                                @RequestParam("teacher") Long teacherId,
                                @RequestParam("group") Long groupId) {
        CourseDto course = courseService.getCourseById(courseId);
        TeacherDto teacher = teacherService.getTeacherById(teacherId);
        GroupDto group = groupService.getGroupById(groupId);

        SubjectDto newSubject = new SubjectDto(0L, course, teacher, group, new ArrayList<>());

        if (checkIfSubjectExists(newSubject)) {
            return "redirect:/subjects/subject-creation?error=true";
        }

        subjectService.addSubject(newSubject);

        return "redirect:/subjects";
    }

    @PostMapping("/subject-deletion/{subjectId}")
    public String deleteSubject(@PathVariable("subjectId") Long subjectId) {
        subjectService.deleteSubjectById(subjectId);
        return "redirect:/subjects";
    }

    @PostMapping("/subject-update")
    public String updateSubject(@RequestParam("subjectId") Long subjectId,
                                @RequestParam("course") Long courseId,
                                @RequestParam("teacher") Long teacherId,
                                @RequestParam("group") Long groupId) {
        CourseDto course = courseService.getCourseById(courseId);
        TeacherDto teacher = teacherService.getTeacherById(teacherId);
        GroupDto group = groupService.getGroupById(groupId);
        SubjectDto oldSubject = subjectService.getSubjectById(subjectId);

        SubjectDto updatedSubject = new SubjectDto(subjectId, course, teacher, group, oldSubject.lessonDtoList());

        if (checkIfSubjectExists(updatedSubject)) {
            return "redirect:/subjects/subject-update/" + subjectId + "?error=true";
        }

        subjectService.updateSubject(updatedSubject);
        return "redirect:/subjects";
    }

    private boolean checkIfSubjectExists(SubjectDto newSubject) {
        List<SubjectDto> allSubjects = subjectService.getAllSubjects();
        for (var subject : allSubjects) {
            if (newSubject.courseDto().equals(subject.courseDto()) &&
                    newSubject.teacherDto().equals(subject.teacherDto()) &&
                    newSubject.groupDto().equals(subject.groupDto())) {
                return true;
            }
        }

        return false;
    }

    private void initModel(Model model) {
        List<CourseDto> allCourses = courseService.getAllCourses();
        List<TeacherDto> allTeachers = teacherService.getAllTeachers();
        List<GroupDto> allGroups = groupService.getAllGroups();

        model.addAttribute("allCourses", allCourses);
        model.addAttribute("allTeachers", allTeachers);
        model.addAttribute("allGroups", allGroups);
    }
}

