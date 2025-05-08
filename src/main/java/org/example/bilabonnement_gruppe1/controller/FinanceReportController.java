package org.example.bilabonnement_gruppe1.controller;


import org.example.bilabonnement_gruppe1.model.CarFinance;
import org.example.bilabonnement_gruppe1.model.DamageReport;
import org.example.bilabonnement_gruppe1.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/financeReport")
public class FinanceReportController {

    @Autowired
    FinanceRepository financeRepository;

    @GetMapping("/financeReport")
    public String financeReport(){
        return "financeReport";
    }

    @GetMapping("/create")
    public String showCreateFinanceReport(){
        return "createFinanceReport";
    }

    @PostMapping("/create")
    public String createFinanceReport(
            @RequestParam("totalPrice") double totalPrice,
            @RequestParam("damageRportId") int damageReportId,
            @RequestParam("getDate") LocalDate getDate,
            @RequestParam("coeEmission") double co2Emission,
            @RequestParam("kmOverLimit") double kmOverLimit,
            @RequestParam("rentalFee") double rentalFee,
            RedirectAttributes redirectAttributes) {

        CarFinance report = new CarFinance();
        report.setTotalPrice(totalPrice);
        report.setDamageReportId(damageReportId);
        report.setDate(getDate);
        report.setCo2Emissinon(co2Emission);
        report.setKmOverLimit(kmOverLimit);
        report.setRentalFee(rentalFee);

        financeRepository.createFinanceReport(report);
        redirectAttributes.addFlashAttribute("succesMessage", "Rapport oprettet");

        return "redirect:/dashboard";

    }
}
