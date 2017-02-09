package com.arturjoshi.ticket.story;

import com.arturjoshi.project.Project;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.ticket.AbstractTicket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by ajoshi on 07-Feb-17.
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = {"id", "estimate", "acceptanceCriteria"})
@Entity
@Table(name = "story")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "story_type", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractStory extends AbstractTicket {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Integer estimate;

    private String acceptanceCriteria;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Sprint sprint;
}
