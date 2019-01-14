package com.rmgs.app.jira;

import com.google.common.collect.Lists;
import com.rmgs.app.jira.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;


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

    @GetMapping("/findProjectIssues")
    @ApiOperation(
            nickname = "findProjectIssues",
            value = "Authorized",
            notes = "Fetching All Project Issues (Search By Project Name and Issue Type) - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public IssueWrapper findProjectIssues(
            @ApiParam(value = "Project Key", required = true)
            @RequestParam String projectKey,
            @ApiParam(value = "Issue Type")
            @RequestParam(required = false) String issuetype,
            @ApiParam(value = "Priority")
            @RequestParam(required = false) String priority,
            @ApiParam(value = "Developer Name")
            @RequestParam(required = false) String developer) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(jiraApiUrl + "/rest/api/latest/search?");
        urlBuilder.append("jql= ");
        urlBuilder.append("project = " + projectKey);
        Optional.ofNullable(issuetype).ifPresent(val -> {
            urlBuilder.append(" and ");
            urlBuilder.append("issuetype = " + val);
        });
        Optional.ofNullable(priority).ifPresent(val -> {
            urlBuilder.append(" and ");
            urlBuilder.append("priority = " + val);
        });
        Optional.ofNullable(developer).ifPresent(val -> {
            urlBuilder.append(" and ");
            urlBuilder.append("creator = " + val);
        });
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        ResponseEntity<IssueWrapper> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, request, IssueWrapper.class);
        return response.getBody();
    }

    @GetMapping("/findProjectStatuses")
    @ApiOperation(
            nickname = "findProjectStatuses",
            value = "Authorized",
            notes = "Fetching All Project Statuses (Search By Project Name) - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<IssueType> findProjectStatuses(
            @ApiParam(value = "Project Key", required = true)
            @RequestParam String projectKey) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(jiraApiUrl + "/rest/api/latest/project/" + projectKey + "/statuses");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        ResponseEntity<IssueType[]> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, request, IssueType[].class);
        return Lists.newArrayList(response.getBody());
    }

    @GetMapping("/findAllProjects")
    @ApiOperation(
            nickname = "findAllJiraProjects",
            value = "Authorized",
            notes = "Fetching All Jira Projects - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<Project> findAllProjects() {
        String url = jiraApiUrl + "/rest/api/latest/project";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("expand", "description,lead,url,projectKeys");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        ResponseEntity<Project[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, Project[].class);
        return Lists.newArrayList(response.getBody());
    }

    @GetMapping("/findAllProjectTypes")
    @ApiOperation(
            nickname = "findAllJiraProjectTypes",
            value = "Authorized",
            notes = "Fetching All Jira Project Types - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<ProjectType> findAllProjectTypes() {
        String url = jiraApiUrl + "/rest/api/latest/project/type";
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        ResponseEntity<ProjectType[]> response = restTemplate.exchange(url, HttpMethod.GET, request, ProjectType[].class);
        return Lists.newArrayList(response.getBody());
    }

    @GetMapping("/findAllIssueTypes")
    @ApiOperation(
            nickname = "findAllJiraIssueTypes",
            value = "Authorized",
            notes = "Fetching All Jira Issue Types - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<IssueType> findAllIssueTypes() {
        String url = jiraApiUrl + "/rest/api/latest/issuetype";
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        ResponseEntity<IssueType[]> response = restTemplate.exchange(url, HttpMethod.GET, request, IssueType[].class);
        return Lists.newArrayList(response.getBody());
    }

    @GetMapping("/findAllPriorities")
    @ApiOperation(
            nickname = "findAllPriorities",
            value = "Authorized",
            notes = "Fetching All Jira Priorities - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<Priority> findAllPriorities() {
        String url = jiraApiUrl + "/rest/api/latest/priority";
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        ResponseEntity<Priority[]> response = restTemplate.exchange(url, HttpMethod.GET, request, Priority[].class);
        return Lists.newArrayList(response.getBody());
    }

    @GetMapping("/findAllUsers")
    @ApiOperation(
            nickname = "findAllJiraUsers",
            value = "Authorized",
            notes = "Fetching All Jira Users - Required Authorities",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<User> findAllUsers() {
        String url = jiraApiUrl + "/rest/api/latest/user/search";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("startAt", 0);
        builder.queryParam("maxResults", 1000);
        builder.queryParam("username", ".");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
        ResponseEntity<User[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, User[].class);
        return Lists.newArrayList(response.getBody());
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
