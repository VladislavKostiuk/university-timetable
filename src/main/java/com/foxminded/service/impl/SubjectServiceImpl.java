package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.SubjectDto;
import com.foxminded.mapper.SubjectMapper;
import com.foxminded.entity.Subject;
import com.foxminded.repository.SubjectRepository;
import com.foxminded.service.SubjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final String entityName = "Subject";
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Override
    public void addSubject(SubjectDto subjectDto) {
        if (subjectRepository.findById(subjectDto.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Subject subject = subjectMapper.mapToSubject(subjectDto);
        subjectRepository.save(subject);
    }

    @Override
    public SubjectDto getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND_BY_ID, entityName, id
                )));
        return subjectMapper.mapToSubjectDto(subject);
    }

    @Override
    public void deleteSubjectById(Long id) {
        subjectRepository.deleteById(id);
    }

    @Override
    public void updateSubject(SubjectDto subjectDto) {
        if (subjectRepository.findById(subjectDto.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Subject subject = subjectMapper.mapToSubject(subjectDto);
        subjectRepository.save(subject);
    }

    @Override
    public List<SubjectDto> getAllSubjects() {
        List<Subject> allSubjects = subjectRepository.findAll();
        return allSubjects.stream().map(subjectMapper::mapToSubjectDto).toList();
    }
}
