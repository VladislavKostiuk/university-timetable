package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.GroupDto;
import com.foxminded.mapper.GroupMapper;
import com.foxminded.entity.Group;
import com.foxminded.repository.GroupRepository;
import com.foxminded.service.GroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final String entityName = "Group";
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    @Override
    public void addGroup(GroupDto groupDto) {
        if (groupRepository.findById(groupDto.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Group group = groupMapper.mapToGroup(groupDto);
        groupRepository.save(group);
    }

    @Override
    public GroupDto getGroupById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND_BY_ID, entityName, id
                )));
        return groupMapper.mapToGroupDto(group);
    }

    @Override
    public void deleteGroupById(Long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public void updateGroup(GroupDto groupDto) {
        if (groupRepository.findById(groupDto.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Group group = groupMapper.mapToGroup(groupDto);
        groupRepository.save(group);
    }

    @Override
    public List<GroupDto> getAllGroups() {
        List<Group> allGroups = groupRepository.findAll();
        return allGroups.stream().map(groupMapper::mapToGroupDto).toList();
    }
}
