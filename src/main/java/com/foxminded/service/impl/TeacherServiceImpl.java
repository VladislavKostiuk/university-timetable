package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.TeacherDTO;
import com.foxminded.mapper.TeacherMapper;
import com.foxminded.entity.Teacher;
import com.foxminded.repository.TeacherRepository;
import com.foxminded.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final String entityName = "Teacher";
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public void addTeacher(TeacherDTO teacherDTO) {
        if (teacherRepository.findById(teacherDTO.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Teacher teacher = teacherMapper.mapToTeacher(teacherDTO);
        teacherRepository.save(teacher);
    }

    @Override
    public TeacherDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND, entityName, id
                )));
        return teacherMapper.mapToTeacherDTO(teacher);
    }

    @Override
    public void deleteTeacherById(Long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public void updateTeacher(TeacherDTO teacherDTO) {
        if (teacherRepository.findById(teacherDTO.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Teacher teacher = teacherMapper.mapToTeacher(teacherDTO);
        teacherRepository.save(teacher);
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        List<Teacher> allTeachers = teacherRepository.findAll();
        return allTeachers.stream().map(teacherMapper::mapToTeacherDTO).toList();
    }
}
