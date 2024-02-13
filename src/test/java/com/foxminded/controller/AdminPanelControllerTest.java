package com.foxminded.controller;

import com.foxminded.dto.StudentDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.enums.Role;
import com.foxminded.service.StudentService;
import com.foxminded.service.TeacherService;
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
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@WebMvcTest(controllers = AdminPanelController.class)
@WithMockUser(authorities = {"ADMIN"})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminPanelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentService studentService;
    @MockBean
    private TeacherService teacherService;
    private StudentDto testStudentDto;
    private TeacherDto testTeacherDto;

    @BeforeEach
    void setup() {
        testStudentDto = new StudentDto(
                1L,
                "test student",
                "some pass",
                Set.of(Role.STUDENT),
                null,
                new ArrayList<>()
        );

        testTeacherDto = new TeacherDto(
                1L,
                "test teacher",
                "some pass",
                Set.of(Role.TEACHER),
                new ArrayList<>()
        );
    }

    @Test
    void testShowStudentPanel_Success() throws Exception {
        List<StudentDto> expectedStudents = new ArrayList<>(List.of(testStudentDto));
        given(studentService.getAllStudents()).willReturn(expectedStudents);

        mockMvc.perform(get("/adminPanel/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanelPages/studentPage"))
                .andExpect(model().attribute("students", expectedStudents))
                .andExpect(model().attribute("allRoles", Role.values()));
    }

    @Test
    void testShowTeacherPanel_Success() throws Exception {
        List<TeacherDto> expectedTeachers = new ArrayList<>(List.of(testTeacherDto));
        given(teacherService.getAllTeachers()).willReturn(expectedTeachers);

        mockMvc.perform(get("/adminPanel/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanelPages/teacherPage"))
                .andExpect(model().attribute("teachers", expectedTeachers))
                .andExpect(model().attribute("allRoles", Role.values()));
    }

    @Test
    void testUpdateStudentRoles_Success() throws Exception{
        given(studentService.getStudentById(1L)).willReturn(testStudentDto);

        mockMvc.perform(post("/adminPanel/1/updateStudentRoles").param("selectedRoles", "STUDENT,ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminPanel/students"));

        verify(studentService, times(1)).updateStudent(any(StudentDto.class));
    }

    @Test
    void testUpdateTeacherRoles_Success() throws Exception{
        given(teacherService.getTeacherById(1L)).willReturn(testTeacherDto);

        mockMvc.perform(post("/adminPanel/1/updateTeacherRoles").param("selectedRoles", "TEACHER,ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminPanel/teachers"));

        verify(teacherService, times(1)).updateTeacher(any(TeacherDto.class));
    }

    @Test
    void testSearchStudentByName() throws Exception {
        String viewName = "redirect:/adminPanel/students?searchText=123";
        mockMvc.perform(post("/adminPanel/searchStudent").param("searchText", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(viewName));
    }

    @Test
    void testSearchTeacherByName() throws Exception {
        String viewName = "redirect:/adminPanel/teachers?searchText=123";
        mockMvc.perform(post("/adminPanel/searchTeacher").param("searchText", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(viewName));
    }
}

