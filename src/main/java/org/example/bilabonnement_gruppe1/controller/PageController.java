package org.example.bilabonnement_gruppe1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.bilabonnement_gruppe1.repository.CarRepository;
import org.example.bilabonnement_gruppe1.repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }
        int activeSubscriptions = rentalAgreementRepository.countActiveAgreements();
        double avgRentalLength = rentalAgreementRepository.averageRentalPeriodLength();
        int availableCars = carRepository.countAvailableCars();

        model.addAttribute("activeSubscriptions", activeSubscriptions);
        model.addAttribute("avgRentalLength", avgRentalLength);
        model.addAttribute("availableCars", availableCars);

        model.addAttribute("activeSubscriptionsTarget", 30);
        model.addAttribute("avgRentalLengthTarget", 100);
        model.addAttribute("availableCarsTarget", 10);

        return "dashboard";
    }
}
