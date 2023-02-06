package com.sg.hero_sightings.daos;

import com.sg.hero_sightings.daos.SuperpowerDaoDB.SuperpowerMapper;
import com.sg.hero_sightings.models.Hero;
import com.sg.hero_sightings.models.Superpower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HeroDaoImpl implements HeroDao {
    private final JdbcTemplate jdbc;
    SuperpowerDao superpowerDao;

    @Autowired
    public HeroDaoImpl(JdbcTemplate jdbc, SuperpowerDao superpowerDao) {
        this.jdbc = jdbc;
        this.superpowerDao = superpowerDao;
    }

    @Override
    public Hero getHeroById(int hero_id) {
        try {
            final String GET_HERO_BY_ID = "SELECT * FROM hero WHERE hero_id = ?";
            Hero hero = jdbc.queryForObject(GET_HERO_BY_ID, new HeroMapper(), hero_id);
            if (hero != null) {
                hero.setSuperpower(getSuperpowerForHero(hero_id));
            }
            return hero;
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Hero> getAllHeroes() {
        final String GET_ALL_HEROES = "SELECT * FROM hero";
        List<Hero> heroes = jdbc.query(GET_ALL_HEROES, new HeroMapper());
        associateSuperpowerAndHeroes(heroes);
        return heroes;
    }

    private void associateSuperpowerAndHeroes(List<Hero> heroes) {
        for (Hero hero : heroes) {
            hero.setSuperpower(getSuperpowerForHero(hero.getHero_id()));
        }
    }

    private Superpower getSuperpowerForHero(int heroId) {
        final String SELECT_SUPERPOWER_FOR_HERO = "SELECT s.* FROM superpower s "
                + "JOIN hero h ON h.power_id = s.power_id WHERE h.hero_id = ?";
        return jdbc.queryForObject(SELECT_SUPERPOWER_FOR_HERO, new SuperpowerMapper(), heroId);
    }

    @Override
    @Transactional
    public Hero addHero(Hero hero) {
        final String INSERT_HERO = "INSERT INTO hero(name, description, power_id) " +
                "VALUES(?,?,?)";
        jdbc.update(INSERT_HERO,
                hero.getName(),
                hero.getDescription(),
                hero.getSuperpower().getSuperpower_id());

        Integer newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        if (newId != null)
            hero.setHero_id(newId);
        else
            hero.setHero_id(-1);
        return hero;
    }

    @Override
    public void updateHero(Hero hero) {
        final String UPDATE_HERO = "UPDATE hero SET name = ?, description = ?, " +
                "power_id = ? WHERE hero_id = ?";
        jdbc.update(UPDATE_HERO,
                hero.getName(),
                hero.getDescription(),
                hero.getSuperpower().getSuperpower_id(),
                hero.getHero_id());
    }

    @Override
    @Transactional
    public void deleteHeroById(int hero_id) {
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM hero_organization ho WHERE hero_id = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, hero_id);

        final String DELETE_SIGHTINGS_FOR_HERO = "DELETE FROM sighting WHERE hero_id = ?";
        jdbc.update(DELETE_SIGHTINGS_FOR_HERO, hero_id);

        final String DELETE_HERO = "DELETE FROM hero WHERE hero_id = ?";
        jdbc.update(DELETE_HERO, hero_id);
    }

    @Override
    public List<Hero> getAllHeroesForLocation(int locationId) {
        final String SELECT_HEROES_FOR_LOCATION = "SELECT DISTINCT h.* FROM hero h " +
                "INNER JOIN sighting s ON s.hero_id = h.hero_id " +
                "INNER JOIN location l ON s.location_id = l.location_id " +
                "WHERE l.location_id = ?";
        List<Hero> heroes = jdbc.query(SELECT_HEROES_FOR_LOCATION, new HeroMapper(), locationId);
        associateSuperpowerAndHeroes(heroes);
        return heroes;
    }

    @Override
    public List<Hero> getAllHeroesForOrganization(int organizationId) {
        final String SELECT_HEROES_FOR_ORGANIZATION = "SELECT h.* FROM hero h " +
                "INNER JOIN hero_organization ho ON h.hero_id = ho.hero_id " +
                "WHERE ho.organization_id = ?";
        List<Hero> heroes = jdbc.query(SELECT_HEROES_FOR_ORGANIZATION, new HeroMapper(), organizationId);
        associateSuperpowerAndHeroes(heroes);
        return heroes;
    }

    public static class HeroMapper implements RowMapper<Hero> {
        @Override
        public Hero mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hero hero = new Hero();
            hero.setHero_id(rs.getInt("hero_id"));
            hero.setName(rs.getString("name"));
            hero.setDescription(rs.getString("description"));


            return hero;
        }
    }
}
