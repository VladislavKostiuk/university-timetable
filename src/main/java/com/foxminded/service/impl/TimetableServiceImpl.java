package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.TimetableDto;
import com.foxminded.mapper.TimetableMapper;
import com.foxminded.entity.Timetable;
import com.foxminded.repository.TimetableRepository;
import com.foxminded.service.TimetableService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {
    private final String entityName = "Timetable";
    private final TimetableRepository timetableRepository;
    private final TimetableMapper timetableMapper;

    @Override
    public void addTimetable(TimetableDto timetableDto) {
        if (timetableRepository.findById(timetableDto.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Timetable timetable = timetableMapper.mapToTimetable(timetableDto);
        timetableRepository.save(timetable);
    }

    @Override
    public TimetableDto getTimetableById(Long id) {
        Timetable timetable = timetableRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND_BY_ID, entityName, id
                )));
        return timetableMapper.mapToTimetableDto(timetable);
    }

    @Override
    public void deleteTimetableById(Long id) {
        timetableRepository.deleteById(id);
    }

    @Override
    public void updateTimetable(TimetableDto timetableDto) {
        if (timetableRepository.findById(timetableDto.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Timetable timetable = timetableMapper.mapToTimetable(timetableDto);
        timetableRepository.save(timetable);
    }

    @Override
    public List<TimetableDto> getAllTimetables() {
        List<Timetable> timetables = timetableRepository.findAll();
        return timetables.stream().map(timetableMapper::mapToTimetableDto).toList();
    }
}
