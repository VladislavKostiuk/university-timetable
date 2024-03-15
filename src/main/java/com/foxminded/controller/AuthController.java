package com.foxminded.controller;

import com.foxminded.dto.GroupDto;
import com.foxminded.dto.StudentDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.dto.TimetableDto;
import com.foxminded.enums.Role;
import com.foxminded.enums.TimetableType;
import com.foxminded.service.StudentService;
import com.foxminded.service.TeacherService;
import com.foxminded.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final PasswordEncoder passwordEncoder;
    private final TimetableService timetableService;

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
                               @RequestParam("user_type") String userType) {
        Optional<StudentDto> existingStudent = studentService.getStudentByName(username);
        Optional<TeacherDto> existingTeacher = teacherService.getTeacherByName(username);

        if (existingStudent.isPresent() || existingTeacher.isPresent()) {
            return "redirect:/register?error=true";
        }

        if (userType.equals("student")) {
            StudentDto student = createStudent(username, password);
            studentService.addStudent(student);
        } else if (userType.equals("teacher")) {
            TeacherDto teacher = createTeacher(username, password);
            teacherService.addTeacher(teacher);

            TimetableDto timetable = new TimetableDto(0L, TimetableType.TEACHER_TIMETABLE,
                    username, new ArrayList<>());
            timetableService.addTimetable(timetable);
        }

        return "redirect:/";
    }

    private StudentDto createStudent(String username, String password) {
        String encryptedPass = passwordEncoder.encode(password);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.STUDENT);
        StudentDto student = new StudentDto(
                0L,
                username,
                encryptedPass,
                roles,
                null,
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

