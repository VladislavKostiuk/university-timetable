package com.foxminded.controller;

import com.foxminded.dto.TimetableDTO;
import com.foxminded.enums.TimetableType;
import com.foxminded.service.TimetableService;
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

@WebMvcTest(controllers = {TimetableController.class})
@ExtendWith(MockitoExtension.class)
class TimetableControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimetableService timetableService;
    private TimetableDTO testTimetableDTO;

    @BeforeEach
    void setup() {
        testTimetableDTO = new TimetableDTO(
                1L,
                TimetableType.STUDENT_TIMETABLE,
                "test group",
                new ArrayList<>()
        );
    }

    @Test
    void testShowAll_Success() throws Exception {
        List<TimetableDTO> expectedTimetables = new ArrayList<>(List.of(testTimetableDTO));
        given(timetableService.getAllTimetables()).willReturn(expectedTimetables);

        mockMvc.perform(get("/timetables"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/timetablePage"))
                .andExpect(model().attribute("allTimetables", expectedTimetables));
    }
}