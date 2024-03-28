package com.foxminded.controller;

import com.foxminded.dto.GroupDto;
import com.foxminded.dto.LessonDto;
import com.foxminded.dto.SubjectDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.dto.TimetableDto;
import com.foxminded.enums.TimetableType;
import com.foxminded.service.GroupService;
import com.foxminded.service.LessonService;
import com.foxminded.service.TeacherService;
import com.foxminded.service.TimetableService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timetables")
public class TimetableController {
    private final TimetableService timetableService;
    private final LessonService lessonService;
    private final TeacherService teacherService;
    private final GroupService groupService;

    @GetMapping
    public String showAll(Model model) {
        List<TimetableDto> allTimetables = timetableService.getAllTimetables();
        allTimetables.forEach(timetable -> sortLessonsByDayTime(timetable.lessonDtoList()));
        model.addAttribute("allTimetables", allTimetables);
        return "entityPages/timetablePage";
    }

    @GetMapping("/timetable-creation")
    public String showCreatePage(Model model) {
        model.addAttribute("allQualifyingNames", getAllQualifyingNames());
        return "createPages/createTimetablePage";
    }

    @GetMapping("/timetable-update/{timetableId}")
    public String showUpdatePage(@PathVariable("timetableId") Long timetableId, Model model) {
        TimetableDto timetable = timetableService.getTimetableById(timetableId);
        sortLessonsByDayTime(timetable.lessonDtoList());
        List<LessonDto> availableLessons = new ArrayList<>(lessonService.getAllLessons());
        sortLessonsByDayTime(availableLessons);

        if (timetable.timetableType() == TimetableType.STUDENT_TIMETABLE) {
            availableLessons = availableLessons.stream()
                    .filter(lesson -> lesson.subjectDto().groupDto().name().equals(timetable.qualifyingName()))
                    .toList();
        } else {
            availableLessons = availableLessons.stream()
                    .filter(lesson -> lesson.subjectDto().teacherDto().name().equals(timetable.qualifyingName()))
                    .toList();
        }

        model.addAttribute("timetable", timetable);
        model.addAttribute("availableLessons", availableLessons);
        return "updatePages/updateTimetablePage";
    }

    @PostMapping("timetable-creation")
    public String createTimetable(@RequestParam("qualifyingName") String qualifyingName) {
        List<String> existingQualNames = timetableService.getAllTimetables().stream().map(TimetableDto::qualifyingName).toList();
        if (existingQualNames.contains(qualifyingName)) {
            return "redirect:/timetables/timetable-creation?error=true";
        }

        TimetableDto newTimetable = new TimetableDto(0L, getTimetableTypeByQualName(qualifyingName),
                qualifyingName, new ArrayList<>());
        timetableService.addTimetable(newTimetable);
        return "redirect:/timetables";
    }

    @PostMapping("{timetableId}/lesson-addition")
    public String addLessonToTimetable(@PathVariable("timetableId") Long timetableId,
                                       @RequestParam("availableLessonId") Long availableLessonId) {
        TimetableDto timetable = timetableService.getTimetableById(timetableId);
        LessonDto selectedLesson = lessonService.getLessonById(availableLessonId);

        if (timetable.lessonDtoList().contains(selectedLesson)) {
            return "redirect:/timetables/timetable-update/" + timetableId + "?error=true";
        }

        timetable.lessonDtoList().add(selectedLesson);
        timetableService.updateTimetable(timetable);
        return "redirect:/timetables/timetable-update/" + timetableId;
    }

    @PostMapping("{timetableId}/lesson-deletion/{lessonId}")
    public String deleteLessonFromTimetable(@PathVariable("timetableId") Long timetableId,
                                            @PathVariable("lessonId") Long lessonId) {
        TimetableDto timetable = timetableService.getTimetableById(timetableId);
        LessonDto selectedLesson = lessonService.getLessonById(lessonId);

        timetable.lessonDtoList().remove(selectedLesson);
        timetableService.updateTimetable(timetable);
        return "redirect:/timetables/timetable-update/" + timetableId;
    }

    @PostMapping("timetable-deletion/{timetableId}")
    public String deleteTimetable(@PathVariable("timetableId") Long timetableId) {
        timetableService.deleteTimetableById(timetableId);
        return "redirect:/timetables";
    }

    private void sortLessonsByDayTime(List<LessonDto> lessons) {
        for (int i = 0; i < lessons.size(); i++) {
            for (int j = 1; j < lessons.size(); j++) {
                LessonDto lesson = lessons.get(j - 1);
                LessonDto nextLesson = lessons.get(j);
                if (nextLesson.day().getValue() < lesson.day().getValue()) {
                    lessons.set(j - 1, nextLesson);
                    lessons.set(j, lesson);
                } else if (nextLesson.day().getValue() == lesson.day().getValue()) {
                    if (nextLesson.appointmentTime().isBefore(lesson.appointmentTime())) {
                        lessons.set(j - 1, nextLesson);
                        lessons.set(j, lesson);
                    }
                }
            }
        }
    }

    private List<String> getAllQualifyingNames() {
        List<GroupDto> allGroups = groupService.getAllGroups();
        List<TeacherDto> allTeachers = teacherService.getAllTeachers();
        List<String> allGroupsQualNames = new ArrayList<>(allGroups.stream().map(GroupDto::name).toList());
        List<String> allTeachersQualNames = allTeachers.stream().map(TeacherDto::name).toList();
        return Stream.concat(allGroupsQualNames.stream(), allTeachersQualNames.stream()).toList();
    }

    private TimetableType getTimetableTypeByQualName(String qualifyingName) {
        List<TeacherDto> allTeachers = teacherService.getAllTeachers();

        for (var teacher : allTeachers) {
            if (teacher.name().equals(qualifyingName)) {
                return TimetableType.TEACHER_TIMETABLE;
            }
        }

        return TimetableType.STUDENT_TIMETABLE;
    }


}

