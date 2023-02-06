package com.sg.hero_sightings.daos;

import com.sg.hero_sightings.models.Organization;

import java.util.List;

public interface OrganizationDao {
    Organization getOrganizationById(int id);

    List<Organization> getAllOrganization();

    // Returns Organization with the id assigned to it by the database.
    Organization addOrganization(Organization organization);
    Organization updateOrganization(Organization organization);
    void deleteOrganizationById(int id);
    List<Organization> getOrganizationsForHero(int heroId);
}
