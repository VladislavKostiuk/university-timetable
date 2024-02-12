package com.foxminded.mapper;

import com.foxminded.dto.LessonDTO;
import com.foxminded.entity.Lesson;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SubjectMapper.class})
public interface LessonMapper {

    @Mapping(source = "lesson.subject", target = "subjectDTO")
    LessonDTO mapToLessonDTO(Lesson lesson);

    @Mapping(source = "lessonDTO.subjectDTO", target = "subject")
    Lesson mapToLesson(LessonDTO lessonDTO);
}
