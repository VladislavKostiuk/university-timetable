package com.foxminded.controller;

import com.foxminded.dto.TeacherDto;
import com.foxminded.dto.TimetableDto;
import com.foxminded.enums.Role;
import com.foxminded.enums.TimetableType;
import com.foxminded.service.CourseService;
import com.foxminded.service.CustomUserDetailsService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TeacherController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser(authorities = {"ADMIN"})
@AutoConfigureMockMvc(addFilters = false)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private CustomUserDetailsService userDetailsService;
    @MockBean
    private TimetableService timetableService;

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

    @Test
    void testShowUpdatePage_Success() throws Exception {
        given(teacherService.getTeacherById(1L)).willReturn(testTeacherDto);
        mockMvc.perform(get("/teachers/teacher-update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("updatePages/updateTeacherPage"))
                .andExpect(model().attributeExists("teacher", "allCourses", "teacherCourses"))
                .andExpect(model().attribute("teacher", testTeacherDto));
    }

    @Test
    void testDeleteTeacher_Success() throws Exception {
        mockMvc.perform(post("/teachers/teacher-deletion/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teachers"));
        verify(teacherService, times(1)).deleteTeacherById(1L);
    }

    @Test
    void testUpdateTeacher_Success() throws Exception {
        TimetableDto testTimetableDto = new TimetableDto(1L, TimetableType.TEACHER_TIMETABLE,
                testTeacherDto.name(), new ArrayList<>());

        given(teacherService.getTeacherById(1L)).willReturn(testTeacherDto);
        given(userDetailsService.isNameAvailable("some name", "test teacher")).willReturn(true);
        given(timetableService.getTimetableByQualifyingName(testTeacherDto.name())).willReturn(testTimetableDto);
        mockMvc.perform(post("/teachers/teacher-update")
                        .param("teacherId", "1")
                        .param("name", "some name")
                        .param("password", "some pass")
                        .param("selectedCourses", "MATH,FINANCE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teachers"));
        verify(teacherService, times(1)).updateTeacher(any(TeacherDto.class));
    }

    @Test
    void testUpdateTeacher_ThisNameIsAlreadyTaken() throws Exception {
        given(teacherService.getTeacherById(1L)).willReturn(testTeacherDto);
        given(userDetailsService.isNameAvailable("some name", "test teacher")).willReturn(false);
        mockMvc.perform(post("/teachers/teacher-update")
                        .param("teacherId", "1")
                        .param("name", "some name")
                        .param("password", "some pass")
                        .param("selectedCourses", "MATH,FINANCE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/teachers/teacher-update/1?error=true"));
        verify(teacherService, times(0)).updateTeacher(any(TeacherDto.class));
    }
}

