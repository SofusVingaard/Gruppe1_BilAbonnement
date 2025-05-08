package org.example.bilabonnement_gruppe1.controller;


import org.example.bilabonnement_gruppe1.model.Car;
import org.example.bilabonnement_gruppe1.model.Customer;
import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.example.bilabonnement_gruppe1.model.User;
import org.example.bilabonnement_gruppe1.repository.CarRepository;
import org.example.bilabonnement_gruppe1.repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("/rentalAgreement")
public class RentalAgreementController {

    @Autowired
    RentalAgreementRepository rentalAgreementRepository;

    @Autowired
    CarRepository carRepository;

    @GetMapping("/rentalAgreement")
    public String rentalAgreement(Model model) {
        ArrayList<RentalAgreement> agreements = rentalAgreementRepository.getAllRentalAgreements();
        model.addAttribute("agreements", agreements);
        return "RentalAgreement";
    }

        @GetMapping("/create")
        public String showCreateForm(Model model) {

            ArrayList<Car> carList = carRepository.showAvailableCars("available");
            model.addAttribute("carList", carList);

            return "createRentalAgreement";
        }

        @PostMapping("/create")
        public String createRentalAgreement(
                @RequestParam("carId") String carId,
                @RequestParam("customerPhoneNumber") int customerPhoneNumber,
                @RequestParam("userId") int userId,
                @RequestParam("startDate") String startDate,
                @RequestParam("endDate") String endDate,
                @RequestParam ("allowedKM") double allowedKM,
                RedirectAttributes redirectAttributes) {

            RentalAgreement agreement = new RentalAgreement();
            agreement.setCar(new Car(carId));
            agreement.setCustomerPhoneNumber((customerPhoneNumber));
            agreement.setUser(new User(userId));
            agreement.setStartDate(LocalDate.parse(startDate));
            agreement.setEndDate(LocalDate.parse(endDate));
            agreement.setActive(true);


            rentalAgreementRepository.createRentalAgreement(agreement);
            redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale oprettet!");

            return "/dashboard";
        }
    @GetMapping("/search")
    public String showSearchPage(Model model) throws SQLException {
        // Vis alle lejeaftaler som default
        ArrayList<RentalAgreement> agreements = rentalAgreementRepository.getAllRentalAgreements();
        model.addAttribute("agreements", agreements);
        return "rentalAgreement";
    }

    @PostMapping("/rentalAgreement")
    public String filterRentalAgreements(
            @RequestParam(value = "phoneNumber", required = false) Integer phoneNumber,
            @RequestParam(value = "status", required = false) String status,
            Model model) {

        ArrayList<RentalAgreement> agreements;

        try {
            if (phoneNumber != null) {
                agreements = rentalAgreementRepository.getRentalAgreementByPhoneNumber(phoneNumber);
            } else if ("active".equals(status)) {
                agreements = rentalAgreementRepository.getActiveRentalAgreements();
            } else if ("inactive".equals(status)) {
                agreements = rentalAgreementRepository.getInactiveRentalAgreements();
            } else {
                agreements = rentalAgreementRepository.getAllRentalAgreements();
            }

            model.addAttribute("agreements", agreements);
            return "rentalAgreement";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/updateRentalAgreement")
    public String updateRentalAgreement(@RequestParam("id") int id, Model model) {
        RentalAgreement agreement = rentalAgreementRepository.getRentalAgreement(id);
        model.addAttribute("agreement", agreement);
        return "updateRentalAgreement";
    }
}

