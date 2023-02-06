package com.sg.hero_sightings.daos;

import com.sg.hero_sightings.models.Organization;
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
public class OrganizationDaoDBImpl implements OrganizationDao{

    @Autowired
    JdbcTemplate jdbc;
    public OrganizationDaoDBImpl (JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    @Override
    public Organization getOrganizationById(int id) {
        try {
            final String GET_ORGANIZATION_BY_ID = "SELECT * FROM organization WHERE organization_id = ?";
            return jdbc.queryForObject(GET_ORGANIZATION_BY_ID, new OrganizationMapper(), id);
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Organization> getAllOrganization() {
        final String GET_ALL_ORGANIZATIONS = "SELECT * FROM organization";
        return jdbc.query(GET_ALL_ORGANIZATIONS, new OrganizationMapper());
    }

    @Override
    @Transactional
    public Organization addOrganization(Organization organization) {
        final String INSERT_ORGANIZATION = "INSERT INTO organization(name, description, address) " +
                "VALUES(?,?,?)";
        jdbc.update(INSERT_ORGANIZATION,
                organization.getName(),
                organization.getDescription(),
                organization.getAddress());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organization.setOrganization_id(newId);
        return organization;
    }

    @Override
    public Organization updateOrganization(Organization organization) {
        final String UPDATE_ORGANIZATION = "UPDATE organization SET name = ?, description = ?, " +
                "address = ? WHERE organization_id = ?";
        jdbc.update(UPDATE_ORGANIZATION,
                organization.getName(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getOrganization_id());
        return organization;
    }

    @Override
    @Transactional
    public void deleteOrganizationById(int id) {
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM hero_organization WHERE organization_id = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, id);
        final String DELETE_ORGANIZATION = "DELETE FROM organization WHERE organization_id = ?";
        jdbc.update(DELETE_ORGANIZATION, id);

    }

    @Override
    public List<Organization> getOrganizationsForHero(int heroId) {
        final String SELECT_ORGANIZATIONS = "SELECT o.* FROM organization o JOIN hero s ON s.organization_id = o.organization_id WHERE s.hero_id = ?";
        return jdbc.query(SELECT_ORGANIZATIONS, new OrganizationMapper(), heroId);
    }

    public static final class OrganizationMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int index) throws SQLException {
            Organization organization= new Organization();
            organization.setOrganization_id(rs.getInt("organization_id"));
            organization.setName(rs.getString("name"));
            organization.setDescription(rs.getString("description"));
            organization.setAddress(rs.getString("address"));

            return organization;
        }
    }

}
