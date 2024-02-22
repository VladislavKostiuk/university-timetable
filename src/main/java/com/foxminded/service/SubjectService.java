package com.foxminded.service;

import com.foxminded.dto.SubjectDto;

import java.util.List;

public interface SubjectService {
    void addSubject(SubjectDto subjectDto);
    SubjectDto getSubjectById(Long id);
    void deleteSubjectById(Long id);
    void updateSubject(SubjectDto subjectDto);
    List<SubjectDto> getAllSubjects();
}
