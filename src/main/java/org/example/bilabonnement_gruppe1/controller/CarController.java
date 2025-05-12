package org.example.bilabonnement_gruppe1.controller;


import jakarta.servlet.http.HttpSession;
import org.example.bilabonnement_gruppe1.model.Car;
import org.example.bilabonnement_gruppe1.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    CarRepository carRepository;

    @GetMapping("/availableCars")
    public String showAvailableCars(Model model) {
        ArrayList<Car> carList = carRepository.showAvailableCars("available");

        model.addAttribute("carList", carList);

        return "carList";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model)
            {
        model.addAttribute("car", new Car());
        return "createCar";
    }

    @PostMapping("/create")
    public String handleCreate(@ModelAttribute("car") Car car) {
        carRepository.createCar(car);
        System.out.println("Oprettet ny bil: " + car.getVehicleNumber());
        return "redirect:/cars/create";
    }

}
