package com.whiteglobe.crm;

public class ProjectIssueGs {
    String issueTitle,issueDate,issueStatus;
    int issueId;

    public ProjectIssueGs(String issueTitle, String issueDate, String issueStatus, int issueId) {
        this.issueTitle = issueTitle;
        this.issueDate = issueDate;
        this.issueStatus = issueStatus;
        this.issueId = issueId;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }
}
