package org.example.bilabonnement_gruppe1.controller;

import org.example.bilabonnement_gruppe1.repository.CarRepository;
import org.example.bilabonnement_gruppe1.repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @Autowired
    RentalAgreementRepository rentalAgreementRepository;

    @Autowired
    CarRepository carRepository;

    @GetMapping("/index")
    public String index() {
        return "index"; // viser templates/index.html
    }
    @GetMapping("/dashboard")
    public String dashboard(Model model){

        int activeSubscriptions = rentalAgreementRepository.countActiveAgreements();
        double avgRentalLength = rentalAgreementRepository.averageRentalPeriodLength();
        int availableCars = carRepository.countAvailableCars();

        model.addAttribute("activeSubscriptions", activeSubscriptions);
        model.addAttribute("avgRentalLength", avgRentalLength);
        model.addAttribute("availableCars", availableCars);

        return "dashboard"; 
    }
}

