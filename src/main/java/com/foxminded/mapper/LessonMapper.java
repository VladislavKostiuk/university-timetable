package com.foxminded.mapper;

import com.foxminded.dto.LessonDTO;
import com.foxminded.model.Lesson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    LessonDTO mapToLessonDTO(Lesson lesson);
    Lesson mapToLesson(LessonDTO lessonDTO);
}
