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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class AdminPanelController {
    private final StudentService studentService;
    private final TeacherService teacherService;

    @GetMapping("/adminPanel/students")
    public String showStudentPanel(Model model, String search) {
        List<StudentDto> students = studentService.getAllStudents();

        if (search != null) {
            students = students.stream()
                    .filter((student -> student.name().startsWith(search)))
                    .toList();
        }

        model.addAttribute("students", students);
        model.addAttribute("allRoles", Role.values());
        return "adminPanelPages/studentPage";
    }

    @GetMapping("/adminPanel/teachers")
    public String showTeacherPanel(Model model, String search) {
        List<TeacherDto> teachers = teacherService.getAllTeachers();

        if (search != null) {
            teachers = teachers.stream()
                    .filter((teacher -> teacher.name().startsWith(search)))
                    .toList();
        }

        model.addAttribute("teachers", teachers);
        model.addAttribute("allRoles", Role.values());
        return "adminPanelPages/teacherPage";
    }

    @PostMapping("/adminPanel/{studentId}/updateStudentRoles")
    public String updateStudentRoles(@PathVariable("studentId") Long studentId, @RequestParam("selected") String selected) {
        List<String> selectedRoles = Arrays.asList(selected.split(","));
        Set<Role> updatedRoles = new HashSet<>();
        selectedRoles.forEach(role -> updatedRoles.add(Role.valueOf(role)));

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

    @PostMapping("/adminPanel/{teacherId}/updateTeacherRoles")
    public String updateTeacherRoles(@PathVariable("teacherId") Long teacherId, @RequestParam("selected") String selected) {
        List<String> selectedRoles = Arrays.asList(selected.split(","));
        Set<Role> updatedRoles = new HashSet<>();
        selectedRoles.forEach(role -> updatedRoles.add(Role.valueOf(role)));

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

    @PostMapping("/adminPanel/searchStudent")
    public String searchStudentByName(@RequestParam("searchText") String searchText) {
        return "redirect:/adminPanel/students?search=" + searchText;
    }

    @PostMapping("/adminPanel/searchTeacher")
    public String searchTeacherByName(@RequestParam("searchText") String searchText) {
        return "redirect:/adminPanel/teachers?search=" + searchText;
    }
}

