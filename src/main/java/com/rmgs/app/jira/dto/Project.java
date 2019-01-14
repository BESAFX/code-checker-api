package com.rmgs.app.jira.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    private final static Logger LOG = LoggerFactory.getLogger(Project.class);

    private Long id;
    private String key;
    private String name;
    private String description;
    private String[] projectKeys;
    private ProjectType projectType;

    @SuppressWarnings("unchecked")
    @JsonProperty("projectTypeKey")
    private void unpack(String projectTypeKey) {
        this.projectType = ProjectType.builder().key(projectTypeKey).build();
    }
}
