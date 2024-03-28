package com.foxminded.controller;

import com.foxminded.dto.LessonDto;
import com.foxminded.dto.TimetableDto;
import com.foxminded.enums.TimetableType;
import com.foxminded.service.GroupService;
import com.foxminded.service.LessonService;
import com.foxminded.service.TeacherService;
import com.foxminded.service.TimetableService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {TimetableController.class})
@ExtendWith(MockitoExtension.class)
@WithMockUser(authorities = {"ADMIN"})
@AutoConfigureMockMvc(addFilters = false)
class TimetableControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimetableService timetableService;
    @MockBean
    private LessonService lessonService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private GroupService groupService;
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

    @Test
    void testShowCreatePage_Success() throws Exception {

        mockMvc.perform(get("/timetables/timetable-creation"))
                .andExpect(status().isOk())
                .andExpect(view().name("createPages/createTimetablePage"))
                .andExpect(model().attributeExists("allQualifyingNames"));
    }

    @Test
    void testShowUpdatePage_Success() throws Exception {
        given(timetableService.getTimetableById(1L)).willReturn(testTimetableDto);
        mockMvc.perform(get("/timetables/timetable-update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("updatePages/updateTimetablePage"))
                .andExpect(model().attributeExists("timetable", "availableLessons"))
                .andExpect(model().attribute("timetable", testTimetableDto));
    }

    @Test
    void testCreateTimetables_Success() throws Exception {
        mockMvc.perform(post("/timetables/timetable-creation")
                        .param("qualifyingName", "some name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timetables"));
        verify(timetableService, times(1)).addTimetable(any(TimetableDto.class));
    }

    @Test
    void testCreateTimetables_TimetableWithThisQualifyingNameAlreadyExists() throws Exception {
        given(timetableService.getAllTimetables()).willReturn(List.of(testTimetableDto));
        mockMvc.perform(post("/timetables/timetable-creation")
                        .param("qualifyingName", "test group"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timetables/timetable-creation?error=true"));
        verify(timetableService, times(0)).addTimetable(any(TimetableDto.class));
    }

    @Test
    void testDeleteTimetable_Success() throws Exception {
        mockMvc.perform(post("/timetables/timetable-deletion/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timetables"));
        verify(timetableService, times(1)).deleteTimetableById(1L);
    }

    @Test
    void testAddLessonToTimetable_Success() throws Exception {
        given(timetableService.getTimetableById(1L)).willReturn(testTimetableDto);
        mockMvc.perform(post("/timetables/1/lesson-addition")
                        .param("availableLessonId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timetables/timetable-update/1"));
        verify(timetableService, times(1)).updateTimetable(any(TimetableDto.class));
    }





}

