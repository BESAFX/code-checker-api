package com.rmgs.app.jira;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/jira")
public class JiraRestAPI {

    @Value("${jira.api.http.host}")
    private String jiraApiUrl;

    private RestTemplate restTemplate;

    private HttpEntity<String> request;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        String plainCreds = "bassam.mahdy@flairstech.com:besa2009";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        request = new HttpEntity<>(headers);
    }

    @GetMapping("/bugs")
    public ResponseEntity<Object> getAllBugs() {
        ResponseEntity<Object> responseEntity = null;
        try {
            String url = jiraApiUrl + "/rest/api/latest/search?jql=project=COD and issuetype = Bug";
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, Object.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw e;
        }
        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        ResponseEntity<Object> responseEntity = null;
        try {
            String url = jiraApiUrl + "/rest/api/latest/user/search?startAt=0&maxResults=1000&username=.";
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, Object.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw e;
        }
        return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
    }


}
