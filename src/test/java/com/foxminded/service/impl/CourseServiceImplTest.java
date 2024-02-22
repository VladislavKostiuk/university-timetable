package com.foxminded.service.impl;

import com.foxminded.dto.CourseDto;
import com.foxminded.enums.CourseName;
import com.foxminded.mapper.CourseMapper;
import com.foxminded.entity.Course;
import com.foxminded.mapper.CourseMapperImpl;
import com.foxminded.repository.CourseRepository;
import com.foxminded.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        CourseMapperImpl.class
})
class CourseServiceImplTest {
    private CourseService courseService;
    @Mock
    private CourseRepository courseRepository;
    @Autowired
    private CourseMapper courseMapper;

    @BeforeEach
    void init() {
        courseService = new CourseServiceImpl(courseRepository, courseMapper);
    }

    @Test
    void testAddCourse_Success() {
        CourseDto testCourseDto = new CourseDto(0L, CourseName.ARCHITECTURE, "desc");
        when(courseRepository.findById(testCourseDto.id())).thenReturn(Optional.empty());
        courseService.addCourse(testCourseDto);
        verify(courseRepository).save(any());
    }

    @Test
    void testAddCourse_CourseAlreadyExists() {
        CourseDto testCourseDto = new CourseDto(0L, CourseName.ARCHITECTURE, "desc");
        Course testCourse = courseMapper.mapToCourse(testCourseDto);
        when(courseRepository.findById(testCourseDto.id())).thenReturn(Optional.of(testCourse));
        assertThrows(IllegalStateException.class, () -> courseService.addCourse(testCourseDto));
    }

    @Test
    void testGetCourseById_Success() {
        Long id = 1L;
        Course course = new Course();
        course.setId(id);
        course.setName(CourseName.ARCHITECTURE);

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        CourseDto expectedCourseDto = courseMapper.mapToCourseDto(course);
        CourseDto actualCourseDto = courseService.getCourseById(id);
        assertEquals(expectedCourseDto, actualCourseDto);
        verify(courseRepository).findById(id);
    }

    @Test
    void testGetCourseById_CourseWasNotFound() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> courseService.getCourseById(anyLong()));
    }

    @Test
    void testUpdateCourse_Success() {
        CourseDto testCourseDto = new CourseDto(0L, CourseName.ARCHITECTURE, "desc");
        Course testCourse = courseMapper.mapToCourse(testCourseDto);
        when(courseRepository.findById(testCourseDto.id())).thenReturn(Optional.of(testCourse));
        courseService.updateCourse(testCourseDto);
        verify(courseRepository).save(any());
    }

    @Test
    void testUpdateCourse_CourseDoesNotExist() {
        CourseDto testCourseDto = new CourseDto(0L, CourseName.ARCHITECTURE, "desc");
        when(courseRepository.findById(testCourseDto.id())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> courseService.updateCourse(testCourseDto));
    }
}

