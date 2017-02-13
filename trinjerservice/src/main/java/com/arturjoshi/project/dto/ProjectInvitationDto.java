package com.arturjoshi.project.dto;

import com.arturjoshi.project.entities.ProjectAccountPermission;
import com.arturjoshi.project.entities.ProjectAccountProfile;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by arturjoshi on 13-Feb-17.
 */
@NoArgsConstructor
@Data
public class ProjectInvitationDto {
    private String email;
    private ProjectAccountPermission.ProjectPermission permission;
    private ProjectAccountProfile.ProjectProfile profile;
}
