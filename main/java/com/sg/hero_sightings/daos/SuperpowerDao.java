package com.sg.hero_sightings.daos;

import com.sg.hero_sightings.models.Hero;
import com.sg.hero_sightings.models.Superpower;

import java.util.List;

public interface SuperpowerDao {

    Superpower getSuperpowerById(int id);
    List<Superpower> getAllSuperpower();

    // Returns Superpower with the id assigned to it by the database.
    Superpower addSuperpower(Superpower superpower);
    void updateSuperpower(Superpower superpower);
    void deleteSuperpowerById(int id);
}
