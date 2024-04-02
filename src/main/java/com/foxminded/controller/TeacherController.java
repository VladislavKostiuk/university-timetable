package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.dto.TimetableDto;
import com.foxminded.enums.Role;
import com.foxminded.enums.TimetableType;
import com.foxminded.helper.SelectedOptionsConverter;
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
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin-panel/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TimetableService timetableService;
    private final SelectedOptionsConverter optionsConverter;

    @GetMapping
    public String showAll(Model model, @RequestParam(value = "teacher-name", required = false) String teacherName) {
        List<TeacherDto> allTeachers = teacherService.getAllTeachers();
        allTeachers = allTeachers.stream().sorted(Comparator.comparing(TeacherDto::id)).toList();

        if (teacherName != null) {
            allTeachers = allTeachers.stream()
                    .filter(teacher -> teacher.name().startsWith(teacherName))
                    .toList();
        }

        model.addAttribute("allTeachers", allTeachers);
        return "entityPages/teacherPage";
    }

    @GetMapping("/teacher-update/{teacherId}")
    public String showUpdatePage(@PathVariable("teacherId") Long teacherId, Model model) {
        TeacherDto teacher = teacherService.getTeacherById(teacherId);
        List<String> teacherCourses = teacher.courseDtoList().stream().map(CourseDto::name).toList();
        initModel(model);
        model.addAttribute("teacher", teacher);
        model.addAttribute("teacherCourses", teacherCourses);
        return "updatePages/updateTeacherPage";
    }

    @GetMapping("/teacher-creation")
    public String showCreatePage(Model model) {
        initModel(model);
        model.addAttribute("teacherRole", Role.TEACHER);
        return "createPages/createTeacherPage";
    }

    @PostMapping("/search")
    public String search(@RequestParam("teacherName") String teacherName) {
        return "redirect:/admin-panel/teachers?teacher-name=" + teacherName;
    }

    @PostMapping("/teacher-deletion/{teacherId}")
    public String deleteTeacher(@PathVariable("teacherId") Long teacherId) {
        teacherService.deleteTeacherById(teacherId);
        return "redirect:/admin-panel/teachers";
    }

    @PostMapping("/teacher-creation")
    public String createTeacher(@RequestParam("name") String name,
                                @RequestParam("password") String password,
                                @RequestParam(value = "selectedCourses", required = false) String selectedCourses,
                                @RequestParam(value = "selectedRoles", required = false) String selectedRoles) {


        if (!userDetailsService.isNameAvailable(name)) {
            return "redirect:/admin-panel/teachers/teacher-creation?error=true";
        }

        String encodedPass = passwordEncoder.encode(password);
        TeacherDto newTeacher = generateTeacher(0L , name, encodedPass, selectedCourses, selectedRoles);

        teacherService.addTeacher(newTeacher);

        TimetableDto timetable = new TimetableDto(0L, TimetableType.TEACHER_TIMETABLE,
                name, new ArrayList<>());
        timetableService.addTimetable(timetable);

        return "redirect:/admin-panel/teachers";
    }

    @PostMapping("/teacher-update")
    public String updateTeacher(@RequestParam("teacherId") Long teacherId,
                                @RequestParam("name") String name,
                                @RequestParam("password") String password,
                                @RequestParam(value = "selectedCourses", required = false) String selectedCourses,
                                @RequestParam(value = "selectedRoles", required = false) String selectedRoles) {
        TeacherDto oldTeacher = teacherService.getTeacherById(teacherId);

        if (!name.equals(oldTeacher.name()) && !userDetailsService.isNameAvailable(name)) {
            return "redirect:/admin-panel/teachers/teacher-update/" + teacherId +"?error=true";
        }

        String encodedPass = password.equals("") ? oldTeacher.password() : passwordEncoder.encode(password);
        TeacherDto updatedTeacher = generateTeacher(teacherId, name, encodedPass, selectedCourses, selectedRoles);

        teacherService.updateTeacher(updatedTeacher);

        if (!oldTeacher.name().equals(name)) {
            TimetableDto oldTimetable = timetableService.getTimetableByQualifyingName(oldTeacher.name());
            TimetableDto updatedTimetable = new TimetableDto(oldTimetable.id(), oldTimetable.timetableType(),
                    name, oldTimetable.lessonDtoList());
            timetableService.updateTimetable(updatedTimetable);
        }

        return "redirect:/admin-panel/teachers";
    }

    private void initModel(Model model) {
        List<CourseDto> allCourses = courseService.getAllCourses();
        List<Role> allRoles = Arrays.asList(Role.values());
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("allRoles", allRoles);
    }

    private TeacherDto generateTeacher(Long teacherId, String name, String encodedPassword,
                                       String selectedCourses, String selectedRoles) {
        List<CourseDto> courseDtoList = optionsConverter.selectedCoursesToList(selectedCourses);
        Set<Role> roleSet = optionsConverter.selectedRolesToSet(selectedRoles);
        return  new TeacherDto(teacherId, name, encodedPassword, roleSet,courseDtoList);
    }
}

