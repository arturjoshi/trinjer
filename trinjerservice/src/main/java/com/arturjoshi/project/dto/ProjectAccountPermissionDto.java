package com.arturjoshi.project.dto;

import com.arturjoshi.project.entities.ProjectAccountPermission;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by arturjoshi on 09-Jan-17.
 */
@AllArgsConstructor
@Data
public class ProjectAccountPermissionDto {

    private String project;
    private String username;
    private ProjectAccountPermission.ProjectPermission permission;
}
