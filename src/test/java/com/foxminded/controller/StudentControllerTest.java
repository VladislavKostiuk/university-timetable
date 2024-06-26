package com.foxminded.controller;

import com.foxminded.dto.StudentDto;
import com.foxminded.enums.Role;
import com.foxminded.helper.SelectedOptionsConverter;
import com.foxminded.service.CourseService;
import com.foxminded.service.CustomUserDetailsService;
import com.foxminded.service.GroupService;
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

@WebMvcTest(controllers = StudentController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser(authorities = {"ADMIN"})
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private CustomUserDetailsService userDetailsService;
    @MockBean
    private SelectedOptionsConverter optionsConverter;
    private StudentDto testStudentDto;

    @BeforeEach
    void setup() {
        testStudentDto = new StudentDto(
                0L,
                "test student",
                "some pass",
                Set.of(Role.STUDENT),
                null,
                new ArrayList<>()
        );
    }

    @Test
    void testShowAll_Success() throws Exception{
        List<StudentDto> expectedStudents = new ArrayList<>(List.of(testStudentDto));
        given(studentService.getAllStudents()).willReturn(expectedStudents);

        mockMvc.perform(get("/admin-panel/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/studentPage"))
                .andExpect(model().attribute("allStudents", expectedStudents));
    }

    @Test
    void testShowUpdatePage_Success() throws Exception {
        given(studentService.getStudentById(1L)).willReturn(testStudentDto);
        mockMvc.perform(get("/admin-panel/students/student-update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("updatePages/updateStudentPage"))
                .andExpect(model().attributeExists("student", "allGroups", "allCourses", "studentCourses"))
                .andExpect(model().attribute("student", testStudentDto));
    }

    @Test
    void testSearch_Success() throws Exception {
        mockMvc.perform(post("/admin-panel/students/search")
                        .param("studentName", "some name")
                        .param("groupName", "some group"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel/students?student-name=some name&group-name=some group"));
    }

    @Test
    void testDeleteStudent_Success() throws Exception {
        mockMvc.perform(post("/admin-panel/students/student-deletion/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel/students"));
        verify(studentService, times(1)).deleteStudentById(1L);
    }

    @Test
    void testUpdateStudent_Success() throws Exception {
        given(studentService.getStudentById(1L)).willReturn(testStudentDto);
        given(userDetailsService.isNameAvailable("some name")).willReturn(true);
        mockMvc.perform(post("/admin-panel/students/student-update")
                        .param("studentId", "1")
                        .param("name", "some name")
                        .param("password", "some pass")
                        .param("group", "1")
                        .param("selectedCourses", "MATH,FINANCE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel/students"));
        verify(studentService, times(1)).updateStudent(any(StudentDto.class));
    }

    @Test
    void testUpdateStudent_ThisNameIsAlreadyTaken() throws Exception {
        given(studentService.getStudentById(1L)).willReturn(testStudentDto);
        given(userDetailsService.isNameAvailable("some name")).willReturn(false);
        mockMvc.perform(post("/admin-panel/students/student-update")
                        .param("studentId", "1")
                        .param("name", "some already existing name")
                        .param("password", "some pass")
                        .param("group", "1")
                        .param("selectedCourses", "MATH,FINANCE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-panel/students/student-update/1?error=true"));
        verify(studentService, times(0)).updateStudent(any(StudentDto.class));
    }
}

