package org.example.bilabonnement_gruppe1.controller;


import org.example.bilabonnement_gruppe1.model.CarFinance;
import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.example.bilabonnement_gruppe1.repository.FinanceRepository;
import org.example.bilabonnement_gruppe1.repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/financeReport")
public class FinanceReportController {

    @Autowired
    FinanceRepository financeRepository;
    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;

    @GetMapping("/financeReport")
    public String financeReport(){
        return "financeReport";
    }
    @GetMapping
    public String showAllFinanceReports(Model model) {
        List<CarFinance> reports = financeRepository.findAll();
        model.addAttribute("reports", reports);
        return "financeReport"; // navn på HTML-siden
    }

    @GetMapping("/create")
    public String showCreateFinanceReport(){
        return "createFinanceReport";
    }

    /*@PostMapping("/create")
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

     */
    @PostMapping("/create")
    public String createFinanceReport(@RequestParam("rentalAgreementId") int id,
                                      RedirectAttributes redirectAttributes) {

        // Hent lejeaftale
        RentalAgreement rental = rentalAgreementRepository.findById(id);

        // Stop hvis ikke fundet
        if (rental == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lejeaftale ikke fundet.");
            return "redirect:/financeReport/create";
        }

        // Opret CarFinance
        CarFinance report = new CarFinance();
        report.setDate(LocalDate.now());
        report.setCo2Emission(report.getCo2Emission());
        report.setKmOverLimit(report.getKmOverLimit());
        report.setRentalFee((report.getRentalFee()));

        // Totalpris: enkel beregning
        double totalPrice = report.getRentalFee() + rental.getKmOverLimit() * 2;
        report.setTotalPrice(totalPrice);

        financeRepository.createFinanceReport(report);

        redirectAttributes.addFlashAttribute("succesMessage", "Finansrapport oprettet.");
        return "redirect:/dashboard";
    }
}

