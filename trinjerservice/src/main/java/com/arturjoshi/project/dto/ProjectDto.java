package com.arturjoshi.project.dto;

import com.arturjoshi.project.Project;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@NoArgsConstructor
@Data
public class ProjectDto {

    private String name;
    private Boolean isVisible = true;   

    public Project convertFromDto() {
        Project project = new Project();
        project.setName(this.name);
        project.setIsVisible(this.isVisible);
        return project;
    }
}
