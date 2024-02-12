package com.foxminded.controller;

import com.foxminded.dto.SubjectDto;
import com.foxminded.service.SubjectService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SubjectController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    private SubjectDto testSubjectDto;

    @BeforeEach
    void setup() {
        testSubjectDto = new SubjectDto(
                1L,
                null,
                null,
                null
        );
    }

    @Test
    void testShowAll_Success() throws Exception{
        List<SubjectDto> expectedSubjects = new ArrayList<>(List.of(testSubjectDto));
        given(subjectService.getAllSubjects()).willReturn(expectedSubjects);

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(view().name("entityPages/subjectPage"))
                .andExpect(model().attribute("allSubjects", expectedSubjects));
    }
}

