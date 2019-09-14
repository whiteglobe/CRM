package com.whiteglobe.crm;

public class ProjectImageGS {
    String imgName,imgRemarks,imgDate,projectUnique;

    public ProjectImageGS(String imgName, String imgRemarks, String imgDate, String projectUnique) {
        this.imgName = imgName;
        this.imgRemarks = imgRemarks;
        this.imgDate = imgDate;
        this.projectUnique = projectUnique;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgRemarks() {
        return imgRemarks;
    }

    public void setImgRemarks(String imgRemarks) {
        this.imgRemarks = imgRemarks;
    }

    public String getImgDate() {
        return imgDate;
    }

    public void setImgDate(String imgDate) {
        this.imgDate = imgDate;
    }

    public String getProjectUnique() {
        return projectUnique;
    }

    public void setProjectUnique(String projectUnique) {
        this.projectUnique = projectUnique;
    }
}
