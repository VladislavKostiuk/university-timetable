package com.foxminded.service;


import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.GroupDTO;

import java.util.List;

public interface GroupService {
    void addGroup(GroupDTO groupDTO);
    GroupDTO getGroupById(Long id);
    void deleteGroupById(Long id);
    void updateGroup(GroupDTO groupDTO);
    List<GroupDTO> getAllGroups();
}
