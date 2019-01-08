package com.rmgs.app.sonar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.rmgs.app.dto.ProjectWrapper;
import com.rmgs.app.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
public class SonarController {

    private final static Logger LOG = LoggerFactory.getLogger(SonarController.class);

    @Value("${api.http.host.sonar}")
    private String sonarUrl;

    private RestTemplate restTemplate;

    private HttpHeaders headers;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setBasicAuth("admin", "admin");
    }

    public Project createProject(Project project) {

        String url = sonarUrl + "api/projects/create";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("name", project.getName());
        builder.queryParam("project", project.getProjKey());
        builder.queryParam("visibility", "public");


        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<Project> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, request, Project.class);
        return response.getBody();
    }

    public ProjectWrapper getProjects(Long analyzedBefore, String projectKeys, String projectQualifiers, Integer page, Integer size) {

        String url = sonarUrl + "api/projects/search";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        Optional.ofNullable(analyzedBefore).ifPresent(val -> builder.queryParam("analyzedBefore", val));
        Optional.ofNullable(projectKeys).ifPresent(val -> builder.queryParam("projects", val));
        Optional.ofNullable(projectQualifiers).ifPresent(val -> builder.queryParam("qualifiers", val));
        Optional.ofNullable(page).ifPresent(val -> builder.queryParam("p", val));
        Optional.ofNullable(size).ifPresent(val -> builder.queryParam("ps", val));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);

        ResponseEntity<ProjectWrapper> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, ProjectWrapper.class);
        return response.getBody();
    }

    public String getHealth() {
        String url = sonarUrl + "api/system/health";
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), "**"), response.getBody());
    }

}
