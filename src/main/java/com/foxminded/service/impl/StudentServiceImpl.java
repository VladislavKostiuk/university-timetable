package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.StudentDTO;
import com.foxminded.mapper.StudentMapper;
import com.foxminded.entity.Student;
import com.foxminded.repository.StudentRepository;
import com.foxminded.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final String entityName = "Student";
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public void addStudent(StudentDTO studentDTO) {
        if (studentRepository.findById(studentDTO.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Student student = studentMapper.mapToStudent(studentDTO);
        studentRepository.save(student);
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND, entityName, id
                )));
        return studentMapper.mapToStudentDTO(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public void updateStudent(StudentDTO studentDTO) {
        if (studentRepository.findById(studentDTO.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Student student = studentMapper.mapToStudent(studentDTO);
        studentRepository.save(student);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        List<Student> allStudents = studentRepository.findAll();
        return allStudents.stream().map(studentMapper::mapToStudentDTO).toList();
    }
}
