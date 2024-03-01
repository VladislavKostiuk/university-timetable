package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.StudentDto;
import com.foxminded.dto.SubjectDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.entity.Course;
import com.foxminded.service.CourseService;
import com.foxminded.service.GroupService;
import com.foxminded.service.StudentService;
import com.foxminded.service.TeacherService;
import lombok.AllArgsConstructor;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final GroupService groupService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String showAll(Model model, @RequestParam(value = "searchText", required = false) String searchText) {
        List<StudentDto> allStudents = studentService.getAllStudents();

        if (searchText != null) {
            if (searchText.equals("")) {
                allStudents = allStudents.stream()
                        .filter(student -> student.groupDto() == null)
                        .toList();
            } else {
                allStudents = allStudents.stream()
                        .filter(student -> student.groupDto() != null)
                        .filter(student -> student.groupDto().name().startsWith(searchText))
                        .toList();
            }
        }

        model.addAttribute("allStudents", allStudents);
        return "entityPages/studentPage";
    }

    @GetMapping("/student-update/{studentId}")
    public String showUpdatePage(@PathVariable("studentId") Long studentId, Model model) {
        StudentDto student = studentService.getStudentById(studentId);
        List<GroupDto> allGroups = groupService.getAllGroups();
        List<CourseDto> allCourses = courseService.getAllCourses();
        List<String> studentCourses = student.courseDtoList().stream().map(CourseDto::name).toList();
        model.addAttribute("student", student);
        model.addAttribute("allGroups", allGroups);
        model.addAttribute("allCourses", allCourses);
        model.addAttribute("studentCourses", studentCourses);
        return "updatePages/updateStudentPage";
    }

    @PostMapping("/search-by-group")
    public String searchByGroup(@RequestParam("searchText") String searchText) {
        return "redirect:/students?searchText=" + searchText;
    }

    @PostMapping("/student-deletion/{studentId}")
    public String deleteSubject(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudentById(studentId);
        return "redirect:/students";
    }

    @PostMapping("/student-update")
    public String updateSubject(@RequestParam("studentId") Long studentId,
                                @RequestParam("name") String name,
                                @RequestParam("password") String password,
                                @RequestParam("group") Long groupId,
                                @RequestParam(value = "selectedCourses", required = false) String selectedCourses) {
        StudentDto oldStudent = studentService.getStudentById(studentId);

        if (!isNameAvailable(name, oldStudent.name())) {
            return "redirect:/students/student-update/" + studentId +"?error=true";
        }

        String encodedPass = password.equals("") ? oldStudent.password() : passwordEncoder.encode(password);
        GroupDto group = groupId != 0 ? groupService.getGroupById(groupId) : null;
        List<String> selectedCoursesList = selectedCourses != null ? Arrays.asList(selectedCourses.split(",")) : new ArrayList<>();
        List<CourseDto> courseDtoList = selectedCoursesList.stream().map(courseService::getCourseByName).toList();
        StudentDto updatedStudent = new StudentDto(studentId, name, encodedPass, oldStudent.roles(), group, courseDtoList);

        studentService.updateStudent(updatedStudent);
        return "redirect:/students";
    }

    private boolean isNameAvailable(String newName, String previousName) {
        if (newName.equals(previousName)) {
            return true;
        }

        Optional<StudentDto> existingStudent = studentService.getStudentByName(newName);
        Optional<TeacherDto> existingTeacher = teacherService.getTeacherByName(newName);

        return !(existingStudent.isPresent() || existingTeacher.isPresent());
    }
}

