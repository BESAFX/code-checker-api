package com.rmgs.app.jira.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.Data;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    private final static Logger LOG = LoggerFactory.getLogger(Issue.class);

    private Long id;
    private String key;
    private String description;
    private String summary;
    private Date created;
    private Date lastViewed;
    private Date updated;
    private Status status;
    private Priority priority;
    private IssueType issueType;
    private Project project;
    private User creator;
    private User reporter;

    @SuppressWarnings("unchecked")
    @JsonProperty("fields")
    private void unpack(Map<String, Object> fields) {
        try{
            Optional.ofNullable((String) fields.get("description"))
                    .ifPresent(val -> this.description = val);
            Optional.ofNullable((String) fields.get("summary"))
                    .ifPresent(val -> this.summary = val);
            Optional.ofNullable((String) fields.get("created"))
                    .ifPresent(val -> this.created = ISODateTimeFormat.dateTime().parseDateTime(val).toDate());
            Optional.ofNullable((String) fields.get("lastViewed"))
                    .ifPresent(val -> this.lastViewed = ISODateTimeFormat.dateTime().parseDateTime(val).toDate());
            Optional.ofNullable((String) fields.get("updated"))
                    .ifPresent(val -> this.updated = ISODateTimeFormat.dateTime().parseDateTime(val).toDate());

            Gson gson = new Gson();

            Optional.ofNullable((Map<String, String>) fields.get("status"))
                    .ifPresent(val -> this.status = gson.fromJson(new JSONObject(val).toString(), Status.class));

            Optional.ofNullable((Map<String, String>) fields.get("priority"))
                    .ifPresent(val -> this.priority = gson.fromJson(new JSONObject(val).toString(), Priority.class));

            Optional.ofNullable((Map<String, String>) fields.get("issuetype"))
                    .ifPresent(val -> this.issueType = gson.fromJson(new JSONObject(val).toString(), IssueType.class));

            Optional.ofNullable((Map<String, String>) fields.get("project"))
                    .ifPresent(val -> this.project = gson.fromJson(new JSONObject(val).toString(), Project.class));

            Optional.ofNullable((Map<String, String>) fields.get("creator"))
                    .ifPresent(val -> this.creator = gson.fromJson(new JSONObject(val).toString(), User.class));

            Optional.ofNullable((Map<String, String>) fields.get("reporter"))
                    .ifPresent(val -> this.reporter = gson.fromJson(new JSONObject(val).toString(), User.class));

        }catch (Exception ex){

        }
    }
}
