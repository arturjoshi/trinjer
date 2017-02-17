package com.arturjoshi.ticket.issue.dto;

import com.arturjoshi.ticket.TicketPriority;
import com.arturjoshi.ticket.TicketStatus;
import com.arturjoshi.ticket.issue.IssueResolution;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by arturjoshi on 17-Feb-17.
 */
@NoArgsConstructor
@Data
public class IssueDto {

    private String summary;
    private String description;
    private TicketPriority priority;
    private TicketStatus status;
    private IssueResolution resolution;
    private String stepsToReproduce;
    private Long assigneeId;
}
