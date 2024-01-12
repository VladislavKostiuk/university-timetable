package com.foxminded.mapper;

import com.foxminded.dto.TimetableDTO;
import com.foxminded.model.Timetable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TimetableMapper {
    TimetableDTO mapToTimetableDTO(Timetable timetable);
    Timetable mapToTimetable(TimetableDTO timetableDTO);
}
