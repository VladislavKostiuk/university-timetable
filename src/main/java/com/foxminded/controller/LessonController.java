package com.foxminded.controller;

import com.foxminded.dto.LessonDto;
import com.foxminded.dto.SubjectDto;
import com.foxminded.service.LessonService;
import com.foxminded.service.SubjectService;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService;
    private final SubjectService subjectService;

    @GetMapping
    public String showAll(Model model) {
        List<LessonDto> allLessons = lessonService.getAllLessons();
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

    @PostMapping("/lesson-creation")
    public String createLesson(@RequestParam("subject") Long subjectId,
                                @RequestParam("dayOfWeek") String dayOfWeek,
                                @RequestParam("time") String time) {
        SubjectDto subject = subjectService.getSubjectById(subjectId);
        LocalTime appointmentTime = stringToLocalTime(time);

        LessonDto newLesson = new LessonDto(0L, subject, DayOfWeek.valueOf(dayOfWeek), appointmentTime);

        if (checkIfLessonExists(newLesson)) {
            return "redirect:/lessons/lesson-creation?error=true";
        }

        lessonService.addLesson(newLesson);
        return "redirect:/lessons";
    }

    @PostMapping("/lesson-deletion/{lessonId}")
    public String deleteLesson(@PathVariable("lessonId") Long lessonId) {
        lessonService.deleteLessonById(lessonId);
        return "redirect:/lessons";
    }

    @PostMapping("/lesson-update")
    public String updateSubject(@RequestParam("lessonId") Long lessonId,
                                @RequestParam("subject") Long subjectId,
                                @RequestParam("dayOfWeek") String dayOfWeek,
                                @RequestParam("time") String time) {
        SubjectDto subject = subjectService.getSubjectById(subjectId);
        LocalTime appointmentTime = stringToLocalTime(time);

        LessonDto updatedLesson = new LessonDto(lessonId, subject, DayOfWeek.valueOf(dayOfWeek), appointmentTime);

        if (checkIfLessonExists(updatedLesson)) {
            return "redirect:/lessons/lesson-update/" + lessonId + "?error=true";
        }

        lessonService.updateLesson(updatedLesson);
        return "redirect:/lessons";
    }

    private void initModel(Model model) {
        List<SubjectDto> allSubjects = subjectService.getAllSubjects();
        List<DayOfWeek> allDaysOfWeek = Arrays.asList(DayOfWeek.values());
        model.addAttribute("allSubjects", allSubjects);
        model.addAttribute("allDaysOfWeek", allDaysOfWeek);
    }

    private LocalTime stringToLocalTime(String time) {
        String[] timeParts = time.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        return LocalTime.of(hours, minutes);
    }

    private boolean checkIfLessonExists(LessonDto newLesson) {
        for (var lesson : lessonService.getAllLessons()) {
            if (newLesson.subjectDto().equals(lesson.subjectDto()) &&
                    newLesson.day().equals(lesson.day()) &&
                    newLesson.appointmentTime().equals(lesson.appointmentTime())) {
                return true;
            }
        }

        return false;
    }
}
