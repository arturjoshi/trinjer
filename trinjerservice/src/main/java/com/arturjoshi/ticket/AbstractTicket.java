package com.arturjoshi.ticket;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * Created by ajoshi on 07-Feb-17.
 */
@NoArgsConstructor
@Data
@MappedSuperclass
public abstract class AbstractTicket {

    private String summary;

    private String description;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;
}
