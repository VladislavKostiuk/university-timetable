package com.foxminded.controller;

import com.foxminded.dto.GroupDto;
import com.foxminded.dto.LessonDto;
import com.foxminded.dto.StudentDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.dto.TimetableDto;
import com.foxminded.dto.TimetableRowDto;
import com.foxminded.enums.TimetableType;
import com.foxminded.service.StudentService;
import com.foxminded.service.TeacherService;
import com.foxminded.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserTimetableController {
    private final TimetableService timetableService;
    private final TeacherService teacherService;
    private final StudentService studentService;

    @GetMapping("/timetable")
    public String showUserTimetable(Model model) {
        String qualifyingName = defineQualifyingName();
        List<TimetableRowDto> timetableRows = null;
        LocalTime lessonTime = LocalTime.of(8, 30);

        if (qualifyingName != null) {
            timetableRows = new ArrayList<>(5);
            TimetableDto timetable = timetableService.getTimetableByQualifyingName(qualifyingName);

            if (timetable.timetableType() == TimetableType.STUDENT_TIMETABLE) {
                qualifyingName = "group " + qualifyingName;
            }

            List<LessonDto> lessons = timetable.lessonDtoList();
            for (int i = 0; i < 5; i++) {
                LessonDto[] lessonsByTime = getLessonsByDayTime(lessons, lessonTime);
                TimetableRowDto timetableRow = new TimetableRowDto(lessonTime, lessonsByTime[0],
                        lessonsByTime[1], lessonsByTime[2], lessonsByTime[3], lessonsByTime[4], lessonsByTime[5]);
                timetableRows.add(timetableRow);

                lessonTime = lessonTime.plusHours(1).plusMinutes(55);
            }
        }

        model.addAttribute("timetableRows", timetableRows);
        model.addAttribute("qualifyingName", qualifyingName);
        return "userTimetablePage";
    }

    private String defineQualifyingName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<StudentDto> student = studentService.getStudentByName(userName);
        Optional<TeacherDto> teacher = teacherService.getTeacherByName(userName);

        if (student.isPresent()) {
            GroupDto groupDto = student.get().groupDto();
            return groupDto != null ? groupDto.name() : null;
        } else {
            return teacher.get().name();
        }
    }

    private LessonDto[] getLessonsByDayTime(List<LessonDto> lessons, LocalTime time) {
        LessonDto[] result = new LessonDto[6];

        for (int i = 0; i < result.length; i++) {
            int dayNumber = i + 1;
            List<LessonDto> foundLesson = lessons.stream()
                    .filter(lesson -> lesson.day() == DayOfWeek.of(dayNumber))
                    .filter(lesson -> lesson.appointmentTime().equals(time))
                    .toList();

            result[i] = foundLesson.isEmpty() ? null : foundLesson.get(0);
        }

        return result;
    }
}
