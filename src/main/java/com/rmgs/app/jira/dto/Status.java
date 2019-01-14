package com.rmgs.app.jira.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {

    private Long id;
    private String name;
    private String description;
}
