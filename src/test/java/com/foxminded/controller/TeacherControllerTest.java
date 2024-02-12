package com.foxminded.controller;

import com.foxminded.dto.TeacherDTO;
import com.foxminded.service.TeacherService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TeacherController.class)
@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    private TeacherDTO testTeacherDTO;

    @BeforeEach
    void setup() {
        testTeacherDTO = new TeacherDTO(
                1L,
                "test teacher",
                null
        );
    }

    @Test
    void testShowAll_Success() throws Exception{
        List<TeacherDTO> expectedTeachers = new ArrayList<>(List.of(testTeacherDTO));
        given(teacherService.getAllTeachers()).willReturn(expectedTeachers);

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/teacherPage"))
                .andExpect(model().attribute("allTeachers", expectedTeachers));
    }
}
