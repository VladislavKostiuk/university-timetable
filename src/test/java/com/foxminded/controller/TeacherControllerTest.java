package com.foxminded.controller;

import com.foxminded.dto.TeacherDto;
import com.foxminded.enums.Role;
import com.foxminded.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TeacherController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    private TeacherDto testTeacherDto;

    @BeforeEach
    void setup() {
        testTeacherDto = new TeacherDto(
                0L,
                "test teacher",
                "some pass",
                Set.of(Role.TEACHER),
                new ArrayList<>()
        );
    }

    @Test
    void testShowAll_Success() throws Exception{
        List<TeacherDto> expectedTeachers = new ArrayList<>(List.of(testTeacherDto));
        given(teacherService.getAllTeachers()).willReturn(expectedTeachers);

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/teacherPage"))
                .andExpect(model().attribute("allTeachers", expectedTeachers));
    }
}

