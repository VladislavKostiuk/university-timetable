package com.foxminded.service.impl;

import com.foxminded.enums.Role;
import com.foxminded.enums.TimetableType;
import com.foxminded.helper.BasicDataGenerator;
import com.foxminded.repository.*;
import com.foxminded.entity.*;
import com.foxminded.service.DbInitService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;


@Service
@Transactional
public class DbInitServiceImpl implements DbInitService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final BasicDataGenerator basicDataGenerator;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final LessonRepository lessonRepository;
    private final TimetableRepository timetableRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random;

    @Autowired
    public DbInitServiceImpl(StudentRepository studentRepository,
                             CourseRepository courseRepository,
                             GroupRepository groupRepository,
                             BasicDataGenerator basicDataGenerator,
                             TeacherRepository teacherRepository,
                             SubjectRepository subjectRepository,
                             LessonRepository lessonRepository,
                             TimetableRepository timetableRepository,
                             PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
        this.basicDataGenerator = basicDataGenerator;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.lessonRepository = lessonRepository;
        this.timetableRepository = timetableRepository;
        this.passwordEncoder = passwordEncoder;
        random = new Random();
    }

    @Override
    public void init() {
        List<Course> courses = basicDataGenerator.generateBasicCourses(10);
        List<Group> groups = basicDataGenerator.generateBasicGroups(10);
        List<Student> students = basicDataGenerator.generateBasicStudents(90);
        List<Teacher> teachers = basicDataGenerator.generateBasicTeachers(5);
        List<Subject> subjects = basicDataGenerator.generateBasicSubjects(15);
        List<Lesson> lessons = basicDataGenerator.generateBasicLessons(25);
        List<Timetable> timetables = basicDataGenerator.generateBasicTimeTables(teachers.size() + groups.size());

        initStudents(students, groups, courses);
        initTeachers(teachers, courses);
        initSubjects(subjects, groups, courses, teachers);
        initLessons(lessons, subjects);
        initTimetables(timetables, lessons, teachers, groups);

        if (courseRepository.count() == 0) {
            courseRepository.saveAll(courses);
        }

        if (groupRepository.count() == 0) {
            groupRepository.saveAll(groups);
        }

        if (studentRepository.count() == 0) {
            studentRepository.saveAll(students);
        }

        if (teacherRepository.count() == 0) {
            teacherRepository.saveAll(teachers);
        }

        if (subjectRepository.count() == 0) {
            subjectRepository.saveAll(subjects);
        }

        if (lessonRepository.count() == 0) {
            lessonRepository.saveAll(lessons);
        }

        if (timetableRepository.count() == 0) {
            timetableRepository.saveAll(timetables);
        }

        setTimetablesToLessons(timetables, lessons);
        lessonRepository.saveAll(lessons);
    }

    private void initStudents(List<Student> allStudents, List<Group> allGroups, List<Course> allCourses) {
        for (int i = 0; i < allStudents.size(); i++) {
            Group group = getRandomGroup(allGroups);
            List<Course> courses = getRandomCourseList(allCourses);
            String password = passwordEncoder.encode(UUID.randomUUID().toString());
            Student student = allStudents.get(i);
            student.setGroup(group);
            student.setCourses(courses);
            student.setPassword(password);
            student.setRoles(Set.of(Role.STUDENT));

            if (i == 0) {
                student.setPassword(passwordEncoder.encode("12345"));
                student.setRoles(Set.of(Role.STUDENT, Role.ADMIN));
            }
        }
    }

    private void initTeachers(List<Teacher> allTeachers, List<Course> allCourses) {
        for (var teacher : allTeachers) {
            List<Course> courses = getRandomCourseList(allCourses);
            teacher.setCourses(courses);
            teacher.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            teacher.setRoles(Set.of(Role.TEACHER));
        }
    }

    private void initSubjects(List<Subject> allSubject, List<Group> allGroups, List<Course> allCourses, List<Teacher> allTeachers) {
        for (var subject : allSubject) {
            Group group = getRandomGroup(allGroups);
            Teacher teacher = getRandomTeacher(allTeachers);
            Course course = getRandomCourse(teacher.getCourses());
            subject.setGroup(group);
            subject.setCourse(course);
            subject.setTeacher(teacher);
        }
    }

    private void initLessons(List<Lesson> allLessons, List<Subject> allSubjects) {
        for (var lesson : allLessons) {
            Subject subject = getRandomSubject(allSubjects);
            lesson.setSubject(subject);
            DayOfWeek day = getRandomDayOfWeek();
            lesson.setDay(day);
            LocalTime appointmentTime = getRandomAppointmentTime();
            lesson.setAppointmentTime(appointmentTime);
        }
    }

    private void initTimetables(List<Timetable> allTimetables, List<Lesson> allLessons,
                                List<Teacher> allTeachers, List<Group> allGroups) {
        for (int i = 0; i < allTeachers.size(); i++) {
            Teacher teacher = allTeachers.get(i);
            Timetable timetable = allTimetables.get(i);
            timetable.setTimetableType(TimetableType.TEACHER_TIMETABLE);
            timetable.setQualifyingName(teacher.getName());

            for (int j = 0; j < allLessons.size(); j++) {
                Lesson lesson = allLessons.get(j);
                String teacherName = lesson.getSubject().getTeacher().getName();
                if (teacherName.equals(teacher.getName())) {
                    timetable.getLessons().add(lesson);
                }
            }
        }

        for (int i = 0; i < allGroups.size(); i++) {
            Group group = allGroups.get(i);
            Timetable timetable = allTimetables.get(allTeachers.size() + i);
            timetable.setTimetableType(TimetableType.STUDENT_TIMETABLE);
            timetable.setQualifyingName(group.getName());

            for (int j = 0; j < allLessons.size(); j++) {
                Lesson lesson = allLessons.get(j);
                String groupName = lesson.getSubject().getGroup().getName();
                if (groupName.equals(group.getName())) {
                    timetable.getLessons().add(lesson);
                }
            }
        }
    }

    private void setTimetablesToLessons(List<Timetable> allTimetables, List<Lesson> allLessons) {
        for (var lesson : allLessons) {
            for (var timetable : allTimetables) {
                if (timetable.getLessons().contains(lesson)) {
                    lesson.getTimetables().add(timetable);
                }
            }
        }
    }

    private LocalTime getRandomAppointmentTime() {
        int[][] timeVariants = new int[][] {
                {8, 30}, {10, 25}, {12, 20}, {14, 15}, {16, 10}
        };

        int[] time = timeVariants[random.nextInt(timeVariants.length)];
        return LocalTime.of(time[0], time[1]);
    }

    private DayOfWeek getRandomDayOfWeek() {
        return DayOfWeek.values()[random.nextInt(6)];
    }

    private Group getRandomGroup(List<Group> groups) {
        return groups.get(random.nextInt(groups.size()));
    }

    private Course getRandomCourse(List<Course> allCourses) {
        return allCourses.get(random.nextInt(allCourses.size()));
    }

    private Teacher getRandomTeacher(List<Teacher> allTeachers) {
        return allTeachers.get(random.nextInt(allTeachers.size()));
    }

    private Subject getRandomSubject(List<Subject> allSubjects) {
        return allSubjects.get(random.nextInt(allSubjects.size()));
    }

    private List<Course> getRandomCourseList(List<Course> allCourses) {
        int coursesAmount = random.nextInt(3) + 1;
        List<Course> courses = new ArrayList<>();
        for (int j = 0; j < coursesAmount; j++) {
            Course randomCourse = getRandomCourse(allCourses);

            while (courses.contains(randomCourse)) {
                randomCourse = getRandomCourse(allCourses);
            }

            courses.add(randomCourse);
        }

        return courses;
    }
}

