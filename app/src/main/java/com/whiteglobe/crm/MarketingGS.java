package com.whiteglobe.crm;

public class MarketingGS {
    int mktID;
    String mktName,mktPhone,mktAddress;

    public MarketingGS(int mktID, String mktName, String mktPhone, String mktAddress) {
        this.mktID = mktID;
        this.mktName = mktName;
        this.mktPhone = mktPhone;
        this.mktAddress = mktAddress;
    }

    public int getMktID() {
        return mktID;
    }

    public void setMktID(int mktID) {
        this.mktID = mktID;
    }

    public String getMktName() {
        return mktName;
    }

    public void setMktName(String mktName) {
        this.mktName = mktName;
    }

    public String getMktPhone() {
        return mktPhone;
    }

    public void setMktPhone(String mktPhone) {
        this.mktPhone = mktPhone;
    }

    public String getMktAddress() {
        return mktAddress;
    }

    public void setMktAddress(String mktAddress) {
        this.mktAddress = mktAddress;
    }
}
