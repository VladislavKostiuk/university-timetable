package com.foxminded.service.impl;

import com.foxminded.entity.Student;
import com.foxminded.entity.Teacher;
import com.foxminded.repository.StudentRepository;
import com.foxminded.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplTest {
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    private Student student;
    private Teacher teacher;

    @BeforeEach
    void setup() {
        student = new Student();
        student.setName("name1");
        teacher = new Teacher();
        teacher.setName("name2");
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(studentRepository.findByName("name1")).thenReturn(Optional.of(student));
        when(teacherRepository.findByName("name2")).thenReturn(Optional.of(teacher));
        UserDetails actualUser = userDetailsService.loadUserByUsername("name2");
        assertEquals(teacher, actualUser);
    }

    @Test
    void testLoadUserByUsername_StudentAndTeacherWithTheSameNameFound() {
        teacher.setName("name1");
        when(studentRepository.findByName("name1")).thenReturn(Optional.of(student));
        when(teacherRepository.findByName("name1")).thenReturn(Optional.of(teacher));
        assertThrows(IllegalStateException.class, () -> userDetailsService.loadUserByUsername("name1"));
    }
}
