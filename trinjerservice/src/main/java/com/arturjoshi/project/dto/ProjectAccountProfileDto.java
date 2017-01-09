package com.arturjoshi.project.dto;

import com.arturjoshi.project.entities.ProjectAccountProfile;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by arturjoshi on 09-Jan-17.
 */
@AllArgsConstructor
@Data
public class ProjectAccountProfileDto {
    private String project;
    private String username;
    private ProjectAccountProfile.ProjectProfile profile;
}
