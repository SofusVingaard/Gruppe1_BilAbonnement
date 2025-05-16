package org.example.bilabonnement_gruppe1.controller;


import jakarta.servlet.http.HttpSession;
import org.example.bilabonnement_gruppe1.model.Car;
import org.example.bilabonnement_gruppe1.model.Customer;
import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.example.bilabonnement_gruppe1.model.User;
import org.example.bilabonnement_gruppe1.repository.CarRepository;
import org.example.bilabonnement_gruppe1.repository.RentalAgreementRepository;
import org.example.bilabonnement_gruppe1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        public String showCreateForm(@RequestParam(value = "limitFilter", required = false) String filter, Model model) {
            ArrayList<Car> carList;

            if ("limited".equals(filter)) {
                carList = carRepository.getCarsByLimited(true);
            } else if ("unlimited".equals(filter)) {
                carList = carRepository.getCarsByLimited(false);
            } else {
                carList = carRepository.showAvailableCars("available");
            }

            model.addAttribute("carList", carList);
            model.addAttribute("filter", filter);

            return "createRentalAgreement";
        }


    @PostMapping("/create")
    public String createRentalAgreement(
            @RequestParam("carId") String carId,
            @RequestParam("customerPhoneNumber") int customerPhoneNumber,
            @RequestParam("userId") String userId,
            @RequestParam("startDate") String startDateStr,
            @RequestParam("duration") int durationInMonths,
            @RequestParam("allowedKM") double allowedKM,
            RedirectAttributes redirectAttributes) {

        Car selectedCar = carRepository.getCarByVehicleNumber(carId);
        if (selectedCar == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bilen blev ikke fundet.");
            return "redirect:/create";
        }

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate;

        if (selectedCar.isLimited()) {
            endDate = startDate.plusMonths(3);  // Fixed 3 months for limited cars
        } else {
            endDate = startDate.plusMonths(durationInMonths);
        }



        RentalAgreement agreement = new RentalAgreement();
        agreement.setCar(selectedCar);
        agreement.setCustomerPhoneNumber(customerPhoneNumber);
        agreement.setUser(new User(userId));
        agreement.setStartDate(startDate);
        agreement.setEndDate(endDate);
        agreement.setActive(true);
        agreement.setAllowedKM(allowedKM);
        agreement.setMonthlyCarPrice(selectedCar.getMonthlyFee());
        agreement.setMonthsRented(durationInMonths);

        long monthsBetween = ChronoUnit.MONTHS.between(startDate, endDate);
        if (monthsBetween < 1) {
            monthsBetween = 1;
        }

        int monthlyExtraFee=0;
        if (allowedKM==1750){
            monthlyExtraFee=250;
        }
        if(allowedKM==2000){
            monthlyExtraFee=450;
        }

// Calculate total price
        int totalPrice = (int) (monthsBetween * (selectedCar.getMonthlyFee()+monthlyExtraFee));
        agreement.setTotalPrice(totalPrice);

// Debug output
        System.out.println("DEBUG: Calculated price - Months: " + monthsBetween
                + ", Monthly fee: " + selectedCar.getMonthlyFee()
                + ", Total: " + totalPrice);


        rentalAgreementRepository.createRentalAgreement(agreement);
        redirectAttributes.addFlashAttribute("successMessage", "Lejeaftale oprettet!");

        return "redirect:/dashboard";
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

    @GetMapping("/updateRentalAgreement/{id}")
    public String updateRentalAgreement(@PathVariable("id") int id, Model model) {
        RentalAgreement agreement = rentalAgreementRepository.getRentalAgreement(id);
        model.addAttribute("agreement", agreement);
        System.out.println("Damage report ID: " + agreement.getDamageReportId());
        return "updateRentalAgreement"; // returner til HTML-formular
    }

    @PostMapping("/updateRentalAgreement/{id}")
    public String updateRentalAgreement(@PathVariable("id") int id, @ModelAttribute RentalAgreement agreement) {
        rentalAgreementRepository.updateRentalAgreement(agreement);
        return "redirect:/rentalAgreement/updateRentalAgreement/" + agreement.getId();
    }

}

