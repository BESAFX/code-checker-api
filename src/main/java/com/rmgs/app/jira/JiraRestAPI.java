package com.rmgs.app.jira;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/jira")
@Api(
        value = "/api/jira",
        description = "JIRA REST API",
        tags = {"JIRA Integration"}
)
public class JiraRestAPI {

    private final static Logger LOG = LoggerFactory.getLogger(JiraRestAPI.class);

    @Value("${jira.api.http.host}")
    private String jiraApiUrl;

    private RestTemplate restTemplate;

    private HttpHeaders headers;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setBasicAuth("bassam.mahdy", "P@$$w0rd");
    }

    @GetMapping("/findAllBugs")
    @ApiOperation(
            nickname = "findAllJiraBugs",
            value = "Authorized",
            notes = "Fetching All Jira Bugs - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> findAllBugs() {
        return null;
    }

    @GetMapping("/findAllProjects")
    @ApiOperation(
            nickname = "findAllJiraProjects",
            value = "Authorized",
            notes = "Fetching All Jira Projects - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> findAllProjects() {
        String url = jiraApiUrl + "/rest/api/latest/project";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("expand", "description,lead,url,projectKeys");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, Object.class);
        } catch (Exception ex) {
            LOG.info(ExceptionUtils.getMessage(ex));
            return new ResponseEntity<>(ExceptionUtils.getMessage(ex), HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/findAllIssueTypes")
    @ApiOperation(
            nickname = "findAllJiraIssueTypes",
            value = "Authorized",
            notes = "Fetching All Jira Issue Types - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> findAllIssueTypes() {
        String url = jiraApiUrl + "/rest/api/latest/issuetype";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, Object.class);
        return response;
    }

    @GetMapping("/findAllUsers")
    @ApiOperation(
            nickname = "findAllJiraUsers",
            value = "Authorized",
            notes = "Fetching All Jira Users - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> findAllUsers() {
        String url = jiraApiUrl + "/rest/api/latest/user/search";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("startAt", 0);
        builder.queryParam("maxResults", 1000);
        builder.queryParam("username", ".");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, Object.class);
        return response;
    }

    @GetMapping("/findAllProjectPermission")
    @ApiOperation(
            nickname = "findAllJiraProjectPermissions",
            value = "Authorized",
            notes = "Fetching All Jira Project Permissions - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> findProjectPermissions(
            @ApiParam(value = "Project Key Required", required = true)
            @RequestParam String projectKey) {
        String url = jiraApiUrl + "/rest/api/latest/mypermissions";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("projectKey", projectKey);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, Object.class);
        return response;
    }


}
