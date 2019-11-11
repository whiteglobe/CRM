package com.whiteglobe.crm;

public class AllProductsGS {
    String cat_name;
    int cat_id;

    public AllProductsGS(String cat_name, int cat_id) {
        this.cat_name = cat_name;
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }
}
