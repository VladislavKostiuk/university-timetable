package com.foxminded.service;

import com.foxminded.dto.StudentDTO;
import com.foxminded.dto.SubjectDTO;

import java.util.List;

public interface SubjectService {
    void addSubject(SubjectDTO subjectDTO);
    SubjectDTO getSubjectById(Long id);
    void deleteSubjectById(Long id);
    void updateSubject(SubjectDTO subjectDTO);
    List<SubjectDTO> getAllSubjects();
}
