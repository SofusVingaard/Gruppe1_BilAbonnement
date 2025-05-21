package org.example.bilabonnement_gruppe1.controller;
import org.example.bilabonnement_gruppe1.model.CarFinance;
import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.example.bilabonnement_gruppe1.repository.DamageRepository;
import org.example.bilabonnement_gruppe1.repository.FinanceRepository;
import org.example.bilabonnement_gruppe1.repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    private DamageRepository damageRepository;

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
    @PostMapping("/create/{rentalAgreementId}")
    public String createFinanceReport(@RequestParam("rentalAgreementId") int rentalAgreementId,
                                      RedirectAttributes redirectAttributes) {
        RentalAgreement agreement = rentalAgreementRepository.getRentalAgreement(rentalAgreementId);

        if (agreement == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lejeaftale ikke fundet.");
            return "redirect:/financeReport";
        }

        double damageCost = damageRepository.getRepairCost(agreement.getDamageReportId());

        CarFinance report = new CarFinance();
        report.setDate(LocalDate.now());
        report.setRentalFee(agreement.getTotalPrice());
        report.setKmOverLimit(agreement.getKmOverLimit());
        report.setCo2Emission(agreement.getCar().getCo2Emission());
        report.setDamageReportId(agreement.getDamageReportId());
        report.setTotalPrice(agreement.getTotalPrice() + damageCost);
        report.setRentalAgreementId(agreement.getId());

        financeRepository.createFinanceReport(report);


        redirectAttributes.addFlashAttribute("successMessage", "Finansrapport oprettet.");
        return "redirect:/financeReport";

    }
    @PostMapping("/markPaid/{id}")
    public String markAsPaid(@PathVariable("id") int id) {
        financeRepository.markAsPaid(id);
        return "redirect:/financeReport";
    }

}

