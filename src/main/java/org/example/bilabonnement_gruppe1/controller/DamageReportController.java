package org.example.bilabonnement_gruppe1.controller;


import jakarta.servlet.http.HttpSession;
import org.example.bilabonnement_gruppe1.model.Damage;
import org.example.bilabonnement_gruppe1.model.DamageReport;
import org.example.bilabonnement_gruppe1.repository.DamageReportRepository;
import org.example.bilabonnement_gruppe1.repository.DamageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping("/damageReport")
public class DamageReportController {


    @Autowired
    private DamageReportRepository damageReportRepository;

    @Autowired
    private DamageRepository damageRepository;

    @GetMapping("/damageReport")
    public String showDamageReport(HttpSession session) {

        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }
        else {
            return "damageReport";
        }
    }

    @PostMapping("/damageReport")
    public String createDamageReport(@RequestParam("userId") int userId,
                                     @RequestParam("kmOverLimit") double kmOverLimit) {

        DamageReport damageReport = new DamageReport(userId, kmOverLimit);
        damageReportRepository.createDamageReport(damageReport);

        damageRepository.getRepairCost(damageReportId);



        return "redirect:/damageReport";
    }

    @GetMapping("/damageList")
    public String showDamageList(Model model,
                                 @RequestParam("damageReportId") int damageReportId) {
        ArrayList<Damage> damageList = damageRepository.findByDamageReportId(damageReportId);
        model.addAttribute("damageList", damageList);
        return "damageList";

    }

    @GetMapping("/damage")
    public String showDamage(HttpSession session) {

        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }
        return "/damage";
    }

    @PostMapping("/damage")
    public String createDamage(@RequestParam("damageReportId") int damageReportId,
                               @RequestParam("damageType") String damageType,
                               @RequestParam("price") double price){

        Damage damage = new Damage(damageReportId, damageType, price);
        damageRepository.createDamage(damage);


        return "redirect:/damageReport";
    }



}
