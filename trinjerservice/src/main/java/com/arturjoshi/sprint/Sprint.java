package com.arturjoshi.sprint;

import com.arturjoshi.project.Project;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
}
