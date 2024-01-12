package com.foxminded.dto;

public record SubjectDTO (
        Long id,
        CourseDTO courseDTO,
        TeacherDTO teacherDTO,
        GroupDTO groupDTO
) {
    public SubjectDTO(CourseDTO courseDTO, TeacherDTO teacherDTO, GroupDTO groupDTO) {
        this(0L, courseDTO, teacherDTO, groupDTO);
    }
}
