package com.foxminded.service;


import com.foxminded.dto.GroupDTO;

public interface GroupService {
    void addGroup(GroupDTO groupDTO);
    GroupDTO getGroupById(Long id);
    void deleteGroupById(Long id);
    void updateGroup(GroupDTO groupDTO);
}
