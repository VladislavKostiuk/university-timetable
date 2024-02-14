package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.TeacherDto;
import com.foxminded.mapper.TeacherMapper;
import com.foxminded.entity.Teacher;
import com.foxminded.repository.TeacherRepository;
import com.foxminded.service.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final String entityName = "Teacher";
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Override
    public void addTeacher(TeacherDto teacherDto) {
        if (teacherRepository.findById(teacherDto.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Teacher teacher = teacherMapper.mapToTeacher(teacherDto);
        teacherRepository.save(teacher);
    }

    @Override
    public TeacherDto getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND_BY_ID, entityName, id
                )));
        return teacherMapper.mapToTeacherDto(teacher);
    }

    @Override
    public Optional<TeacherDto> getTeacherByName(String name) {
        Optional<TeacherDto> teacherDto = Optional.empty();
        Optional<Teacher> teacher = teacherRepository.findByName(name);

        if (teacher.isPresent()) {
            teacherDto = Optional.of(teacherMapper.mapToTeacherDto(teacher.get()));
        }

        return teacherDto;
    }

    @Override
    public void deleteTeacherById(Long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public void updateTeacher(TeacherDto teacherDto) {
        if (teacherRepository.findById(teacherDto.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Teacher teacher = teacherMapper.mapToTeacher(teacherDto);
        teacherRepository.save(teacher);
    }

    @Override
    public List<TeacherDto> getAllTeachers() {
        List<Teacher> allTeachers = teacherRepository.findAll();
        return allTeachers.stream().map(teacherMapper::mapToTeacherDto).toList();
    }
}
