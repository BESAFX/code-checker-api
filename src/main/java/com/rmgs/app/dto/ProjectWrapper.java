package com.rmgs.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectWrapper {

    private Paging paging;
    private List<Component> components;

}
