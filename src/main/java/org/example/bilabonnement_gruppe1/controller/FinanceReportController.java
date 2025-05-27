package org.example.bilabonnement_gruppe1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.bilabonnement_gruppe1.model.FinanceReport;
import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.example.bilabonnement_gruppe1.repository.FinanceReportRepository;
import org.example.bilabonnement_gruppe1.repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/finance")
public class FinanceReportController {

    @Autowired
    private FinanceReportRepository financeReportRepository;

    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;

    @GetMapping("/reports")
    public String showAllFinanceReports(HttpSession session, Model model) {
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }

        List<FinanceReport> reports = financeReportRepository.getAllFinanceReports();
        model.addAttribute("financeReports", reports);
        return "financeReports";
    }

    @GetMapping("/generate/{rentalAgreementId}")
    public String generateFinanceReport(@PathVariable int rentalAgreementId,
                                        HttpSession session,
                                        Model model) {
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }

        RentalAgreement agreement = rentalAgreementRepository.getActiveRentalAgreementById(rentalAgreementId);
        if (agreement == null) {
            model.addAttribute("error", "Rental Agreement not found or inactive");
            return "errorPage";  // evt. en custom error side
        }
        model.addAttribute("rentalAgreement", agreement);


        FinanceReport existingReport = financeReportRepository.getFinanceReportByRentalAgreementId(rentalAgreementId);

        if (existingReport != null) {
            model.addAttribute("financeReport", existingReport);
        } else {

            FinanceReport newReport = financeReportRepository.generateFinanceReport(rentalAgreementId);
            if (newReport != null) {
                financeReportRepository.createFinanceReport(newReport);
                model.addAttribute("financeReport", newReport);
            } else {
                model.addAttribute("error", "Could not generate finance report for this rental agreement");
            }
        }

        return "financeReportDetails";
    }

    @PostMapping("/updatePaymentStatus")
    public String updatePaymentStatus(@RequestParam int financeReportId,
                                      @RequestParam boolean paid,
                                      HttpSession session) {
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }

        LocalDate paymentDate = paid ? LocalDate.now() : null;
        financeReportRepository.updatePaymentStatus(financeReportId, paid, paymentDate);

        return "redirect:/finance/reports";
    }

    @GetMapping("/agreements")
    public String showRentalAgreementsForFinance(HttpSession session, Model model) {
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }

        List<RentalAgreement> agreements = rentalAgreementRepository.getAllRentalAgreements();
        model.addAttribute("rentalAgreements", agreements);
        return "financeAgreementSelection";
    }
}