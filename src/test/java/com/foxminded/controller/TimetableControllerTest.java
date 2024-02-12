package com.foxminded.controller;

import com.foxminded.dto.TimetableDto;
import com.foxminded.enums.TimetableType;
import com.foxminded.service.TimetableService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {TimetableController.class})
@ExtendWith(MockitoExtension.class)
@WithMockUser
class TimetableControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimetableService timetableService;
    private TimetableDto testTimetableDto;

    @BeforeEach
    void setup() {
        testTimetableDto = new TimetableDto(
                1L,
                TimetableType.STUDENT_TIMETABLE,
                "test group",
                new ArrayList<>()
        );
    }

    @Test
    void testShowAll_Success() throws Exception {
        List<TimetableDto> expectedTimetables = new ArrayList<>(List.of(testTimetableDto));
        given(timetableService.getAllTimetables()).willReturn(expectedTimetables);

        mockMvc.perform(get("/timetables"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/timetablePage"))
                .andExpect(model().attribute("allTimetables", expectedTimetables));
    }
}

