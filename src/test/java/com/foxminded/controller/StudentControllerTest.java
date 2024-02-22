package com.foxminded.controller;

import com.foxminded.dto.StudentDto;
import com.foxminded.enums.Role;
import com.foxminded.service.StudentService;
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

@WebMvcTest(controllers = StudentController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private StudentDto testStudentDto;

    @BeforeEach
    void setup() {
        testStudentDto = new StudentDto(
                0L,
                "test student",
                "some pass",
                Set.of(Role.STUDENT),
                null,
                new ArrayList<>()
        );
    }

    @Test
    void testShowAll_Success() throws Exception{
        List<StudentDto> expectedStudents = new ArrayList<>(List.of(testStudentDto));
        given(studentService.getAllStudents()).willReturn(expectedStudents);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/studentPage"))
                .andExpect(model().attribute("allStudents", expectedStudents));
    }
}

