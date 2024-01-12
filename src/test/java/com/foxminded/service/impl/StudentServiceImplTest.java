package com.foxminded.service.impl;

import com.foxminded.dto.StudentDTO;
import com.foxminded.mapper.StudentMapper;
import com.foxminded.entity.Student;
import com.foxminded.mapper.StudentMapperImpl;
import com.foxminded.repository.StudentRepository;
import com.foxminded.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        StudentMapperImpl.class
})
class StudentServiceImplTest {

    private StudentService studentService;
    @Mock
    private StudentRepository studentRepository;
    @Autowired
    private StudentMapper studentMapper;

    @BeforeEach
    void init() {
        studentService = new StudentServiceImpl(studentRepository, studentMapper);
    }

    @Test
    void testAddStudent_Success() {
        StudentDTO testStudentDTO = new StudentDTO("name", null, new ArrayList<>());
        when(studentRepository.findById(testStudentDTO.id())).thenReturn(Optional.empty());
        studentService.addStudent(testStudentDTO);
        verify(studentRepository).save(any());
    }

    @Test
    void testAddStudent_StudentAlreadyExists() {
        StudentDTO testStudentDTO = new StudentDTO("name", null, new ArrayList<>());
        Student testStudent = studentMapper.mapToStudent(testStudentDTO);
        when(studentRepository.findById(testStudentDTO.id())).thenReturn(Optional.of(testStudent));
        assertThrows(IllegalStateException.class, () -> studentService.addStudent(testStudentDTO));
    }

    @Test
    void testGetStudentById_Success() {
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        student.setName("name");

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        StudentDTO expectedStudentDTO = studentMapper.mapToStudentDTO(student);
        StudentDTO actualStudentDTO = studentService.getStudentById(id);
        assertEquals(expectedStudentDTO, actualStudentDTO);
        verify(studentRepository).findById(id);
    }

    @Test
    void testGetStudentById_StudentWasNotFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentById(anyLong()));
    }

    @Test
    void testUpdateStudent_Success() {
        StudentDTO testStudentDTO = new StudentDTO("name", null, new ArrayList<>());
        Student testStudent = studentMapper.mapToStudent(testStudentDTO);
        when(studentRepository.findById(testStudentDTO.id())).thenReturn(Optional.of(testStudent));
        studentService.updateStudent(testStudentDTO);
        verify(studentRepository).save(any());
    }

    @Test
    void testUpdateStudent_StudentDoesNotExist() {
        StudentDTO testStudentDTO = new StudentDTO("name", null, new ArrayList<>());
        when(studentRepository.findById(testStudentDTO.id())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> studentService.updateStudent(testStudentDTO));
    }
}