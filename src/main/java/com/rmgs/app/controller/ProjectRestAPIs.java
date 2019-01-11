package com.rmgs.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.rmgs.app.config.CustomException;
import com.rmgs.app.dao.ProjectDao;
import com.rmgs.app.model.Project;
import com.rmgs.app.sonar.SonarController;
import com.rmgs.app.ws.Notification;
import com.rmgs.app.ws.NotificationService;
import com.rmgs.app.ws.NotificationType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/project")
@Api(
        value = "/api/project",
        description = "Projects REST API",
        tags = {"Rest API - Projects"}
)
public class ProjectRestAPIs {

    private final String FILTER_TABLE = "**";

    @Autowired
    private SonarController sonarController;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ProjectDao projectDao;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_VIEW_PROJECTS_PRIVILEGE')")
    @ResponseBody
    @ApiOperation(
            nickname = "createProject",
            value = "",
            notes = "Create New Project - Required Authorities",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String create(
            @ApiParam(value = "Project Information", required = true)
            @RequestBody Project project
    ) {
        projectDao.findByName(project.getName()).ifPresent(val -> new CustomException("Project name exist"));
        projectDao.findByProjKey(project.getProjKey()).ifPresent(val -> new CustomException("Project Key exist"));
        sonarController.createProject(project);
        project = projectDao.save(project);
        notificationService.notifyAll(Notification
                .builder()
                .title("Project")
                .message("Project Created Successfully")
                .type(NotificationType.information)
                .icon("add")
                .build()
        );
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), project);
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ROLE_VIEW_PROJECTS_PRIVILEGE')")
    @ResponseBody
    @ApiOperation(
            nickname = "getAllProjects",
            value = "",
            notes = "Find All Projects - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String findAll() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), projectDao.findAll());
    }
}
