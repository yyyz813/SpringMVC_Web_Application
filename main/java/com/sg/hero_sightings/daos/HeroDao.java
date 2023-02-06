package com.sg.hero_sightings.daos;

import com.sg.hero_sightings.models.Hero;

import java.util.List;

public interface HeroDao {

    Hero getHeroById(int id);

    List<Hero> getAllHeroes();

    // Returns Hero with the id assigned to it by the database.
    Hero addHero(Hero hero);
    void updateHero(Hero hero);
    void deleteHeroById(int id);
    List<Hero> getAllHeroesForLocation(int locationId);
    List<Hero> getAllHeroesForOrganization(int organizationId);

}
