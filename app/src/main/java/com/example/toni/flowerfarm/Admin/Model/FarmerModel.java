package com.example.toni.flowerfarm.Admin.Model;

/**
 * Created by toni on 3/26/17.
 */

public class FarmerModel {
    private String name;
    private String farm_name;
    private String id_number;
    private String mobile_number;

    public FarmerModel() {
    }

    public FarmerModel(String name, String farm_name, String id_number, String mobile_number) {
        this.name = name;
        this.farm_name = farm_name;
        this.id_number = id_number;
        this.mobile_number = mobile_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFarm_name() {
        return farm_name;
    }

    public void setFarm_name(String farm_name) {
        this.farm_name = farm_name;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }
}
