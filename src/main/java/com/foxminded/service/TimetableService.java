package com.foxminded.service;

import com.foxminded.dto.TimetableDTO;

public interface TimetableService {
    void addTimetable(TimetableDTO timetableDTO);
    TimetableDTO getTimetableById(Long id);
    void deleteTimetableById(Long id);
    void updateTimetable(TimetableDTO timetableDTO);
}
