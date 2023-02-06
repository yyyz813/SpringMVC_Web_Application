package com.sg.hero_sightings.controllers;

import com.sg.hero_sightings.daos.SightingDao;
import com.sg.hero_sightings.models.Sighting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class IndexController {
    SightingDao sightingDao;

    @Autowired
    public IndexController(SightingDao sightingDao) {
        this.sightingDao = sightingDao;
    }

    @GetMapping(value = {"index", "/", "index.html"})
    public String displayIndexAndLast10Sightings(Model model) {
        model.addAttribute("sightings", sightingDao.getAllSighting().stream()
                .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                .limit(10)
                .collect(Collectors.toList()));
        return "index";
    }
}
