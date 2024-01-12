package com.foxminded.service;

import com.foxminded.dto.SubjectDTO;

public interface SubjectService {
    void addSubject(SubjectDTO subjectDTO);
    SubjectDTO getSubjectById(Long id);
    void deleteSubjectById(Long id);
    void updateSubject(SubjectDTO subjectDTO);
}
