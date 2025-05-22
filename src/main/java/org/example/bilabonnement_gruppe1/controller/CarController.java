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

    @GetMapping("/carList")
    public String showCarList(Model model) {
        model.addAttribute("cars", carRepository.findAll(false));
        return "carList";
    }


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

    @GetMapping("/filter")
    public String filterCars(@RequestParam(defaultValue = "all") String status, Model model) {
        try {
            ArrayList<Car> cars = carRepository.getCarsByStatus(status);
            model.addAttribute("carList", cars);
        } catch (Exception e) {
            model.addAttribute("error", "Kunne ikke hente biler.");
            model.addAttribute("carList", new ArrayList<Car>()); // tom liste
        }
        model.addAttribute("selectedStatus", status);
        return "carList";
    }


    @GetMapping("/edit/{vehicleNumber}")
    public String showEditForm(@PathVariable String vehicleNumber, Model model) {
        Car car = carRepository.getCarByVehicleNumber(vehicleNumber);
        if (car != null) {
            model.addAttribute("car", car);
            return "editCar";
        } else {
            return "redirect:/cars/carList";
        }
    }

    @PostMapping("/edit")
    public String handleEdit(@ModelAttribute("car") Car car) {
        carRepository.updateCar(car);
        return "redirect:/cars/carList";
    }




}
