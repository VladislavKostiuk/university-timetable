package com.foxminded.mapper;

import com.foxminded.dto.LessonDto;
import com.foxminded.entity.Lesson;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(source = "lesson.course", target = "courseDto")
    @Mapping(source = "lesson.teacher", target = "teacherDto")
    @Mapping(source = "lesson.group", target = "groupDto")
    LessonDto mapToLessonDto(Lesson lesson);

    @Mapping(source = "lessonDto.courseDto", target = "course")
    @Mapping(source = "lessonDto.teacherDto", target = "teacher")
    @Mapping(source = "lessonDto.groupDto", target = "group")
    Lesson mapToLesson(LessonDto lessonDto);
}
