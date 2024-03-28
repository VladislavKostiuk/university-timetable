package com.foxminded.service;

import com.foxminded.dto.LessonDto;
import com.foxminded.dto.TimetableDto;

import java.util.List;

public interface TimetableService {
    void addTimetable(TimetableDto timetableDto);
    TimetableDto getTimetableById(Long id);
    TimetableDto getTimetableByQualifyingName(String qualifyingName);
    void deleteTimetableById(Long id);
    void updateTimetable(TimetableDto timetableDto);
    List<TimetableDto> getAllTimetables();
}
