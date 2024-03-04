package com.foxminded.controller;

import com.foxminded.dto.StudentDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.enums.Role;
import com.foxminded.service.StudentService;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adminPanel")
public class AdminPanelController {
    private final StudentService studentService;
    private final TeacherService teacherService;

    @GetMapping("/students")
    public String showStudentPanel(Model model, @RequestParam(value = "searchText", required = false) String searchText) {
        List<StudentDto> students = studentService.getAllStudents();

        if (searchText != null) {
            students = students.stream()
                    .filter((student -> student.name().startsWith(searchText)))
                    .toList();
        }

        model.addAttribute("students", students);
        model.addAttribute("allRoles", Role.values());
        return "adminPanelPages/studentPage";
    }

    @GetMapping("/teachers")
    public String showTeacherPanel(Model model, @RequestParam(value = "searchText", required = false) String searchText) {
        List<TeacherDto> teachers = teacherService.getAllTeachers();

        if (searchText != null) {
            teachers = teachers.stream()
                    .filter((teacher -> teacher.name().startsWith(searchText)))
                    .toList();
        }

        model.addAttribute("teachers", teachers);
        model.addAttribute("allRoles", Role.values());
        return "adminPanelPages/teacherPage";
    }

    @PostMapping("/student-roles-update/{studentId}")
    public String updateStudentRoles(@PathVariable("studentId") Long studentId,
                                     @RequestParam(value = "selectedRoles", required = false) String selectedRoles) {
        Set<Role> updatedRoles = selectedRolesToSet(selectedRoles);

        StudentDto student = studentService.getStudentById(studentId);
        StudentDto updatedStudent = new StudentDto(
                student.id(),
                student.name(),
                student.password(),
                updatedRoles,
                student.groupDto(),
                student.courseDtoList()
        );
        studentService.updateStudent(updatedStudent);

        return "redirect:/adminPanel/students";
    }

    @PostMapping("/teacher-roles-update/{teacherId}")
    public String updateTeacherRoles(@PathVariable("teacherId") Long teacherId,
                                     @RequestParam(value = "selectedRoles", required = false) String selectedRoles) {
        Set<Role> updatedRoles = selectedRolesToSet(selectedRoles);

        TeacherDto teacher = teacherService.getTeacherById(teacherId);
        TeacherDto updatedTeacher = new TeacherDto(
                teacher.id(),
                teacher.name(),
                teacher.password(),
                updatedRoles,
                teacher.courseDtoList()
        );
        teacherService.updateTeacher(updatedTeacher);

        return "redirect:/adminPanel/teachers";
    }

    @PostMapping("/searchStudent")
    public String searchStudentByName(@RequestParam("searchText") String searchText) {
        return "redirect:/adminPanel/students?searchText=" + searchText;
    }

    @PostMapping("/searchTeacher")
    public String searchTeacherByName(@RequestParam("searchText") String searchText) {
        return "redirect:/adminPanel/teachers?searchText=" + searchText;
    }

    private Set<Role> selectedRolesToSet(String selectedRoles) {
        List<String> selectedRolesList = selectedRoles != null ? Arrays.asList(selectedRoles.split(",")) : new ArrayList<>();
        Set<Role> updatedRoles = new HashSet<>();
        selectedRolesList.forEach(role -> updatedRoles.add(Role.valueOf(role)));
        return updatedRoles;
    }
}

