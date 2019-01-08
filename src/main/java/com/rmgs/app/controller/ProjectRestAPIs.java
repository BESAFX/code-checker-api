package com.rmgs.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.rmgs.app.config.CustomException;
import com.rmgs.app.dao.ProjectDao;
import com.rmgs.app.model.Project;
import com.rmgs.app.sonar.SonarController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ProjectRestAPIs {

    private final String FILTER_TABLE = "**";

    @Autowired
    private SonarController sonarController;

    @Autowired
    private ProjectDao projectDao;

    @PostMapping("/api/project")
    @PreAuthorize("hasRole('ROLE_VIEW_PROJECTS_PRIVILEGE')")
    @ResponseBody
    public String create(@RequestBody Project project) {
        projectDao.findByName(project.getName()).ifPresent(val -> new CustomException("Project name exist"));
        projectDao.findByProjKey(project.getProjKey()).ifPresent(val -> new CustomException("Project Key exist"));
        sonarController.createProject(project);
        project = projectDao.save(project);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), project);
    }

    @GetMapping("/api/project/findAll")
    @PreAuthorize("hasRole('ROLE_VIEW_PROJECTS_PRIVILEGE')")
    @ResponseBody
    public String userAccess() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), projectDao.findAll());
    }
}
