package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.enums.CourseName;
import com.foxminded.service.CourseService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser
class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private CourseDto testCourseDto;

    @BeforeEach
    void setup() {
        testCourseDto = new CourseDto(
                0L,
                CourseName.MEDICINE,
                "desc"
        );
    }

    @Test
    void testShowAll_Success() throws Exception{
        List<CourseDto> expectedCourses = new ArrayList<>(List.of(testCourseDto));
        given(courseService.getAllCourses()).willReturn(expectedCourses);

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/coursePage"))
                .andExpect(model().attribute("allCourses", expectedCourses));
    }
}

