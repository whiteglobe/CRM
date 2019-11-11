package com.whiteglobe.crm;

public class ProjectDocumentsGS {
    int docId;
    String docName,docDate,docUploadedBy;

    public ProjectDocumentsGS(int docId, String docName, String docDate, String docUploadedBy) {
        this.docId = docId;
        this.docName = docName;
        this.docDate = docDate;
        this.docUploadedBy = docUploadedBy;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public String getDocUploadedBy() {
        return docUploadedBy;
    }

    public void setDocUploadedBy(String docUploadedBy) {
        this.docUploadedBy = docUploadedBy;
    }
}
