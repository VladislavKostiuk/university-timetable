package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.LessonDto;
import com.foxminded.dto.SubjectDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.enums.Role;
import com.foxminded.service.LessonService;
import com.foxminded.service.SubjectService;
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
import java.time.DayOfWeek;
import java.time.LocalTime;
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

@WebMvcTest(controllers = LessonController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser(authorities = {"ADMIN"})
@AutoConfigureMockMvc(addFilters = false)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;
    @MockBean
    private SubjectService subjectService;

    private LessonDto testLessonDto;
    private SubjectDto testSubjectDto;

    @BeforeEach
    void setup() {
        CourseDto course = new CourseDto(0L, "medicine", "desc");
        TeacherDto teacher = new TeacherDto(0L, "test teacher", "some pass",
                Set.of(Role.TEACHER), new ArrayList<>());
        GroupDto group = new GroupDto(0L, "test group");
        testSubjectDto = new SubjectDto(1L, course, teacher, group);
        testLessonDto = new LessonDto(
                1L,
                testSubjectDto,
                DayOfWeek.MONDAY,
                LocalTime.of(10, 25)
        );
    }

    @Test
    void testShowAll_Success() throws Exception{
        List<LessonDto> expectedLessons = new ArrayList<>(List.of(testLessonDto));
        given(lessonService.getAllLessons()).willReturn(expectedLessons);

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/lessonPage"))
                .andExpect(model().attribute("allLessons", expectedLessons));
    }

    @Test
    void testShowCreatePage_Success() throws Exception {

        mockMvc.perform(get("/lessons/lesson-creation"))
                .andExpect(status().isOk())
                .andExpect(view().name("createPages/createLessonPage"))
                .andExpect(model().attributeExists("allSubjects", "allDaysOfWeek"));
    }

    @Test
    void testShowUpdatePage_Success() throws Exception {
        given(lessonService.getLessonById(1L)).willReturn(testLessonDto);
        mockMvc.perform(get("/lessons/lesson-update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("updatePages/updateLessonPage"))
                .andExpect(model().attributeExists("allSubjects", "allDaysOfWeek", "lesson"))
                .andExpect(model().attribute("lesson", testLessonDto));
    }

    @Test
    void testCreateLesson_Success() throws Exception {
        mockMvc.perform(post("/lessons/lesson-creation")
                        .param("subject", "1")
                        .param("dayOfWeek", "MONDAY")
                        .param("time", "10:25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lessons"));
        verify(lessonService, times(1)).addLesson(any(LessonDto.class));
    }

    @Test
    void testCreateLesson_LessonWithTheseParamsAlreadyExists() throws Exception {
        given(subjectService.getSubjectById(1L)).willReturn(testSubjectDto);
        given(lessonService.getAllLessons()).willReturn(List.of(testLessonDto));

        mockMvc.perform(post("/lessons/lesson-creation")
                        .param("subject", "1")
                        .param("dayOfWeek", "MONDAY")
                        .param("time", "10:25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lessons/lesson-creation?error=true"));
        verify(lessonService, times(0)).addLesson(any(LessonDto.class));
    }

    @Test
    void testDeleteLesson_Success() throws Exception {
        mockMvc.perform(post("/lessons/lesson-deletion/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lessons"));
        verify(lessonService, times(1)).deleteLessonById(1L);
    }

    @Test
    void testUpdateLesson_Success() throws Exception {
        given(lessonService.getLessonById(1L)).willReturn(testLessonDto);
        mockMvc.perform(post("/lessons/lesson-update")
                        .param("lessonId", "1")
                        .param("subject", "1")
                        .param("dayOfWeek", "MONDAY")
                        .param("time", "10:25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lessons"));
        verify(lessonService, times(1)).updateLesson(any(LessonDto.class));
    }

    @Test
    void testUpdateLesson_LessonWithTheseParamsAlreadyExists() throws Exception {
        given(subjectService.getSubjectById(1L)).willReturn(testSubjectDto);
        given(lessonService.getAllLessons()).willReturn(List.of(testLessonDto));

        mockMvc.perform(post("/lessons/lesson-update")
                        .param("lessonId", "1")
                        .param("subject", "1")
                        .param("dayOfWeek", "MONDAY")
                        .param("time", "10:25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lessons/lesson-update/1?error=true"));
        verify(lessonService, times(0)).updateLesson(any(LessonDto.class));
    }
}

