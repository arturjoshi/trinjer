package com.arturjoshi.milestone;

import com.arturjoshi.project.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by ajoshi on 13-Jan-17.
 */
@NoArgsConstructor
@Data
@Entity
public class Milestone {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne(optional = false)
    private Project project;

    @ManyToOne
    private Milestone parentMilestone;

    @OneToMany(mappedBy = "parentMilestone")
    @JsonIgnore
    private List<Milestone> childrenMilestones;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MilestoneType type;

    public enum MilestoneType {
        MILESTONE,
        SPRINT
    }
}
