package com.rmgs.app.jira.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String key;
    private String name;
    private String emailAddress;
    private String displayName;
    private Boolean active;
}
