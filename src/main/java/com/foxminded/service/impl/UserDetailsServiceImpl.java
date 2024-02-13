package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.repository.StudentRepository;
import com.foxminded.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails student = studentRepository.findByName(username).orElse(null);
        UserDetails teacher = teacherRepository.findByName(username).orElse(null);

        if (student != null && teacher != null) {
            throw new IllegalStateException(ErrorMessages.STUDENT_AND_TEACHER_WITH_SAME_NAME_FOUND);
        }

        return student != null ? student : teacher;
    }
}
