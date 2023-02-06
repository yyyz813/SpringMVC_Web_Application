package com.sg.hero_sightings.daos;

import com.sg.hero_sightings.models.Hero;
import com.sg.hero_sightings.models.Sighting;

import java.time.LocalDate;
import java.util.List;

public interface SightingDao {
    Sighting getSightingById(int id);
    List<Sighting> getAllSighting();

    // Returns Sighting with the id assigned to it by the database.
    Sighting addSighting(Sighting sighting);
    void updateSighting(Sighting sighting);
    void deleteSightingById(int id);

    List<Sighting> getSightingsForDate(LocalDate date);
}
