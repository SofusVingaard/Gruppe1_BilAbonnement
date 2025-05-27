package org.example.bilabonnement_gruppe1.controller;


import jakarta.servlet.http.HttpSession;
import org.example.bilabonnement_gruppe1.model.Damage;
import org.example.bilabonnement_gruppe1.repository.DamageReportRepository;
import org.example.bilabonnement_gruppe1.repository.DamageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


// Sofus
@Controller
@RequestMapping("/damageReport")
public class DamageReportController {



    @Autowired
    private DamageRepository damageRepository;


    // Sofus
    @GetMapping("/damageReport")
    public String showDamageReport(HttpSession session,
                                   Model model,
                                   @RequestParam("damageReportId") int damageReportId) {

        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }

        List<Damage> damageList = damageRepository.getDamageList(damageReportId);
        double totalPrice = damageRepository.getRepairCost(damageReportId);

        model.addAttribute("damageList", damageList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("damageReportId", damageReportId);

        return "damageReport";
    }

    // Sofus
    @PostMapping("/damage")
    public String createDamage(@RequestParam("damageReportId") int damageReportId,
                               @RequestParam("damageType") String damageType,
                               @RequestParam("price") double price){

        Damage damage = new Damage(damageReportId, damageType, price);
        damageRepository.createDamage(damage);


        return "redirect:/damageReport/damageReport?damageReportId=" + damageReportId;
    }

    // Sofus
    @PostMapping("/delete")
    public String deleteDamage(@RequestParam("damageReportId") int damageReportId,
                               @RequestParam("damageId") int damageId) {

        damageRepository.deleteDamage(damageId);

        return "redirect:/damageReport/damageReport?damageReportId=" + damageReportId;

    }
}
