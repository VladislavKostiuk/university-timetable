package com.foxminded.controller;

import com.foxminded.dto.GroupDTO;
import com.foxminded.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GroupController.class)
@ExtendWith(MockitoExtension.class)
class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    private GroupDTO testGroupDTO;

    @BeforeEach
    void setup() {
        testGroupDTO = new GroupDTO(
                1L,
                "test group"
        );
    }

    @Test
    void testShowAll_Success() throws Exception {
        List<GroupDTO> expectedGroups = new ArrayList<>(List.of(testGroupDTO));
        given(groupService.getAllGroups()).willReturn(expectedGroups);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/groupPage"))
                .andExpect(model().attribute("allGroups", expectedGroups));
    }
}