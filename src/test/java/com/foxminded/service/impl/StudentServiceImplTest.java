package com.foxminded.service.impl;

import com.foxminded.dto.StudentDto;
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
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        StudentDto testStudentDto = new StudentDto(0L, "name", "pass", new HashSet<>(), null, new ArrayList<>());
        when(studentRepository.findById(testStudentDto.id())).thenReturn(Optional.empty());
        studentService.addStudent(testStudentDto);
        verify(studentRepository).save(any());
    }

    @Test
    void testAddStudent_StudentAlreadyExists() {
        StudentDto testStudentDto = new StudentDto(0L, "name", "pass", new HashSet<>(), null, new ArrayList<>());
        Student testStudent = studentMapper.mapToStudent(testStudentDto);
        when(studentRepository.findById(testStudentDto.id())).thenReturn(Optional.of(testStudent));
        assertThrows(IllegalStateException.class, () -> studentService.addStudent(testStudentDto));
    }

    @Test
    void testGetStudentById_Success() {
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        student.setName("name");

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        StudentDto expectedStudentDto = studentMapper.mapToStudentDto(student);
        StudentDto actualStudentDto = studentService.getStudentById(id);
        assertEquals(expectedStudentDto, actualStudentDto);
        verify(studentRepository).findById(id);
    }

    @Test
    void testGetStudentById_StudentWasNotFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentById(anyLong()));
    }

    @Test
    void testGetStudentByName_Success() {
        StudentDto testStudentDto = new StudentDto(0L, "name", "pass", new HashSet<>(), null, new ArrayList<>());
        Student testStudent = studentMapper.mapToStudent(testStudentDto);
        when(studentRepository.findByName("name")).thenReturn(Optional.of(testStudent));
        assertEquals(Optional.of(testStudentDto), studentService.getStudentByName("name"));
    }

    @Test
    void testGetStudentByName_StudentWithThatNameWasNotFound() {
        when(studentRepository.findByName("name")).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), studentService.getStudentByName("name"));
    }

    @Test
    void testUpdateStudent_Success() {
        StudentDto testStudentDto = new StudentDto(0L, "name", "pass", new HashSet<>(), null, new ArrayList<>());
        Student testStudent = studentMapper.mapToStudent(testStudentDto);
        when(studentRepository.findById(testStudentDto.id())).thenReturn(Optional.of(testStudent));
        studentService.updateStudent(testStudentDto);
        verify(studentRepository).save(any());
    }

    @Test
    void testUpdateStudent_StudentDoesNotExist() {
        StudentDto testStudentDto = new StudentDto(0L, "name", "pass", new HashSet<>(), null, new ArrayList<>());
        when(studentRepository.findById(testStudentDto.id())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> studentService.updateStudent(testStudentDto));
    }
}

