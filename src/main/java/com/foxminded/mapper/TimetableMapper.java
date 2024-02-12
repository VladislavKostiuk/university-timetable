package com.foxminded.mapper;

import com.foxminded.dto.TimetableDto;
import com.foxminded.entity.Timetable;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {LessonMapper.class})
public interface TimetableMapper {

    @Mapping(source = "timetable.lessons", target = "lessonDtoList")
    TimetableDto mapToTimetableDto(Timetable timetable);

    @Mapping(source = "timetableDto.lessonDtoList", target = "lessons")
    Timetable mapToTimetable(TimetableDto timetableDto);


}
