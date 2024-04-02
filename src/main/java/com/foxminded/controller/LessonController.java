package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.LessonDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.service.CourseService;
import com.foxminded.service.GroupService;
import com.foxminded.service.LessonService;
import com.foxminded.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin-panel/lessons")
public class LessonController {
    private final LessonService lessonService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final GroupService groupService;

    @GetMapping
    public String showAll(Model model, @RequestParam(value = "course-name", required = false) String courseName,
                          @RequestParam(value = "group-name", required = false) String groupName,
                          @RequestParam(value = "teacher-name", required = false) String teacherName) {
        List<LessonDto> allLessons = lessonService.getAllLessons();
        allLessons = allLessons.stream().sorted(Comparator.comparing(LessonDto::id)).toList();

        if (courseName != null) {
            allLessons = allLessons.stream()
                    .filter(lesson -> lesson.courseDto().name().startsWith(courseName))
                    .toList();
        }

        if (groupName != null) {
            allLessons = allLessons.stream()
                    .filter(lesson -> lesson.groupDto().name().startsWith(groupName))
                    .toList();
        }

        if (teacherName != null) {
            allLessons = allLessons.stream()
                    .filter(lesson -> lesson.teacherDto().name().startsWith(teacherName))
                    .toList();
        }

        model.addAttribute("allLessons", allLessons);
        return "entityPages/lessonPage";
    }

    @GetMapping("/lesson-creation")
    public String showCreatePage(Model model) {
        initModel(model);
        return "createPages/createLessonPage";
    }

    @GetMapping("/lesson-update/{lessonId}")
    public String showUpdatePage(@PathVariable("lessonId") Long lessonId, Model model) {
        LessonDto lesson = lessonService.getLessonById(lessonId);
        model.addAttribute("lesson", lesson);
        initModel(model);
        return "updatePages/updateLessonPage";
    }

    @PostMapping("/search")
    public String search(@RequestParam("courseName") String courseName,
                                @RequestParam("groupName") String groupName,
                                @RequestParam("teacherName") String teacherName) {
        return "redirect:/admin-panel/lessons?course-name=" + courseName + "&group-name=" + groupName + "&teacher-name=" + teacherName;
    }

    @PostMapping("/lesson-creation")
    public String createLesson(@RequestParam("course") Long courseId,
                               @RequestParam("teacher") Long teacherId,
                               @RequestParam("group") Long groupId,
                                @RequestParam("dayOfWeek") String dayOfWeek,
                                @RequestParam("time") String time) {
        CourseDto course = courseService.getCourseById(courseId);
        TeacherDto teacher = teacherService.getTeacherById(teacherId);
        GroupDto group = groupService.getGroupById(groupId);
        LocalTime appointmentTime = stringToLocalTime(time);

        LessonDto newLesson = new LessonDto(0L, course, teacher, group, DayOfWeek.valueOf(dayOfWeek), appointmentTime);

        if (checkIfLessonExists(newLesson)) {
            return "redirect:/admin-panel/lessons/lesson-creation?error=true";
        }

        lessonService.addLesson(newLesson);
        return "redirect:/admin-panel/lessons";
    }

    @PostMapping("/lesson-deletion/{lessonId}")
    public String deleteLesson(@PathVariable("lessonId") Long lessonId) {
        lessonService.deleteLessonById(lessonId);
        return "redirect:/admin-panel/lessons";
    }

    @PostMapping("/lesson-update")
    public String updateSubject(@RequestParam("lessonId") Long lessonId,
                                @RequestParam("course") Long courseId,
                                @RequestParam("teacher") Long teacherId,
                                @RequestParam("group") Long groupId,
                                @RequestParam("dayOfWeek") String dayOfWeek,
                                @RequestParam("time") String time) {
        CourseDto course = courseService.getCourseById(courseId);
        TeacherDto teacher = teacherService.getTeacherById(teacherId);
        GroupDto group = groupService.getGroupById(groupId);
        LocalTime appointmentTime = stringToLocalTime(time);

        LessonDto updatedLesson = new LessonDto(lessonId, course, teacher, group, DayOfWeek.valueOf(dayOfWeek), appointmentTime);

        if (checkIfLessonExists(updatedLesson)) {
            return "redirect:/admin-panel/lessons/lesson-update/" + lessonId + "?error=true";
        }

        lessonService.updateLesson(updatedLesson);
        return "redirect:/admin-panel/lessons";
    }

    private void initModel(Model model) {
        List<CourseDto> allCourses = courseService.getAllCourses();
        List<TeacherDto> allTeachers = teacherService.getAllTeachers();
        List<GroupDto> allGroups = groupService.getAllGroups();
        List<DayOfWeek> allDaysOfWeek = Arrays.asList(DayOfWeek.values());
        List<String> timeOptions = List.of("8:30", "10:25", "12:20", "14:15", "16:10");

        model.addAttribute("allCourses", allCourses);
        model.addAttribute("allTeachers", allTeachers);
        model.addAttribute("allGroups", allGroups);
        model.addAttribute("allDaysOfWeek", allDaysOfWeek);
        model.addAttribute("timeOptions", timeOptions);
    }

    private LocalTime stringToLocalTime(String time) {
        String[] timeParts = time.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        return LocalTime.of(hours, minutes);
    }

    private boolean checkIfLessonExists(LessonDto newLesson) {
        for (var lesson : lessonService.getAllLessons()) {
            if (newLesson.courseDto().equals(lesson.courseDto()) &&
                    newLesson.groupDto().equals(lesson.groupDto()) &&
                    newLesson.teacherDto().equals(lesson.teacherDto()) &&
                    newLesson.day().equals(lesson.day()) &&
                    newLesson.appointmentTime().equals(lesson.appointmentTime())) {
                return true;
            }
        }

        return false;
    }
}
