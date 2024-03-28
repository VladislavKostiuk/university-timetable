package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.StudentDto;
import com.foxminded.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin-panel/courses")
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public String showAll(Model model, @RequestParam(value = "course-name", required = false) String courseName) {
        List<CourseDto> allCourses = courseService.getAllCourses();
        allCourses = allCourses.stream().sorted(Comparator.comparing(CourseDto::id)).toList();

        if (courseName != null) {
            allCourses = allCourses.stream()
                    .filter(course -> course.name().startsWith(courseName))
                    .toList();
        }

        model.addAttribute("allCourses", allCourses);
        return "entityPages/coursePage";
    }

    @GetMapping("/course-creation")
    public String showCreatePage() {
        return "createPages/createCoursePage";
    }

    @GetMapping("/course-update/{courseId}")
    public String showUpdatePage(@PathVariable("courseId") Long courseId, Model model) {
        CourseDto course = courseService.getCourseById(courseId);
        model.addAttribute("course", course);
        return "updatePages/updateCoursePage";
    }

    @PostMapping("/search")
    public String search(@RequestParam("courseName") String courseName) {
        return "redirect:/admin-panel/courses?course-name=" + courseName;
    }

    @PostMapping("/course-creation")
    public String createCourse(@RequestParam("name") String name,
                                @RequestParam("description") String description) {
        CourseDto newCourse = new CourseDto(0L, name, description);

        if (checkIfCourseExists(newCourse)) {
            return "redirect:/admin-panel/courses/course-creation?error=true";
        }
        courseService.addCourse(newCourse);

        return "redirect:/admin-panel/courses";
    }

    @PostMapping("/course-update")
    public String updateCourse(@RequestParam("courseId") Long courseId,
                               @RequestParam("name") String name,
                                @RequestParam("description") String description) {
        CourseDto updatedCourse = new CourseDto(courseId, name, description);

        if (checkIfCourseExists(updatedCourse)) {
            return "redirect:/admin-panel/courses/course-update/" + courseId + "?error=true";
        }

        courseService.updateCourse(updatedCourse);
        return "redirect:/admin-panel/courses";
    }

    @PostMapping("/course-deletion/{courseId}")
    public String deleteCourse(@PathVariable("courseId") Long courseId) {
        courseService.deleteCourseById(courseId);
        return "redirect:/admin-panel/courses";
    }

    private boolean checkIfCourseExists(CourseDto newCourse) {
        List<CourseDto> allCourses = courseService.getAllCourses();
        for (var course : allCourses) {
            if (course.name().equals(newCourse.name()) &&
            course.description().equals(newCourse.description())) {
                return true;
            }
        }

        return false;
    }
}

