package com.rmgs.app.jira.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueWrapper {

    private Integer startAt;
    private Integer maxResults;
    private Integer total;
    private List<Issue> issues;
}
