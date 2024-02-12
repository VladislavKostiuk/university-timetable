package com.foxminded.controller;

import com.foxminded.dto.LessonDTO;
import com.foxminded.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LessonController.class)
@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    private LessonDTO testLessonDTO;

    @BeforeEach
    void setup() {

        testLessonDTO = new LessonDTO(
                null,
                DayOfWeek.MONDAY,
                LocalTime.now()
        );
    }

    @Test
    void testShowAll_Success() throws Exception{
        List<LessonDTO> expectedLessons = new ArrayList<>(List.of(testLessonDTO));
        given(lessonService.getAllLessons()).willReturn(expectedLessons);

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/lessonPage"))
                .andExpect(model().attribute("allLessons", expectedLessons));
    }
}
