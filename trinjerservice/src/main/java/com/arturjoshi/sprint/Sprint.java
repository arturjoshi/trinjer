package com.arturjoshi.sprint;

import com.arturjoshi.project.Project;
import com.arturjoshi.ticket.issue.AbstractIssue;
import com.arturjoshi.ticket.story.AbstractStory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ajoshi on 16-Jan-17.
 */
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(of = {"id", "description", "startDate", "endDate"})
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String description;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ManyToOne(optional = false)
    private Project project;

    @OneToMany(mappedBy = "sprint")
    @Cascade(CascadeType.DELETE)
    @JsonIgnore
    private Set<AbstractStory> sprintBacklog = new HashSet<>();

    @OneToMany(mappedBy = "sprint")
    @Cascade(CascadeType.DELETE)
    @JsonIgnore
    private Set<AbstractIssue> issues = new HashSet<>();
}
