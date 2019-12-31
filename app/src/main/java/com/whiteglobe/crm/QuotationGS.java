package com.whiteglobe.crm;

public class QuotationGS {
    String quotNo,partyName,quotDate,lead,amt;

    public QuotationGS(String quotNo, String partyName, String quotDate, String lead, String amt) {
        this.quotNo = quotNo;
        this.partyName = partyName;
        this.quotDate = quotDate;
        this.lead = lead;
        this.amt = amt;
    }

    public String getQuotNo() {
        return quotNo;
    }

    public void setQuotNo(String quotNo) {
        this.quotNo = quotNo;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getQuotDate() {
        return quotDate;
    }

    public void setQuotDate(String quotDate) {
        this.quotDate = quotDate;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }
}
