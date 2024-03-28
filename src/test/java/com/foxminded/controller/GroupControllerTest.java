package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.TimetableDto;
import com.foxminded.enums.TimetableType;
import com.foxminded.service.GroupService;
import com.foxminded.service.TimetableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GroupController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser(authorities = {"ADMIN"})
@AutoConfigureMockMvc(addFilters = false)
class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private TimetableService timetableService;

    private GroupDto testGroupDto;

    @BeforeEach
    void setup() {
        testGroupDto = new GroupDto(
                1L,
                "test group"
        );
    }

    @Test
    void testShowAll_Success() throws Exception {
        List<GroupDto> expectedGroups = new ArrayList<>(List.of(testGroupDto));
        given(groupService.getAllGroups()).willReturn(expectedGroups);

        mockMvc.perform(get("/admin-panel/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/groupPage"))
                .andExpect(model().attribute("allGroups", expectedGroups));
    }

    @Test
    void testShowCreatePage_Success() throws Exception {

        mockMvc.perform(get("/admin-panel/groups/group-creation"))
                .andExpect(status().isOk())
                .andExpect(view().name("createPages/createGroupPage"));
    }

    @Test
    void testShowUpdatePage_Success() throws Exception {
        given(groupService.getGroupById(1L)).willReturn(testGroupDto);
        mockMvc.perform(get("/admin-panel/groups/group-update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("updatePages/updateGroupPage"))
                .andExpect(model().attribute("group", testGroupDto));
    }

    @Test
    void testSearch_Success() throws Exception {
        mockMvc.perform(post("/admin-panel/groups/search")
                        .param("groupName", "some group"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel/groups?group-name=some group"));
    }

    @Test
    void testCreateGroup_Success() throws Exception {
        given(groupService.getAllGroups()).willReturn(List.of(testGroupDto));
        mockMvc.perform(post("/admin-panel/groups/group-creation")
                        .param("name", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel/groups"));
        verify(groupService, times(1)).addGroup(any(GroupDto.class));
    }

    @Test
    void testCreateGroup_CourseWithTheseParamsAlreadyExists() throws Exception {
        given(groupService.getAllGroups()).willReturn(List.of(testGroupDto));
        mockMvc.perform(post("/admin-panel/groups/group-creation")
                        .param("name", "test group"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel/groups/group-creation?error=true"));
        verify(groupService, times(0)).addGroup(any(GroupDto.class));
    }

    @Test
    void testDeleteGroup_Success() throws Exception {
        mockMvc.perform(post("/admin-panel/groups/group-deletion/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel/groups"));
        verify(groupService, times(1)).deleteGroupById(1L);
    }

    @Test
    void testUpdateGroup_Success() throws Exception {
        TimetableDto testTimetableDto = new TimetableDto(1L, TimetableType.STUDENT_TIMETABLE,
                testGroupDto.name(), new ArrayList<>());

        given(groupService.getAllGroups()).willReturn(List.of(testGroupDto));
        given(groupService.getGroupById(1L)).willReturn(testGroupDto);
        given(timetableService.getTimetableByQualifyingName(testGroupDto.name())).willReturn(testTimetableDto);
        mockMvc.perform(post("/admin-panel/groups/group-update")
                        .param("groupId", "1")
                        .param("name", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel/groups"));
        verify(groupService, times(1)).updateGroup(any(GroupDto.class));
    }

    @Test
    void testUpdateCourse_CourseWithTheseParamsAlreadyExists() throws Exception {
        given(groupService.getAllGroups()).willReturn(List.of(testGroupDto));
        given(groupService.getGroupById(1L)).willReturn(testGroupDto);
        mockMvc.perform(post("/admin-panel/groups/group-update")
                        .param("groupId", "1")
                        .param("name", "test group"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel/groups/group-update/1?error=true"));
        verify(groupService, times(0)).updateGroup(any(GroupDto.class));
    }
}

