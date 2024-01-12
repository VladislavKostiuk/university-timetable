package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.SubjectDTO;
import com.foxminded.mapper.SubjectMapper;
import com.foxminded.model.Subject;
import com.foxminded.repository.SubjectRepository;
import com.foxminded.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final String entityName = "Subject";
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public void addSubject(SubjectDTO subjectDTO) {
        if (subjectRepository.findById(subjectDTO.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Subject subject = subjectMapper.mapToSubject(subjectDTO);
        subjectRepository.save(subject);
    }

    @Override
    public SubjectDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND, entityName, id
                )));
        return subjectMapper.mapToSubjectDTO(subject);
    }

    @Override
    public void deleteSubjectById(Long id) {
        subjectRepository.deleteById(id);
    }

    @Override
    public void updateSubject(SubjectDTO subjectDTO) {
        if (subjectRepository.findById(subjectDTO.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Subject subject = subjectMapper.mapToSubject(subjectDTO);
        subjectRepository.save(subject);
    }
}