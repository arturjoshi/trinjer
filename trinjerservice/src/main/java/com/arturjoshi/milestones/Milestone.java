package com.arturjoshi.milestones;

import com.arturjoshi.project.Project;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Milestone {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilestoneType type;

    @ManyToOne
    private Milestone parentMilestone;

    @OneToMany(mappedBy = "parentMilestone")
    @JsonIgnore
    private Set<Milestone> children = new HashSet<>();

    public void addChildMilestone(Milestone milestone) {
        this.children.add(milestone);
        milestone.setParentMilestone(this);
    }

    public enum MilestoneType {
        MILESTONE,
        SPRINT
    }
}
