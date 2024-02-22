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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = AuthController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private PasswordEncoder passwordEncoder;
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
    void testShowLoginPage_Success() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("authPages/loginPage"));
    }

    @Test
    void testShowRegisterPage_Success() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("authPages/registerPage"));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        given(studentService.getStudentByName("some name")).willReturn(Optional.empty());
        given(teacherService.getTeacherByName("some name")).willReturn(Optional.empty());

        mockMvc.perform(post("/register")
                        .param("username", "test name")
                        .param("password", "test password")
                        .param("user_type", "teacher"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(passwordEncoder, times(1)).encode(anyString());
        verify(teacherService, times(1)).addTeacher(any(TeacherDto.class));
    }

    @Test
    void testRegisterUser_UserNameIsAlreadyTaken() throws Exception {
        given(studentService.getStudentByName("test name")).willReturn(Optional.of(testStudentDto));
        given(teacherService.getTeacherByName("test name")).willReturn(Optional.of(testTeacherDto));

        mockMvc.perform(post("/register")
                        .param("username", "test name")
                        .param("password", "test password")
                        .param("user_type", "student"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/register?error=true"));
    }
}
