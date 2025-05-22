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


    @GetMapping
    public String showAllFinanceReports(Model model) {
        List<CarFinance> reports = financeRepository.findAll();
        model.addAttribute("reports", reports);
        return "financeReport";
    }

    @PostMapping("/create")
    public String createFinanceReport(@RequestParam("customerPhoneNumber") int phoneNumber,
                                      RedirectAttributes redirectAttributes) {
        try {
            // Get active rental agreement with all necessary info
            RentalAgreement agreement = rentalAgreementRepository.getActiveRentalAgreementByPhoneNumber(phoneNumber);

            if (agreement == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "No active rental agreement found for this phone number.");
                return "redirect:/financeReport";
            }

            // Calculate total price components
            double rentalFee = agreement.getTotalPrice();
            double damageCost = agreement.getDamageReport() != null ?
                    agreement.getDamageReport().getRepairCost() : 0.0;

            // Calculate additional charges (like km over limit if applicable)
            double additionalCharges = 0.0;
            if (agreement.getCar().isLimited() && agreement.getKmOverLimit() > 0) {
                // Assuming 1 DKK per km over limit - adjust as needed
                additionalCharges = agreement.getKmOverLimit() * 1.0;
            }

            // Create finance report
            CarFinance report = new CarFinance();
            report.setDate(LocalDate.now());
            report.setRentalFee(rentalFee);
            report.setDamagePrice(damageCost);
            report.setAdditionalCharges(additionalCharges);
            report.setTotalPrice(rentalFee + damageCost + additionalCharges);
            report.setRentalAgreementId(agreement.getId());

            financeRepository.createFinanceReport(report);

            redirectAttributes.addFlashAttribute("successMessage", "Finance report created successfully.");
            redirectAttributes.addFlashAttribute("agreement", agreement);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating finance report: " + e.getMessage());
        }

        return "redirect:/financeReport";
    }

    @PostMapping("/markPaid/{id}")
    public String markAsPaid(@PathVariable("id") int id) {
        financeRepository.markAsPaid(id);
        return "redirect:/financeReport";
    }
    /*
    @GetMapping("/financeReport")
    public String showFinancePage(@RequestParam(value = "phoneNumber", required = false) Integer phoneNumber, Model model) {
        List<CarFinance> reports;
        if (phoneNumber != null) {
            reports = financeRepository.findByPhoneNumber(phoneNumber);
        } else {
            reports = financeRepository.findAll();
        }
        model.addAttribute("reports", reports);
        return "financeReport";
    }

     */

    @GetMapping("/pay/{id}")
    public String showPaymentPage(@PathVariable("id") int id, Model model) {
        CarFinance report = financeRepository.findById(id);
        if (report == null) {
            return "redirect:/financeReport";
        }
        model.addAttribute("report", report);
        return "payFinanceReport";
    }
    @GetMapping("/create")
    public String showCreateForm() {
        return "createFinanceReport";
    }


}

