package com.whiteglobe.crm;

public class ExhibitionGS {
    String exhbId,exhbImg1,exhbImg2,partyName,phoneNo;

    public ExhibitionGS(String exhbId, String exhbImg1, String exhbImg2, String partyName, String phoneNo) {
        this.exhbId = exhbId;
        this.exhbImg1 = exhbImg1;
        this.exhbImg2 = exhbImg2;
        this.partyName = partyName;
        this.phoneNo = phoneNo;
    }

    public String getExhbId() {
        return exhbId;
    }

    public void setExhbId(String exhbId) {
        this.exhbId = exhbId;
    }

    public String getExhbImg1() {
        return exhbImg1;
    }

    public void setExhbImg1(String exhbImg1) {
        this.exhbImg1 = exhbImg1;
    }

    public String getExhbImg2() {
        return exhbImg2;
    }

    public void setExhbImg2(String exhbImg2) {
        this.exhbImg2 = exhbImg2;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
