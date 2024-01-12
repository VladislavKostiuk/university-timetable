package com.foxminded.mapper;

import com.foxminded.dto.SubjectDTO;
import com.foxminded.model.Subject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectDTO mapToSubjectDTO(Subject subject);
    Subject mapToSubject(SubjectDTO subjectDTO);
}
