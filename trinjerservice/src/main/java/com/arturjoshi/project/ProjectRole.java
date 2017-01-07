package com.arturjoshi.project;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by arturjoshi on 07-Jan-17.
 */

@Entity
@NoArgsConstructor
@Data
public class ProjectRole {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String role;
}
