package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.TimetableDTO;
import com.foxminded.mapper.TimetableMapper;
import com.foxminded.entity.Timetable;
import com.foxminded.repository.TimetableRepository;
import com.foxminded.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableServiceImpl implements TimetableService {
    private final String entityName = "Timetable";
    private final TimetableRepository timetableRepository;
    private final TimetableMapper timetableMapper;

    @Autowired
    public TimetableServiceImpl(TimetableRepository timetableRepository, TimetableMapper timetableMapper) {
        this.timetableRepository = timetableRepository;
        this.timetableMapper = timetableMapper;
    }

    @Override
    public void addTimetable(TimetableDTO timetableDTO) {
        if (timetableRepository.findById(timetableDTO.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Timetable timetable = timetableMapper.mapToTimetable(timetableDTO);
        timetableRepository.save(timetable);
    }

    @Override
    public TimetableDTO getTimetableById(Long id) {
        Timetable timetable = timetableRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND, entityName, id
                )));
        return timetableMapper.mapToTimetableDTO(timetable);
    }

    @Override
    public void deleteTimetableById(Long id) {
        timetableRepository.deleteById(id);
    }

    @Override
    public void updateTimetable(TimetableDTO timetableDTO) {
        if (timetableRepository.findById(timetableDTO.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Timetable timetable = timetableMapper.mapToTimetable(timetableDTO);
        timetableRepository.save(timetable);
    }

    @Override
    public List<TimetableDTO> getAllTimetables() {
        List<Timetable> timetables = timetableRepository.findAll();
        return timetables.stream().map(timetableMapper::mapToTimetableDTO).toList();
    }
}
