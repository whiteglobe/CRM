package com.whiteglobe.crm;

public class CompanyDocumentGS {
    String companyDocTitle,companyDocRemarks,companyDocExpiryDate,companyDocName;

    public CompanyDocumentGS(String companyDocTitle, String companyDocRemarks, String companyDocExpiryDate, String companyDocName) {
        this.companyDocTitle = companyDocTitle;
        this.companyDocRemarks = companyDocRemarks;
        this.companyDocExpiryDate = companyDocExpiryDate;
        this.companyDocName = companyDocName;
    }

    public String getCompanyDocTitle() {
        return companyDocTitle;
    }

    public void setCompanyDocTitle(String companyDocTitle) {
        this.companyDocTitle = companyDocTitle;
    }

    public String getCompanyDocRemarks() {
        return companyDocRemarks;
    }

    public void setCompanyDocRemarks(String companyDocRemarks) {
        this.companyDocRemarks = companyDocRemarks;
    }

    public String getCompanyDocExpiryDate() {
        return companyDocExpiryDate;
    }

    public void setCompanyDocExpiryDate(String companyDocExpiryDate) {
        this.companyDocExpiryDate = companyDocExpiryDate;
    }

    public String getCompanyDocName() {
        return companyDocName;
    }

    public void setCompanyDocName(String companyDocName) {
        this.companyDocName = companyDocName;
    }
}
