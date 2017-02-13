package com.arturjoshi.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by arturjoshi on 13-Feb-17.
 */
@NoArgsConstructor
@Data
public class ProjectInvitationDto extends ProjectPermissionProfileDto {
    private String email;
}
