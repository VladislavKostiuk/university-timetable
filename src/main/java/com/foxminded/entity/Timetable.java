package com.foxminded.entity;

import com.foxminded.enums.TimetableType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timetable")
@Getter
@Setter
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timetable_type")
    @Enumerated(EnumType.STRING)
    private TimetableType timetableType;

    @Column(name = "qualifying_name")
    private String qualifyingName;

//    @ManyToMany(mappedBy = "timetables")
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
}
