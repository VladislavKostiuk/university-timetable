package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.StudentDto;
import com.foxminded.mapper.StudentMapper;
import com.foxminded.entity.Student;
import com.foxminded.repository.StudentRepository;
import com.foxminded.service.StudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final String entityName = "Student";
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public void addStudent(StudentDto studentDto) {
        if (studentRepository.findById(studentDto.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Student student = studentMapper.mapToStudent(studentDto);
        studentRepository.save(student);
    }

    @Override
    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND_BY_ID, entityName, id
                )));
        return studentMapper.mapToStudentDto(student);
    }

    @Override
    public Optional<StudentDto> getStudentByName(String name) {
        Optional<StudentDto> studentDto = Optional.empty();
        Optional<Student> student = studentRepository.findByName(name);

        if (student.isPresent()) {
            studentDto = Optional.of(studentMapper.mapToStudentDto(student.get()));
        }

        return studentDto;
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public void updateStudent(StudentDto studentDto) {
        if (studentRepository.findById(studentDto.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Student student = studentMapper.mapToStudent(studentDto);
        studentRepository.save(student);
    }

    @Override
    public List<StudentDto> getAllStudents() {
        List<Student> allStudents = studentRepository.findAll();
        return allStudents.stream().map(studentMapper::mapToStudentDto).toList();
    }
}
