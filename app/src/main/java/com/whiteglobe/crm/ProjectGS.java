package com.whiteglobe.crm;

public class ProjectGS {
    String projectUnique,projectName,projectOverview,projectClearenceDate,projectStartDate,projectEndDate,projectStatus;

    public ProjectGS(String projectUnique, String projectName, String projectOverview, String projectClearenceDate, String projectStartDate, String projectEndDate, String projectStatus) {
        this.projectUnique = projectUnique;
        this.projectName = projectName;
        this.projectOverview = projectOverview;
        this.projectClearenceDate = projectClearenceDate;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.projectStatus = projectStatus;
    }

    public String getProjectUnique() {
        return projectUnique;
    }

    public void setProjectUnique(String projectUnique) {
        this.projectUnique = projectUnique;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectOverview() {
        return projectOverview;
    }

    public void setProjectOverview(String projectOverview) {
        this.projectOverview = projectOverview;
    }

    public String getProjectClearenceDate() {
        return projectClearenceDate;
    }

    public void setProjectClearenceDate(String projectClearenceDate) {
        this.projectClearenceDate = projectClearenceDate;
    }

    public String getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(String projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public String getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(String projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }
}
