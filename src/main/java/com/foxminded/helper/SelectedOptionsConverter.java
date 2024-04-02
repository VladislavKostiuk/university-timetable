package com.foxminded.helper;

import com.foxminded.dto.CourseDto;
import com.foxminded.enums.Role;
import com.foxminded.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SelectedOptionsConverter {
    private final CourseService courseService;

    public Set<Role> selectedRolesToSet(String selectedRoles) {
        List<String> selectedRolesList = selectedRoles != null ? Arrays.asList(selectedRoles.split(",")) : new ArrayList<>();
        Set<Role> updatedRoles = new HashSet<>();
        selectedRolesList.forEach(role -> updatedRoles.add(Role.valueOf(role)));
        return updatedRoles;
    }

    public List<CourseDto> selectedCoursesToList(String selectedCourses) {
        List<String> selectedCoursesList = selectedCourses != null ? Arrays.asList(selectedCourses.split(",")) : new ArrayList<>();
        return selectedCoursesList.stream().map(courseService::getCourseByName).toList();
    }
}
