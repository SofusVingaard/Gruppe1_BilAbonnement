package org.example.bilabonnement_gruppe1.controller;


import org.example.bilabonnement_gruppe1.model.Car;
import org.example.bilabonnement_gruppe1.model.Customer;
import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.example.bilabonnement_gruppe1.model.User;
import org.example.bilabonnement_gruppe1.repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
    @RequestMapping("/rentalAgreement")
    public class RentalAgreementController {

        @Autowired
        RentalAgreementRepository rentalAgreementRepository;

        @GetMapping("/create")
        public String showCreateForm() {
            return "createRentalAgreement";
        }

        @PostMapping("/create")
        public String createRentalAgreement(
                @RequestParam("carId") String carId,
                @RequestParam("customerId") int customerId,
                @RequestParam("userId") int userId,
                @RequestParam("startDate") String startDate,
                @RequestParam("endDate") String endDate,
                RedirectAttributes redirectAttributes) {

            RentalAgreement agreement = new RentalAgreement();
            agreement.setCar(new Car(carId));
            agreement.setCustomer(new Customer(customerId));
            agreement.setUser(new User(userId));
            agreement.setStartDate(LocalDate.parse(startDate));
            agreement.setEndDate(LocalDate.parse(endDate));
            agreement.setActive(true);

            rentalAgreementRepository.createRentalAgreement(agreement);
            redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale oprettet!");

            return "redirect:/index";
        }
    }

