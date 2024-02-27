package com.foxminded.controller;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.SubjectDto;
import com.foxminded.dto.TeacherDto;
import com.foxminded.enums.Role;
import com.foxminded.service.CourseService;
import com.foxminded.service.GroupService;
import com.foxminded.service.LessonService;
import com.foxminded.service.SubjectService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SubjectController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser(authorities = {"ADMIN"})
@AutoConfigureMockMvc(addFilters = false)
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private LessonService lessonService;
    @MockBean
    private GroupService groupService;

    private SubjectDto testSubjectDto;

    @BeforeEach
    void setup() {
        testSubjectDto = new SubjectDto(
                1L,
                null,
                null,
                null,
                new ArrayList<>()
        );
    }

    @Test
    void testShowAll_Success() throws Exception {
        List<SubjectDto> expectedSubjects = new ArrayList<>(List.of(testSubjectDto));
        given(subjectService.getAllSubjects()).willReturn(expectedSubjects);

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/subjectPage"))
                .andExpect(model().attribute("allSubjects", expectedSubjects));
    }

    @Test
    void testShowCreatePage_Success() throws Exception {

        mockMvc.perform(get("/subjects/subject-creation"))
                .andExpect(status().isOk())
                .andExpect(view().name("createPages/createSubjectPage"))
                .andExpect(model().attributeExists("allCourses", "allTeachers", "allGroups"));
    }

    @Test
    void testShowUpdatePage_Success() throws Exception {
        given(subjectService.getSubjectById(1L)).willReturn(testSubjectDto);
        mockMvc.perform(get("/subjects/subject-update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("updatePages/updateSubjectPage"))
                .andExpect(model().attributeExists("allCourses", "allTeachers", "allGroups", "subject"))
                .andExpect(model().attribute("subject", testSubjectDto));
    }

    @Test
    void testCreateSubject_Success() throws Exception {
        mockMvc.perform(post("/subjects/subject-creation")
                        .param("course", "1")
                        .param("teacher", "1")
                        .param("group", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/subjects"));
        verify(subjectService, times(1)).addSubject(any(SubjectDto.class));
    }

    @Test
    void testCreateSubject_SubjectWithTheseParamsAlreadyExists() throws Exception {
        CourseDto course = new CourseDto(0L, "medicine", "desc");
        TeacherDto teacher = new TeacherDto(0L, "test teacher", "some pass",
                Set.of(Role.TEACHER), new ArrayList<>());
        GroupDto group = new GroupDto(0L, "test group");
        SubjectDto subject = new SubjectDto(0L, course, teacher, group, new ArrayList<>());

        given(courseService.getCourseById(1L)).willReturn(course);
        given(teacherService.getTeacherById(1L)).willReturn(teacher);
        given(groupService.getGroupById(1L)).willReturn(group);
        given(subjectService.getAllSubjects()).willReturn(List.of(subject));

        mockMvc.perform(post("/subjects/subject-creation")
                        .param("course", "1")
                        .param("teacher", "1")
                        .param("group", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/subjects/subject-creation?error=true"));
        verify(subjectService, times(0)).addSubject(any(SubjectDto.class));
    }

    @Test
    void testDeleteSubject_Success() throws Exception {
        mockMvc.perform(post("/subjects/subject-deletion/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/subjects"));
        verify(subjectService, times(1)).deleteSubjectById(1L);
    }

    @Test
    void testUpdateSubject_Success() throws Exception {
        given(subjectService.getSubjectById(1L)).willReturn(testSubjectDto);
        mockMvc.perform(post("/subjects/subject-update")
                        .param("subjectId", "1")
                        .param("course", "1")
                        .param("teacher", "1")
                        .param("group", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/subjects"));
        verify(subjectService, times(1)).updateSubject(any(SubjectDto.class));
    }

    @Test
    void testUpdateSubject_SubjectWithTheseParamsAlreadyExists() throws Exception {
        CourseDto course = new CourseDto(0L, "medicine", "desc");
        TeacherDto teacher = new TeacherDto(0L, "test teacher", "some pass",
                Set.of(Role.TEACHER), new ArrayList<>());
        GroupDto group = new GroupDto(0L, "test group");
        SubjectDto subject = new SubjectDto(0L, course, teacher, group, new ArrayList<>());

        given(courseService.getCourseById(1L)).willReturn(course);
        given(teacherService.getTeacherById(1L)).willReturn(teacher);
        given(groupService.getGroupById(1L)).willReturn(group);
        given(subjectService.getAllSubjects()).willReturn(List.of(subject));
        given(subjectService.getSubjectById(1L)).willReturn(testSubjectDto);

        mockMvc.perform(post("/subjects/subject-update")
                        .param("subjectId", "1")
                        .param("course", "1")
                        .param("teacher", "1")
                        .param("group", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/subjects/subject-update/1?error=true"));
        verify(subjectService, times(0)).updateSubject(any(SubjectDto.class));
    }
}

