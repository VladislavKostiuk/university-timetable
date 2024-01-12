package com.foxminded.controller;

import com.foxminded.dto.LessonDTO;
import com.foxminded.dto.StudentDTO;
import com.foxminded.service.LessonService;
import com.foxminded.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class)
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private StudentDTO testStudentDTO;

    @BeforeEach
    void setup() {
        testStudentDTO = new StudentDTO(
                1L,
                "test student",
                null,
                null
        );
    }

    @Test
    void testShowAll_Success() throws Exception{
        List<StudentDTO> expectedStudents = new ArrayList<>(List.of(testStudentDTO));
        given(studentService.getAllStudents()).willReturn(expectedStudents);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/studentPage"))
                .andExpect(model().attribute("allStudents", expectedStudents));
    }
}