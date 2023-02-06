package com.sg.hero_sightings.daos;

import com.sg.hero_sightings.models.Hero;
import com.sg.hero_sightings.models.Location;

import java.util.List;

public interface LocationDao {
    Location getLocationById(int id);
    List<Location> getAllLocation(); //Done

    // Returns Location with the id assigned to it by the database.
    Location addLocation(Location location); //Done
    void updateLocation(Location location); //Done
    void deleteLocationById(int id); //Done

    List<Location> getAllLocationsForHero(int heroId);
}
