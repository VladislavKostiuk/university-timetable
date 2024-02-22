package com.foxminded.service;


import com.foxminded.dto.GroupDto;

import java.util.List;

public interface GroupService {
    void addGroup(GroupDto groupDto);
    GroupDto getGroupById(Long id);
    void deleteGroupById(Long id);
    void updateGroup(GroupDto groupDto);
    List<GroupDto> getAllGroups();
}
