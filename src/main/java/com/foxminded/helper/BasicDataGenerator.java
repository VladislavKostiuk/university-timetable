package com.foxminded.helper;

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
    private final List<String> fullNames;
    private final List<String> courseNames;
    private final Random random;

    public BasicDataGenerator() {
        random = new Random();
        fullNames = new ArrayList<>();
        initStudentNames();
        courseNames = new ArrayList<>();
        initCourseNames();
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
            course.setName(getCourseName());
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
            student.setName(getFullName());
            students.add(student);
        }

        return students;
    }

    public List<Teacher> generateBasicTeachers(int amount) {
        List<Teacher> teachers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Teacher teacher = new Teacher();
            teacher.setId(i + 1L);
            teacher.setName(getFullName());
            teachers.add(teacher);
        }

        return teachers;
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

    private String getFullName() {
        String fullName = fullNames.get(random.nextInt(fullNames.size()));
        fullNames.remove(fullName);
        return fullName;
    }

    private String getCourseName() {
        String courseName = courseNames.get(random.nextInt(courseNames.size()));
        courseNames.remove(courseName);
        return  courseName;
    }

    private void initCourseNames() {
        String[] courseNamesArr = new String[] {"Math", "Biology", "Finance", "Art",
                "Architecture", "Engineering", "Science", "Management", "Economics", "Medicine"};
        courseNames.addAll(Arrays.asList(courseNamesArr));
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

        for (String firstName : firstNamesArr) {
            for (String lastName : lastNamesArr) {
                fullNames.add(firstName + " " + lastName);
            }
        }
    }
}
