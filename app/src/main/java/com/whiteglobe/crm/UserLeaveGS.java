package com.whiteglobe.crm;

public class UserLeaveGS {
    int leaveId;
    String leaveReason,leavePostedDate,leaveStatus;

    public UserLeaveGS(int leaveId, String leaveReason, String leavePostedDate, String leaveStatus) {
        this.leaveId = leaveId;
        this.leaveReason = leaveReason;
        this.leavePostedDate = leavePostedDate;
        this.leaveStatus = leaveStatus;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getLeavePostedDate() {
        return leavePostedDate;
    }

    public void setLeavePostedDate(String leavePostedDate) {
        this.leavePostedDate = leavePostedDate;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }
}
