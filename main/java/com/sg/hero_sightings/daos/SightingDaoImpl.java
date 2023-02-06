
package com.sg.hero_sightings.daos;

import com.sg.hero_sightings.models.Hero;
import com.sg.hero_sightings.models.Location;
import com.sg.hero_sightings.models.Sighting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class SightingDaoImpl implements SightingDao {

    @Autowired
    JdbcTemplate jdbc;


    @Override
    public Sighting getSightingById(int id) {
        try {
            final String GET_SIGHTING_BY_ID = "SELECT * FROM sighting WHERE sighting_id = ?";
            Sighting sighting = jdbc.queryForObject(GET_SIGHTING_BY_ID, new SightingMapper(), id);
            sighting.setHero(getHeroForSighting(id));
            sighting.setLocation(getLocationForSighting(id));
            return sighting;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    private Location getLocationForSighting(int id) {
        final String SELECT_LOCATION = "SELECT l.* FROM location l JOIN sighting s ON s.location_id = l.location_id WHERE s.sighting_id = ?";
        return jdbc.queryForObject(SELECT_LOCATION, new LocationDaoImpl.LocationMapper(), id);
    }

    private Hero getHeroForSighting(int id) {
        final String SELECT_SUPER_HUMAN = "SELECT h.* FROM hero h JOIN sighting s ON s.hero_id = h.hero_id WHERE s.sighting_id = ?";
        return jdbc.queryForObject(SELECT_SUPER_HUMAN, new HeroDaoImpl.HeroMapper(), id);
    }

    @Override
    public List<Sighting> getAllSighting() {
        final String GET_ALL_SIGHTINGS = "SELECT * FROM sighting";
        List<Sighting> sightings = jdbc.query(GET_ALL_SIGHTINGS, new SightingMapper());
        associateHeroWithLocations(sightings);
        return sightings;
    }

    private void associateHeroWithLocations(List<Sighting> sightings) {
        for (Sighting sighting : sightings) {
            sighting.setLocation(getLocationForSighting(sighting.getSighting_id()));
            sighting.setHero(getHeroForSighting(sighting.getSighting_id()));
        }
    }

    @Override
    public Sighting addSighting(Sighting sighting) {
        final String INSERT_SIGHTING = "INSERT INTO sighting (date, location_id, hero_id) VALUES (?,?,?)";
        jdbc.update(INSERT_SIGHTING,
                sighting.getDate(),
                sighting.getLocation().getLocation_id(),
                sighting.getHero().getHero_id());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setSighting_id(newId);
        return sighting;
    }

    @Override
    public void updateSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = " UPDATE sighting SET  hero_id = ?, location_id = ?  WHERE sighting_id = ? ";
        jdbc.update(UPDATE_SIGHTING,
                sighting.getHero().getHero_id(),
                sighting.getLocation().getLocation_id(),
                sighting.getSighting_id());
    }


    @Override
    public void deleteSightingById(int id) {
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE sighting_id = ?";
        jdbc.update(DELETE_SIGHTING, id);
    }

    @Override
    public List<Sighting> getSightingsForDate(LocalDate date) {
        final String GET_SIGHTINGS_BY_DATE = "SELECT * FROM sighting WHERE date = ?";
        List<Sighting> sightings = jdbc.query(GET_SIGHTINGS_BY_DATE, new SightingMapper(), date);
        associateHeroWithLocations(sightings);
        return sightings;
    }

    public static final class SightingMapper implements RowMapper<Sighting> {
        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            LocalDate localDate = LocalDate.parse(rs.getString("date"));
            Sighting sighting = new Sighting();
            sighting.setSighting_id(rs.getInt("sighting_id"));
            sighting.setDate(localDate);
            return sighting;
        }
    }
}