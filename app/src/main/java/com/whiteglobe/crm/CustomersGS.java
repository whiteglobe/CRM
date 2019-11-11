package com.whiteglobe.crm;

public class CustomersGS {
    int custID;
    String custName,custEmail,custPhone;

    public CustomersGS(int custID, String custName, String custEmail, String custPhone) {
        this.custID = custID;
        this.custName = custName;
        this.custEmail = custEmail;
        this.custPhone = custPhone;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }
}
