package com.sg.hero_sightings.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Hero {
    private int hero_id;
    @NotBlank(message = "Name must not be empty.")
    @Size(max = 50, message = "Name must be less than 50 characters.")
    private String name;
    @Size(max = 255, message = "Description must be less than 255 characters.")
    private String description;
    private Superpower superpower;

    public int getHero_id() {
        return hero_id;
    }

    public void setHero_id(int hero_id) {
        this.hero_id = hero_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Superpower getSuperpower() {
        return superpower;
    }

    public void setSuperpower(Superpower superpower) {
        this.superpower = superpower;
    }
}
