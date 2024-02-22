package com.foxminded.mapper;

import com.foxminded.dto.LessonDto;
import com.foxminded.entity.Lesson;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SubjectMapper.class})
public interface LessonMapper {

    @Mapping(source = "lesson.subject", target = "subjectDto")
    @Mapping(source = "lesson.timetables", target = "timetableDtoList")
    LessonDto mapToLessonDto(Lesson lesson);

    @Mapping(source = "lessonDto.subjectDto", target = "subject")
    @Mapping(source = "lessonDto.timetableDtoList", target = "timetables")
    Lesson mapToLesson(LessonDto lessonDto);
}
