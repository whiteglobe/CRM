package com.whiteglobe.crm;

public class ProjectTasksGS {
    String taskTitle,taskDate;
    int taskID;

    public ProjectTasksGS(String taskTitle, String taskDate, int taskID) {
        this.taskTitle = taskTitle;
        this.taskDate = taskDate;
        this.taskID = taskID;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
