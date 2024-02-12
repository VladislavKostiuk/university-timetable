package com.foxminded.service.impl;

import com.foxminded.dto.SubjectDto;
import com.foxminded.mapper.SubjectMapper;
import com.foxminded.entity.Subject;
import com.foxminded.mapper.SubjectMapperImpl;
import com.foxminded.repository.SubjectRepository;
import com.foxminded.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        SubjectMapperImpl.class
})
class SubjectServiceImplTest {

    private SubjectService subjectService;
    @Mock
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectMapper subjectMapper;

    @BeforeEach
    void init() {
        subjectService = new SubjectServiceImpl(subjectRepository, subjectMapper);
    }

    @Test
    void testAddSubject_Success() {
        SubjectDto testSubjectDto = new SubjectDto(0L, null, null, null);
        when(subjectRepository.findById(testSubjectDto.id())).thenReturn(Optional.empty());
        subjectService.addSubject(testSubjectDto);
        verify(subjectRepository).save(any());
    }

    @Test
    void testAddSubject_SubjectAlreadyExists() {
        SubjectDto testSubjectDto = new SubjectDto(0L, null, null, null);
        Subject testSubject = subjectMapper.mapToSubject(testSubjectDto);
        when(subjectRepository.findById(testSubjectDto.id())).thenReturn(Optional.of(testSubject));
        assertThrows(IllegalStateException.class, () -> subjectService.addSubject(testSubjectDto));
    }

    @Test
    void testGetSubjectById_Success() {
        Long id = 1L;
        Subject subject = new Subject();
        subject.setId(id);

        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        SubjectDto expectedSubjectDto = subjectMapper.mapToSubjectDto(subject);
        SubjectDto actualSubjectDto = subjectService.getSubjectById(id);
        assertEquals(expectedSubjectDto, actualSubjectDto);
        verify(subjectRepository).findById(id);
    }

    @Test
    void testGetSubjectById_SubjectWasNotFound() {
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> subjectService.getSubjectById(anyLong()));
    }

    @Test
    void testUpdateSubject_Success() {
        SubjectDto testSubjectDto = new SubjectDto(0L, null, null, null);
        Subject testSubject = subjectMapper.mapToSubject(testSubjectDto);
        when(subjectRepository.findById(testSubjectDto.id())).thenReturn(Optional.of(testSubject));
        subjectService.updateSubject(testSubjectDto);
        verify(subjectRepository).save(any());
    }

    @Test
    void testUpdateSubject_SubjectDoesNotExist() {
        SubjectDto testSubjectDto = new SubjectDto(0L, null, null, null);
        when(subjectRepository.findById(testSubjectDto.id())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> subjectService.updateSubject(testSubjectDto));
    }
}

