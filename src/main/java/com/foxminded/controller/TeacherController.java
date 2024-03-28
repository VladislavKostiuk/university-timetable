package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.dto.TimetableDto;
import com.foxminded.service.CourseService;
import com.foxminded.service.CustomUserDetailsService;
import com.foxminded.service.TeacherService;
import com.foxminded.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TimetableService timetableService;

    @GetMapping
    public String showAll(Model model) {
        List<TeacherDto> allTeachers = teacherService.getAllTeachers();
        model.addAttribute("allTeachers", allTeachers);
        return "entityPages/teacherPage";
    }

    @GetMapping("/teacher-update/{teacherId}")
    public String showUpdatePage(@PathVariable("teacherId") Long teacherId, Model model) {
        TeacherDto teacher = teacherService.getTeacherById(teacherId);
        List<CourseDto> allCourses = courseService.getAllCourses();
        List<String> teacherCourses = teacher.courseDtoList().stream().map(CourseDto::name).toList();
        model.addAttribute("teacher", teacher);
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("teacherCourses", teacherCourses);
        return "updatePages/updateTeacherPage";
    }

    @PostMapping("/teacher-deletion/{teacherId}")
    public String deleteSubject(@PathVariable("teacherId") Long teacherId) {
        teacherService.deleteTeacherById(teacherId);
        return "redirect:/teachers";
    }

    @PostMapping("/teacher-update")
    public String updateSubject(@RequestParam("teacherId") Long teacherId,
                                @RequestParam("name") String name,
                                @RequestParam("password") String password,
                                @RequestParam(value = "selectedCourses", required = false) String selectedCourses) {
        TeacherDto oldTeacher = teacherService.getTeacherById(teacherId);

        if (!userDetailsService.isNameAvailable(name, oldTeacher.name())) {
            return "redirect:/teachers/teacher-update/" + teacherId +"?error=true";
        }

        String encodedPass = password.equals("") ? oldTeacher.password() : passwordEncoder.encode(password);
        List<String> selectedCoursesList = selectedCourses != null ? Arrays.asList(selectedCourses.split(",")) : new ArrayList<>();
        List<CourseDto> courseDtoList = selectedCoursesList.stream().map(courseService::getCourseByName).toList();
        TeacherDto updatedTeacher = new TeacherDto(teacherId, name, encodedPass, oldTeacher.roles(),courseDtoList);

        teacherService.updateTeacher(updatedTeacher);

        if (!oldTeacher.name().equals(name)) {
            TimetableDto oldTimetable = timetableService.getTimetableByQualifyingName(oldTeacher.name());
            TimetableDto updatedTimetable = new TimetableDto(oldTimetable.id(), oldTimetable.timetableType(),
                    name, oldTimetable.lessonDtoList());
            timetableService.updateTimetable(updatedTimetable);
        }
        return "redirect:/teachers";
    }
}

