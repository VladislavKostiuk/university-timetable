package com.foxminded.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lesson")
@Getter
@Setter
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "day")
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @Column(name = "appointment_time")
    private LocalTime appointmentTime;

//    @ManyToMany
//    @JoinTable(
//            name = "lesson_timetables",
//            joinColumns = @JoinColumn(name = "lesson_id"),
//            inverseJoinColumns = @JoinColumn(name = "timetable_id")
//    )
    @ManyToMany(mappedBy = "lessons")
    private List<Timetable> timetables;

    public Lesson() {
        timetables = new ArrayList<>();
    }
}
