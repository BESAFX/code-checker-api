package com.rmgs.app.jira.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueType {

    private Long id;
    private String name;
    private String description;
    private Boolean subtask;
    private List<Status> statuses;
}
