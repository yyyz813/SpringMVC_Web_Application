package com.sg.hero_sightings.models;

public class Superpower{
    private int superpower_id;

    // Example: "Super agility and flying."
    private String powerName;

    public int getSuperpower_id() {
        return superpower_id;
    }

    public void setSuperpower_id(int superpower_id) {
        this.superpower_id = superpower_id;
    }

    public String getPowerName() {
        return powerName;
    }

    public void setPowerName(String powerName) {
        this.powerName = powerName;
    }
}
