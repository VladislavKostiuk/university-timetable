package com.foxminded.mapper;

import com.foxminded.dto.TimetableDTO;
import com.foxminded.entity.Timetable;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {LessonMapper.class})
public interface TimetableMapper {

    @Mapping(source = "timetable.lessons", target = "lessonDTOList")
    TimetableDTO mapToTimetableDTO(Timetable timetable);

    @Mapping(source = "timetableDTO.lessonDTOList", target = "lessons")
    Timetable mapToTimetable(TimetableDTO timetableDTO);


}
