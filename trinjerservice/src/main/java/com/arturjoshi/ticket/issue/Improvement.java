package com.arturjoshi.ticket.issue;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by ajoshi on 07-Feb-17.
 */
@NoArgsConstructor
@Data
@Entity
@DiscriminatorValue("improvement")
public class Improvement extends AbstractIssue {
}
