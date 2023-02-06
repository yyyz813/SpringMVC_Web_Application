package com.sg.hero_sightings.controllers;

import com.sg.hero_sightings.daos.OrganizationDao;
import com.sg.hero_sightings.models.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrganizationController {
    private final String ORGANIZATIONS_PLURAL = "organizations";
    @Autowired
    OrganizationDao organizationDao;

    @GetMapping(ORGANIZATIONS_PLURAL)
    public String displayOrganizations(Model model) {
        List<Organization> organizations = organizationDao.getAllOrganization();
        model.addAttribute(ORGANIZATIONS_PLURAL, organizations);
        return ORGANIZATIONS_PLURAL;
    }


    @PostMapping("addOrganization")
    public String addOrganization(HttpServletRequest request){

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String address = request.getParameter("address");

        Organization organization = new Organization();
        organization.setName(name);
        organization.setDescription(description);
        organization.setAddress(address);

        organizationDao.addOrganization(organization);

        return "redirect:/" + ORGANIZATIONS_PLURAL;

    }
    @GetMapping("editOrganization")
    public String editOrganization(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Organization organization = organizationDao.getOrganizationById(id);
        model.addAttribute("organization", organization);
        return "editOrganizations";
    }
    @PostMapping("save")
    public String performEditOrganization(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Organization organization = organizationDao.getOrganizationById(id);

        organization.setName(request.getParameter("name"));
        organization.setDescription(request.getParameter("description"));
        organization.setAddress(request.getParameter("address"));

        organizationDao.updateOrganization(organization);

        return "redirect:organizations";
    }

    @GetMapping("deleteOrganization")
    public String deleteOrganization(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        organizationDao.deleteOrganizationById(id);
        return "redirect:/"+ ORGANIZATIONS_PLURAL;
    }




}
