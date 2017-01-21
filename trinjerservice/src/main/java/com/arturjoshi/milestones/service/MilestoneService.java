package com.arturjoshi.milestones.service;

import com.arturjoshi.milestones.Milestone;
import org.springframework.stereotype.Service;

/**
 * Created by ajoshi on 16-Jan-17.
 */
@Service
public class MilestoneService {

    public Milestone addChildMilestone(Milestone parent, Milestone child) {
        if(!parent.getType().equals(Milestone.MilestoneType.MILESTONE) ||
                !child.getType().equals(Milestone.MilestoneType.SPRINT)) {
            throw new IllegalArgumentException();
        }
        parent.getChildren().add(child);
        child.setParentMilestone(parent);
        return parent;
    }
}
