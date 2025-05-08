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
    public String rentalAgreement() {
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
                @RequestParam (required = false) double kmOverLimit,
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

            return "redirect:/dashboard";
        }
    @GetMapping("/active")
    public String showActiveAgreements(Model model) {
        ArrayList<RentalAgreement> active = rentalAgreementRepository.getActiveRentalAgreements();
        model.addAttribute("agreements", active);
        return "rentalAgreementList";
    }

    @GetMapping("/inactive")
    public String showInactiveAgreements(Model model) {
        ArrayList<RentalAgreement> inactive = rentalAgreementRepository.getInactiveRentalAgreements();
        model.addAttribute("agreements", inactive);
        return "rentalAgreementList";
    }
    @GetMapping("/searchByPhone")
    public String showSearchForm() {
        return "searchByPhone";
    }

    // Håndtér søgning
    @PostMapping("/searchByPhone")
    public String searchByPhoneNumber(@RequestParam("customerPhoneNumber") int customerPhoneNumber, Model model) throws SQLException {
        ArrayList<RentalAgreement> agreements = rentalAgreementRepository.getRentalAgreementByPhoneNumber(customerPhoneNumber);
        model.addAttribute("agreements", agreements);
        return "rentalAgreementList";
    }
    }

