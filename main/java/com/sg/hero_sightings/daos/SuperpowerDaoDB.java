package com.sg.hero_sightings.daos;

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
public class SuperpowerDaoDB implements SuperpowerDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Superpower getSuperpowerById(int id) {
        try {
            final String SQL_STR = "SELECT * FROM superpower WHERE power_id = ?;";
            return jdbc.queryForObject(SQL_STR, new SuperpowerMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Superpower> getAllSuperpower() {
        final String SQL_STR = "SELECT * FROM superpower;";
        return jdbc.query(SQL_STR, new SuperpowerMapper());
    }

    @Override
    @Transactional
    public Superpower addSuperpower(Superpower superpower) {
        final String INSERT_SUPERPOWER = "INSERT INTO superpower(`power_name`) VALUES (?);";
        jdbc.update(INSERT_SUPERPOWER,
                superpower.getPowerName());

        int powerId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superpower.setSuperpower_id(powerId);
        return superpower;
    }

    @Override
    public void updateSuperpower(Superpower superpower) {
        final String SQL_STR = "UPDATE superpower SET power_name = ? WHERE power_id = ?;";
        jdbc.update(SQL_STR,
                superpower.getPowerName(),
                superpower.getSuperpower_id());

    }

    @Override
    @Transactional // To run multiple queries
    public void deleteSuperpowerById(int id) {
        final String DELETE_HERO= "DELETE FROM hero WHERE hero.power_id = ?;";
        jdbc.update(DELETE_HERO, id);

        final String DELETE_POWER = "DELETE FROM superpower WHERE superpower.power_id = ?;";
        jdbc.update(DELETE_POWER, id);

    }

    public static final class SuperpowerMapper implements RowMapper<Superpower> {
        @Override
        public Superpower mapRow(ResultSet rs, int index) throws SQLException {
            Superpower superpower = new Superpower();
            superpower.setSuperpower_id(rs.getInt("power_id"));
            superpower.setPowerName(rs.getString("power_name"));
            return superpower;
        }
    }
}
