package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.StudentDto;
import com.foxminded.enums.Role;
import com.foxminded.helper.SelectedOptionsConverter;
import com.foxminded.service.CourseService;
import com.foxminded.service.CustomUserDetailsService;
import com.foxminded.service.GroupService;
import com.foxminded.service.StudentService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin-panel/students")
public class StudentController {
    private final StudentService studentService;
    private final GroupService groupService;
    private final CourseService courseService;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final SelectedOptionsConverter optionsConverter;

    @GetMapping
    public String showAll(Model model, @RequestParam(value = "student-name", required = false) String studentName,
                          @RequestParam(value = "group-name", required = false) String groupName) {
        List<StudentDto> allStudents = studentService.getAllStudents();
        allStudents = allStudents.stream().sorted(Comparator.comparing(StudentDto::id)).toList();

        if (studentName != null) {
            allStudents = allStudents.stream()
                    .filter(student -> student.name().startsWith(studentName))
                    .toList();
        }

        if (groupName != null) {
                allStudents = allStudents.stream()
                        .filter(student -> student.groupDto() != null)
                        .filter(student -> student.groupDto().name().startsWith(groupName))
                        .toList();
        }

        model.addAttribute("allStudents", allStudents);
        return "entityPages/studentPage";
    }

    @GetMapping("/student-update/{studentId}")
    public String showUpdatePage(@PathVariable("studentId") Long studentId, Model model) {
        StudentDto student = studentService.getStudentById(studentId);
        List<String> studentCourses = student.courseDtoList().stream().map(CourseDto::name).toList();
        initModel(model);
        model.addAttribute("student", student);
        model.addAttribute("studentCourses", studentCourses);
        return "updatePages/updateStudentPage";
    }

    @GetMapping("/student-creation")
    public String showCreatePage(Model model) {
        initModel(model);
        model.addAttribute("studentRole", Role.STUDENT);
        return "createPages/createStudentPage";
    }

    @PostMapping("/search")
    public String search(@RequestParam("studentName") String studentName, @RequestParam("groupName") String groupName) {
        return "redirect:/admin-panel/students?student-name=" + studentName + "&group-name=" + groupName;
    }

    @PostMapping("/student-deletion/{studentId}")
    public String deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudentById(studentId);
        return "redirect:/admin-panel/students";
    }

    @PostMapping("/student-creation")
    public String createStudent(@RequestParam("name") String name,
                                @RequestParam("password") String password,
                                @RequestParam("group") Long groupId,
                                @RequestParam(value = "selectedCourses", required = false) String selectedCourses,
                                @RequestParam(value = "selectedRoles", required = false) String selectedRoles) {

        if (!userDetailsService.isNameAvailable(name)) {
            return "redirect:/admin-panel/students/student-creation?error=true";
        }

        String encodedPass =  passwordEncoder.encode(password);
        StudentDto newStudent = generateStudent(0L, name, encodedPass, groupId, selectedCourses, selectedRoles);

        studentService.addStudent(newStudent);
        return "redirect:/admin-panel/students";
    }

    @PostMapping("/student-update")
    public String updateStudent(@RequestParam("studentId") Long studentId,
                                @RequestParam("name") String name,
                                @RequestParam("password") String password,
                                @RequestParam("group") Long groupId,
                                @RequestParam(value = "selectedCourses", required = false) String selectedCourses,
                                @RequestParam(value = "selectedRoles", required = false) String selectedRoles) {
        StudentDto oldStudent = studentService.getStudentById(studentId);

        if (!name.equals(oldStudent.name()) && !userDetailsService.isNameAvailable(name)) {
            return "redirect:/admin-panel/students/student-update/" + studentId +"?error=true";
        }

        String encodedPass = password.equals("") ? oldStudent.password() : passwordEncoder.encode(password);
        StudentDto updatedStudent = generateStudent(studentId, name, encodedPass, groupId, selectedCourses, selectedRoles);

        studentService.updateStudent(updatedStudent);
        return "redirect:/admin-panel/students";
    }

    private void initModel(Model model) {
        List<GroupDto> allGroups = groupService.getAllGroups();
        List<CourseDto> allCourses = courseService.getAllCourses();
        List<Role> allRoles = Arrays.asList(Role.values());
        model.addAttribute("allGroups", allGroups);
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("allRoles", allRoles);
    }

    private StudentDto generateStudent(Long studentId, String name, String encodedPassword,
                                       Long groupId, String selectedCourses, String selectedRoles) {

        GroupDto group = groupId != 0 ? groupService.getGroupById(groupId) : null;
        List<CourseDto> courseDtoList = optionsConverter.selectedCoursesToList(selectedCourses);
        Set<Role> roleSet = optionsConverter.selectedRolesToSet(selectedRoles);
        return new StudentDto(studentId, name, encodedPassword, roleSet, group, courseDtoList);
    }
}

