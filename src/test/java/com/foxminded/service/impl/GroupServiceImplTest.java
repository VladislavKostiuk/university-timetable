package com.foxminded.service.impl;

import com.foxminded.dto.GroupDTO;
import com.foxminded.mapper.GroupMapper;
import com.foxminded.entity.Group;
import com.foxminded.mapper.GroupMapperImpl;
import com.foxminded.repository.GroupRepository;
import com.foxminded.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        GroupMapperImpl.class
})
class GroupServiceImplTest {

    private GroupService groupService;
    @Mock
    private GroupRepository groupRepository;
    @Autowired
    private GroupMapper groupMapper;

    @BeforeEach
    void init() {
        groupService = new GroupServiceImpl(groupRepository, groupMapper);
    }

    @Test
    void testAddGroup_Success() {
        GroupDTO testGroupDTO = new GroupDTO("group1");
        when(groupRepository.findById(testGroupDTO.id())).thenReturn(Optional.empty());
        groupService.addGroup(testGroupDTO);
        verify(groupRepository).save(any());
    }

    @Test
    void testAddGroup_GroupAlreadyExists() {
        GroupDTO testGroupDTO = new GroupDTO("group1");
        Group testGroup = groupMapper.mapToGroup(testGroupDTO);
        when(groupRepository.findById(testGroupDTO.id())).thenReturn(Optional.of(testGroup));
        assertThrows(IllegalStateException.class, () -> groupService.addGroup(testGroupDTO));
    }

    @Test
    void testGetGroupById_Success() {
        Long id = 1L;
        Group group = new Group();
        group.setId(id);
        group.setName("group1");

        when(groupRepository.findById(id)).thenReturn(Optional.of(group));
        GroupDTO expectedGroupDTO = groupMapper.mapToGroupDTO(group);
        GroupDTO actualGroupDTO = groupService.getGroupById(id);
        assertEquals(expectedGroupDTO, actualGroupDTO);
        verify(groupRepository).findById(id);
    }

    @Test
    void testGetGroupById_GroupWasNotFound() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> groupService.getGroupById(anyLong()));
    }

    @Test
    void testUpdateGroup_Success() {
        GroupDTO testGroupDTO = new GroupDTO("group1");
        Group testGroup = groupMapper.mapToGroup(testGroupDTO);
        when(groupRepository.findById(testGroupDTO.id())).thenReturn(Optional.of(testGroup));
        groupService.updateGroup(testGroupDTO);
        verify(groupRepository).save(any());
    }

    @Test
    void testUpdateGroup_GroupDoesNotExist() {
        GroupDTO testGroupDTO = new GroupDTO("group1");
        when(groupRepository.findById(testGroupDTO.id())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> groupService.updateGroup(testGroupDTO));
    }
}
