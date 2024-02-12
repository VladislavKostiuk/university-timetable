package com.foxminded.controller;

import com.foxminded.dto.GroupDto;
import com.foxminded.dto.StudentDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.enums.Role;
import com.foxminded.service.StudentService;
import com.foxminded.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginPage() {
        return "authPages/loginPage";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "authPages/registerPage";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("radio_option") String radio_option) {
        StudentDto student;
        TeacherDto teacher;
        try {
            student = studentService.getStudentByName(username);
            return "redirect:/register?error=true";
        } catch (IllegalArgumentException e) {}

        try {
            teacher = teacherService.getTeacherByName(username);
            return "redirect:/register?error=true";
        } catch (IllegalArgumentException e) {}

        if (radio_option.equals("student")) {
            student = createStudent(username, password);
            studentService.addStudent(student);
        } else if (radio_option.equals("teacher")) {
            teacher = createTeacher(username, password);
            teacherService.addTeacher(teacher);
        }

        return "redirect:/";
    }

    private StudentDto createStudent(String username, String password) {
        String encryptedPass = passwordEncoder.encode(password);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.STUDENT);
        GroupDto group = new GroupDto(1L, "");
        StudentDto student = new StudentDto(
                0L,
                username,
                encryptedPass,
                roles,
                group,
                new ArrayList<>()
        );
        return student;
    }

    private TeacherDto createTeacher(String username, String password) {
        String encryptedPass = passwordEncoder.encode(password);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.TEACHER);
        TeacherDto teacher = new TeacherDto(
                0L,
                username,
                encryptedPass,
                roles,
                new ArrayList<>()
        );
        return teacher;
    }
}

