package com.whiteglobe.crm;

public class ProductDetailsGS {
    String prodName,prodDescr,prodContent, prodBrand, prodImage;

    public ProductDetailsGS(String prodName, String prodDescr, String prodContent, String prodBrand, String prodImage) {
        this.prodName = prodName;
        this.prodDescr = prodDescr;
        this.prodContent = prodContent;
        this.prodBrand = prodBrand;
        this.prodImage = prodImage;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdDescr() {
        return prodDescr;
    }

    public void setProdDescr(String prodDescr) {
        this.prodDescr = prodDescr;
    }

    public String getProdContent() {
        return prodContent;
    }

    public void setProdContent(String prodContent) {
        this.prodContent = prodContent;
    }

    public String getProdBrand() {
        return prodBrand;
    }

    public void setProdBrand(String prodBrand) {
        this.prodBrand = prodBrand;
    }

    public String getProdImage() {
        return prodImage;
    }

    public void setProdImage(String prodImage) {
        this.prodImage = prodImage;
    }
}
