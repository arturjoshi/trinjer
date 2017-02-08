package com.arturjoshi.sprint.service;

import com.arturjoshi.project.Project;
import com.arturjoshi.project.repository.ProjectRepository;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.sprint.repository.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ajoshi on 08-Feb-17.
 */
@Service
public class SprintService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintRepository sprintRepository;

    public Sprint createSprint(Long accountId, Long projectId, Sprint sprint) {
        Project project = projectRepository.findOne(projectId);

        sprint.setProject(project);
        return sprintRepository.save(sprint);
    }

}
