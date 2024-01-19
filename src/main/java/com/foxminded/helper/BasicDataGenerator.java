package com.foxminded.helper;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.enums.CourseName;
import com.foxminded.entity.*;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.UUID;


@Component
public class BasicDataGenerator {
    private final List<String> firstNames;
    private final List<String> lastNames;
    private final List<CourseName> courseNames;
    private final Random random;

    public BasicDataGenerator() {
        random = new Random();
        firstNames = new ArrayList<>();
        lastNames = new ArrayList<>();
        initStudentNames();
        courseNames = new ArrayList<>(Arrays.asList(CourseName.values()));
        Collections.shuffle(courseNames);
    }

    public List<Group> generateBasicGroups(int amount) {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Group group = new Group();
            group.setId(i + 1L);
            group.setName(generateGroupName());
            groups.add(group);
        }

        return groups;
    }

    public List<Course> generateBasicCourses(int amount) {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Course course = new Course();
            course.setId(i + 1L);
            course.setName(generateCourseName());
            course.setDescription(UUID.randomUUID().toString());
            courses.add(course);
        }

        return courses;
    }

    public List<Student> generateBasicStudents(int amount) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Student student = new Student();
            student.setId(i + 1L);
            student.setName(generateFullName());
            students.add(student);
        }

        return students;
    }

    public List<Teacher> generateBasicTeachers(int amount) {
        List<Teacher> teachers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Teacher teacher = new Teacher();
            teacher.setId(i + 1L);
            teacher.setName(generateFullName());
            teachers.add(teacher);
        }

        return teachers;
    }

    public List<Subject> generateBasicSubjects(int amount) {
        List<Subject> subjects = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Subject subject = new Subject();
            subject.setId(i + 1L);
            subjects.add(subject);
        }

        return subjects;
    }

    public List<Lesson> generateBasicLessons(int amount) {
        List<Lesson> lessons = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Lesson lesson = new Lesson();
            lesson.setId(i + 1L);
            lessons.add(lesson);
        }

        return lessons;
    }

    public List<Timetable> generateBasicTimeTables(int amount) {
        List<Timetable> timetables = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Timetable timetable = new Timetable();
            timetable.setId(i + 1L);
            timetables.add(timetable);
        }

        return timetables;
    }

    private String generateGroupName() {
        return String.valueOf((char) (random.nextInt(26) + 'a')) +
                (char) (random.nextInt(26) + 'a') + "-" +
                random.nextInt(10) +
                random.nextInt(10);
    }

    private String generateFullName() {
        String firstName = firstNames.get(random.nextInt(firstNames.size()));
        String lastName = lastNames.get(random.nextInt(lastNames.size()));
        return firstName + " " + lastName;
    }

    private CourseName generateCourseName() {
        if (courseNames.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.NO_MORE_AVAILABLE_NAMES_FOR_COURSES);
        }
        CourseName courseName = courseNames.get(0);
        courseNames.remove(courseName);
        return courseName;
    }

    private void initStudentNames() {
        String[] firstNamesArr = new String[] {"Liam", "Noah", "Oliver", "James", "Elijah",
                "William", "Henry", "Lucas", "Benjamin", "Theodore",
                "Olivia", "Emma", "Charlotte", "Amelia", "Sophia",
                "Isabella", "Ava", "Mira", "Evelyn", "Luna"};
        String[] lastNamesArr = new String[] {"Jackson", "Mason", "Logan", "Wyatt", "Hudson",
                "Grayson", "Carter", "Lincoln", "Nolan", "Cameron",
                "Addison", "Ainsley", "Arley", "Avery", "Parker",
                "Rawley", "Collins", "Eston", "Hadley", "Kensley"};

        firstNames.addAll(Arrays.asList(firstNamesArr));
        lastNames.addAll(Arrays.asList(lastNamesArr));
    }
}
