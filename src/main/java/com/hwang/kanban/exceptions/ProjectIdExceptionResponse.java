package com.hwang.kanban.exceptions;

public class ProjectIdExceptionResponse {

    private String projectIdentifier;

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }

    public ProjectIdExceptionResponse(String projectIdentifier){
        this.projectIdentifier = projectIdentifier;
    }
}
