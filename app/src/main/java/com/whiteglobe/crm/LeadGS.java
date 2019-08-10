package com.whiteglobe.crm;

public class LeadGS {

    int leadID;
    String title,name,phoneno;

    public LeadGS(){}

    public LeadGS(int leadID, String title, String name, String phoneno) {
        this.leadID = leadID;
        this.title = title;
        this.name = name;
        this.phoneno = phoneno;
    }

    public int getLeadID() {
        return leadID;
    }

    public void setLeadID(int leadID) {
        this.leadID = leadID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
}
