package com.sg.hero_sightings.controllers;

import com.sg.hero_sightings.daos.HeroDao;
import com.sg.hero_sightings.daos.LocationDao;
import com.sg.hero_sightings.daos.OrganizationDao;
import com.sg.hero_sightings.daos.SuperpowerDao;
import com.sg.hero_sightings.models.Hero;
import com.sg.hero_sightings.models.Location;
import com.sg.hero_sightings.models.Organization;
import com.sg.hero_sightings.models.Superpower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;


import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class HeroController {

    private final String HEROES = "heroes";
    HeroDao heroDao;
    SuperpowerDao superpowerDao;

    OrganizationDao organizationDao;

    LocationDao locationDao;

    Set<ConstraintViolation<Hero>> violations = new HashSet<>();

    @Autowired
    public HeroController(HeroDao heroDao, SuperpowerDao superpowerDao, OrganizationDao organizationDao, LocationDao locationDao) {
        this.heroDao = heroDao;
        this.superpowerDao = superpowerDao;
        this.organizationDao = organizationDao;
        this.locationDao = locationDao;
    }

    @GetMapping(HEROES)
    public String displayHeroes(Model model) {
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Superpower> superpowers = superpowerDao.getAllSuperpower();
        prepOrganizations(model);
        prepLocations(model);
        model.addAttribute("heroes", heroes);
        model.addAttribute("superpowers", superpowers);
        model.addAttribute("errors", violations);
        return HEROES;
    }

    private void prepOrganizations(Model model) {
        List<Organization> allOrganizations = organizationDao.getAllOrganization();
        Organization organization = new Organization();
        organization.setOrganization_id(0);
        organization.setName("ALL ORGANIZATIONS");
        allOrganizations.add(0, organization);
        model.addAttribute("organizations", allOrganizations);
    }

    private void prepLocations(Model model) {
        List<Location> allLocations = locationDao.getAllLocation();
        Location location = new Location();
        location.setLocation_id(0);
        location.setName("ALL LOCATIONS");
        allLocations.add(0, location);
        model.addAttribute("locations", allLocations);
    }

    @GetMapping("filterHeroByOrganization")
    public String displayHeroesByOrganization(Integer orgId, Model model) {
        List<Hero> heroes;
        if (orgId > 0) {
            heroes = heroDao.getAllHeroesForOrganization(orgId);
        } else {
            heroes = heroDao.getAllHeroes();
        }
        List<Superpower> superpowers = superpowerDao.getAllSuperpower();
        prepOrganizations(model);
        prepLocations(model);
        model.addAttribute("heroes", heroes);
        model.addAttribute("superpowers", superpowers);
        model.addAttribute("errors", violations);
        model.addAttribute("orgId", orgId);
        return HEROES;
    }

    @GetMapping("filterHeroByLocations")
    public String filterHeroByLocations(Integer locId, Model model) {
        List<Hero> heroes;
        if (locId > 0) {
            heroes = heroDao.getAllHeroesForLocation(locId);
        } else {
            heroes = heroDao.getAllHeroes();
        }
        List<Superpower> superpowers = superpowerDao.getAllSuperpower();
        prepOrganizations(model);
        prepLocations(model);
        model.addAttribute("heroes", heroes);
        model.addAttribute("superpowers", superpowers);
        model.addAttribute("errors", violations);
        model.addAttribute("locId", locId);
        return HEROES;
    }

    @PostMapping("addHero")
    public String addHero(Hero hero, BindingResult result, HttpServletRequest request) {
        String powerId = request.getParameter("superpower_id");
        hero.setSuperpower(superpowerDao.getSuperpowerById(Integer.parseInt(powerId)));
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(hero);

        if(violations.isEmpty()) {
            heroDao.addHero(hero);
        }

        return "redirect:/heroes";
    }

    @GetMapping("deleteHero")
    public String deleteHero(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        heroDao.deleteHeroById(id);

        return "redirect:/heroes";
    }

    @GetMapping("editHero")
    public String editHero(Integer id, Model model) {
        Hero hero = heroDao.getHeroById(id);
        List<Superpower> superpowers = superpowerDao.getAllSuperpower();
        model.addAttribute("hero", hero);
        model.addAttribute("superpowers", superpowers);
        return "editHero";
    }

    @PostMapping("editHero")
    public String performEditHero(@Valid Hero hero, BindingResult result, HttpServletRequest request, Model model) {
        hero.setHero_id(Integer.parseInt(request.getParameter("id")));
        hero.setSuperpower(superpowerDao.getSuperpowerById(Integer.parseInt(request.getParameter("superpower_id"))));

        if (result.hasErrors()) {
            model.addAttribute("superpowers", superpowerDao.getAllSuperpower());
            return "editHero";
        }
        heroDao.updateHero(hero);

        return "redirect:/heroes";
    }

}
