package com.foxminded.entity;

import com.foxminded.enums.TimetableType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timetable")
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timetable_type")
    @Enumerated(EnumType.STRING)
    private TimetableType timetableType;

    @Column(name = "qualifying_name")
    private String qualifyingName;

    @ManyToMany
    @JoinTable(
            name = "timetable_lesson",
            joinColumns = @JoinColumn(name = "timetable_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private List<Lesson> lessons;

    public Timetable() {
        lessons = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimetableType getTimetableType() {
        return timetableType;
    }

    public void setTimetableType(TimetableType timetableType) {
        this.timetableType = timetableType;
    }

    public String getQualifyingName() {
        return qualifyingName;
    }

    public void setQualifyingName(String qualifyingName) {
        this.qualifyingName = qualifyingName;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }


}
