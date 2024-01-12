package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.GroupDTO;
import com.foxminded.mapper.GroupMapper;
import com.foxminded.entity.Group;
import com.foxminded.repository.GroupRepository;
import com.foxminded.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private final String entityName = "Group";
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository,
                            GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    @Override
    public void addGroup(GroupDTO groupDTO) {
        if (groupRepository.findById(groupDTO.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Group group = groupMapper.mapToGroup(groupDTO);
        groupRepository.save(group);
    }

    @Override
    public GroupDTO getGroupById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND, entityName, id
                )));
        return groupMapper.mapToGroupDTO(group);
    }

    @Override
    public void deleteGroupById(Long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public void updateGroup(GroupDTO groupDTO) {
        if (groupRepository.findById(groupDTO.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Group group = groupMapper.mapToGroup(groupDTO);
        groupRepository.save(group);
    }

    @Override
    public List<GroupDTO> getAllGroups() {
        List<Group> allGroups = groupRepository.findAll();
        return allGroups.stream().map(groupMapper::mapToGroupDTO).toList();
    }
}
