package com.foxminded.controller;

import com.foxminded.dto.StudentDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.service.StudentService;
import com.foxminded.service.TeacherService;
import com.foxminded.service.TimetableService;
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
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = {UserTimetableController.class})
@ExtendWith(MockitoExtension.class)
@WithMockUser(authorities = {"ADMIN"})
@AutoConfigureMockMvc(addFilters = false)
class UserTimetableControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TimetableService timetableService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private StudentService studentService;

    @Test
    void testShowUserTimetable() throws Exception {
        StudentDto student = new StudentDto(1L, "user", "pass",
                new HashSet<>(), null, new ArrayList<>());

        given(studentService.getStudentByName("user")).willReturn(Optional.of(student));
        mockMvc.perform(get("/timetable"))
                .andExpect(status().isOk())
                .andExpect(view().name("userTimetablePage"));
    }
}