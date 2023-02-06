package com.sg.hero_sightings.controllers;

import com.sg.hero_sightings.daos.HeroDao;
import com.sg.hero_sightings.daos.LocationDao;
import com.sg.hero_sightings.daos.SightingDao;
import com.sg.hero_sightings.models.Hero;
import com.sg.hero_sightings.models.Location;
import com.sg.hero_sightings.models.Sighting;
//import jakarta.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SightingController {
    @Autowired
    SightingDao sightDao;
    @Autowired
    LocationDao locationDao;
    @Autowired
    HeroDao heroDao;

    @GetMapping("/sightings")
    public String displaySightings(Model model) {
       model.addAttribute("locations", locationDao.getAllLocation());
       model.addAttribute("hero", heroDao.getAllHeroes());
       model.addAttribute("sightings", sightDao.getAllSighting());
       return "sightings";
    }

    @GetMapping("deleteSighting")
    public String deleteSighting(Integer id) {
        sightDao.deleteSightingById(id);
        return "redirect:/sightings";
    }
    @GetMapping("viewSighting")
    public String viewSighting(Integer id, Model model) {
        Sighting sight = sightDao.getSightingById(id);
        model.addAttribute("sighting", sight);
        return "sightingDetails";
    }

    @PostMapping("addSighting")
    public String addSighting(HttpServletRequest request) {
        String locationId = request.getParameter("locations");
        String heroId = request.getParameter("superHero");

        Sighting sighting = new Sighting();
        sighting.setDate(LocalDate.parse(request.getParameter("date")));
        sighting.setLocation(locationDao.getLocationById(Integer.parseInt(locationId)));
        sighting.setHero(heroDao.getHeroById(Integer.parseInt(heroId)));

        sightDao.addSighting(sighting);

        return "redirect:/sightings";
    }


    @GetMapping("editSighting")
    public String editSighting(Integer id, Model model) {
        Sighting sight = sightDao.getSightingById(id);
        List<Location> locations = locationDao.getAllLocation();
        List<Hero> supers = heroDao.getAllHeroes();
        model.addAttribute("locations", locations);
        model.addAttribute("hero", supers);
        model.addAttribute("sighting", sight);
        return "editSighting";
    }

    @PostMapping("editSighting")
    public String executeEditSighting(Sighting sight, HttpServletRequest request) {
        sight.setSighting_id(Integer.parseInt(request.getParameter("id")));
        String locationId = request.getParameter("locations");
        String heroId = request.getParameter("superHero");

        sight.setLocation(locationDao.getLocationById(Integer.parseInt(locationId)));
        sight.setHero(heroDao.getHeroById(Integer.parseInt(heroId)));
        sightDao.updateSighting(sight);
        return "redirect:/sightings";
    }

}
