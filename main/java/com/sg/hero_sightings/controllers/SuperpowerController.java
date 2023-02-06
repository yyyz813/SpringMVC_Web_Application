package com.sg.hero_sightings.controllers;


import com.sg.hero_sightings.daos.SuperpowerDao;
import com.sg.hero_sightings.models.Superpower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SuperpowerController {

    @Autowired
    SuperpowerDao superpowerDao;

    @GetMapping("superpowers")
    public String displayAllSuperpowers(Model model) {
        List<Superpower> superpowersList = superpowerDao.getAllSuperpower();
        model.addAttribute("superpowers", superpowersList);
        return "superpowers";
    }

    // httpservlet get data from form
    @GetMapping("removepower")
    public String deleteSuperpower(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        superpowerDao.deleteSuperpowerById(id);
        return "redirect:superpowers";
    }

    @PostMapping("addSuperpower")    // has to match the form action attr
    public String submitSuperpower(HttpServletRequest request) {
        String powerName = request.getParameter("powerName");  // I get the value from input:name attribute

        Superpower newPower = new Superpower();
        newPower.setPowerName(powerName);

        superpowerDao.addSuperpower(newPower);

        return "redirect:superpowers";
    }

    @GetMapping("editpower")
    public String updateSuperpower(HttpServletRequest request, Model model){
        String id = request.getParameter("id");
        Superpower superpower = superpowerDao.getSuperpowerById(Integer.parseInt(id));
        model.addAttribute("power", superpower);   // (attribute name, key data) key value pair grabbed earlier
        return "editpower";
    }

    @PostMapping("editPower")
    public String performEditPower(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        String powerName = request.getParameter("powerName");
        Superpower superpower = superpowerDao.getSuperpowerById(id);

        superpower.setPowerName(powerName);
        superpowerDao.updateSuperpower(superpower);
        return "redirect:superpowers";
    }


}
