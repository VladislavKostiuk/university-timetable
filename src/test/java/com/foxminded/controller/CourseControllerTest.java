package com.foxminded.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.CourseDTO;
import com.foxminded.entity.Course;
import com.foxminded.enums.CourseName;
import com.foxminded.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class)
@ExtendWith(MockitoExtension.class)
class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private CourseDTO testCourseDTO;

    @BeforeEach
    void setup() {
        testCourseDTO = new CourseDTO(
                CourseName.MEDICINE,
                "desc"
        );
    }

    @Test
    void testShowAll_Success() throws Exception{
        List<CourseDTO> expectedCourses = new ArrayList<>(List.of(testCourseDTO));
        given(courseService.getAllCourses()).willReturn(expectedCourses);

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/coursePage"))
                .andExpect(model().attribute("allCourses", expectedCourses));
    }
}