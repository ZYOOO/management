package com.managementSystem.domain;

import java.io.Serializable;

/**
 * @author ZYOOO
 * @date 2021-09-12 16:56
 */
public class User implements Serializable {
    private int ID;
    private String name;
    private String unit;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
