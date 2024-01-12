package com.foxminded.service;

import com.foxminded.dto.TimetableDTO;

import java.util.List;

public interface TimetableService {
    void addTimetable(TimetableDTO timetableDTO);
    TimetableDTO getTimetableById(Long id);
    void deleteTimetableById(Long id);
    void updateTimetable(TimetableDTO timetableDTO);
    List<TimetableDTO> getAllTimetables();
}
