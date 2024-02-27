package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.SubjectDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser(authorities = {"ADMIN"})
@AutoConfigureMockMvc(addFilters = false)
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
                "medicine",
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

    @Test
    void testShowCreatePage_Success() throws Exception {

        mockMvc.perform(get("/courses/course-creation"))
                .andExpect(status().isOk())
                .andExpect(view().name("createPages/createCoursePage"));
    }

    @Test
    void testShowUpdatePage_Success() throws Exception {
        given(courseService.getCourseById(1L)).willReturn(testCourseDto);
        mockMvc.perform(get("/courses/course-update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("updatePages/updateCoursePage"))
                .andExpect(model().attribute("course", testCourseDto));
    }

    @Test
    void testCreateCourse_Success() throws Exception {
        given(courseService.getAllCourses()).willReturn(List.of(testCourseDto));
        mockMvc.perform(post("/courses/course-creation")
                        .param("name", "name")
                        .param("description", "description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/courses"));
        verify(courseService, times(1)).addCourse(any(CourseDto.class));
    }

    @Test
    void testCreateCourse_CourseWithTheseParamsAlreadyExists() throws Exception {
        given(courseService.getAllCourses()).willReturn(List.of(testCourseDto));
        mockMvc.perform(post("/courses/course-creation")
                        .param("name", "medicine")
                        .param("description", "desc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/courses/course-creation?error=true"));
        verify(courseService, times(0)).addCourse(any(CourseDto.class));
    }

    @Test
    void testDeleteCourse_Success() throws Exception {
        mockMvc.perform(post("/courses/course-deletion/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/courses"));
        verify(courseService, times(1)).deleteCourseById(1L);
    }

    @Test
    void testUpdateCourse_Success() throws Exception {
        given(courseService.getAllCourses()).willReturn(List.of(testCourseDto));
        given(courseService.getCourseById(1L)).willReturn(testCourseDto);
        mockMvc.perform(post("/courses/course-update")
                        .param("courseId", "1")
                        .param("name", "name")
                        .param("description", "description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/courses"));
        verify(courseService, times(1)).updateCourse(any(CourseDto.class));
    }

    @Test
    void testUpdateCourse_CourseWithTheseParamsAlreadyExists() throws Exception {
        given(courseService.getAllCourses()).willReturn(List.of(testCourseDto));
        given(courseService.getCourseById(1L)).willReturn(testCourseDto);
        mockMvc.perform(post("/courses/course-update")
                        .param("courseId", "1")
                        .param("name", "medicine")
                        .param("description", "desc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/courses/course-update/1?error=true"));
        verify(courseService, times(0)).updateCourse(any(CourseDto.class));
    }

}

