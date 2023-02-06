package com.sg.hero_sightings.daos;

import com.sg.hero_sightings.models.Hero;
import com.sg.hero_sightings.models.Location;
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
public class LocationDaoImpl implements com.sg.hero_sightings.daos.LocationDao {
    @Autowired
    private final JdbcTemplate jdbc;

    public LocationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    @Override
    public Location getLocationById(int id) {
        try {
            final String SELECT_LOCATION_BY_ID = "SELECT * FROM location WHERE location_id=?;";
            Location location =  jdbc.queryForObject(SELECT_LOCATION_BY_ID, new LocationMapper(), id);
            return location;
        }catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Location> getAllLocation() {
        final String SELECT_ALL_LOCATIONS = "SELECT * FROM location;";
        return jdbc.query(SELECT_ALL_LOCATIONS, new LocationMapper());
    }

    @Override
    public Location addLocation(Location location) {
        final String INSERT_LOCATION = "INSERT INTO location(name, address, description, longitude, latitude) VALUES(?,?,?,?,?)";
        jdbc.update(INSERT_LOCATION,
                location.getName(),
                location.getAddress(),
                location.getDescription(),
                location.getLongitude(),
                location.getLatitude());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setLocation_id(newId);

        return location;
    }

    @Override
    public void updateLocation(Location location) {
        final String UPDATE_LOCATION = "UPDATE location SET location_id = ?, name = ?, address = ?, description = ?, longitude = ?, latitude = ? WHERE id = ?";
        jdbc.update(UPDATE_LOCATION,
                location.getLocation_id(),
                location.getName(),
                location.getAddress(),
                location.getDescription(),
                location.getLongitude(),
                location.getLatitude());
    }

    @Override
    @Transactional
    public void deleteLocationById(int id) {
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE sighting.location_id = ?";
        jdbc.update(DELETE_SIGHTING, id);

        final String DELETE_LOCATION = "DELETE FROM location WHERE location_id = ?;";
        jdbc.update(DELETE_LOCATION, id);
    }

    @Override
    public List<Location> getAllLocationsForHero(int heroId) {
        final String SELECT_ALL_LOCATIONS_FOR_HERO = "SELECT * FROM location l JOIN sighting s ON s.location_id = l.location_id WHERE s.location_id = ?";

        return jdbc.query(SELECT_ALL_LOCATIONS_FOR_HERO, new LocationMapper(), heroId);
    }

    public static final class LocationMapper implements RowMapper<Location> {
        public Location mapRow(ResultSet rs, int index) throws SQLException {
            Location location = new Location();
            location.setLocation_id(rs.getInt("location_id"));
            location.setName(rs.getString("name"));
            location.setAddress(rs.getString("address"));
            location.setDescription(rs.getString("description"));
            location.setLongitude(rs.getDouble("longitude"));
            location.setLatitude(rs.getDouble("latitude"));
            return location;
        }
    }

}
