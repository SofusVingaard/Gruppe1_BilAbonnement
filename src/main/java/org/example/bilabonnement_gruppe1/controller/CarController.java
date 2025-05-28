package org.example.bilabonnement_gruppe1.controller;


import jakarta.servlet.http.HttpSession;
import org.example.bilabonnement_gruppe1.model.Car;
import org.example.bilabonnement_gruppe1.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
//Christoffer
@Controller
@RequestMapping("/cars")
public class CarController {


    @Autowired
    CarRepository carRepository;

    //Christoffer
    @GetMapping("/carList")
    public String showCarList(Model model, HttpSession session) {

        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }

        model.addAttribute("cars", carRepository.findAll(false));
        return "carList";
    }

    //Christoffer
    @GetMapping("/availableCars")
    public String showAvailableCars(Model model, HttpSession session) {

        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }

        ArrayList<Car> carList = carRepository.showAvailableCars("available");
        model.addAttribute("carList", carList);

        return "carList";
    }

    //Christoffer
    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {

        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }

        model.addAttribute("car", new Car());
        return "createCar";
    }

    //Christtofer
    @PostMapping("/create")
    public String handleCreate(@ModelAttribute("car") Car car) {

        carRepository.createCar(car);
        System.out.println("Oprettet ny bil: " + car.getVehicleNumber());
        return "redirect:/cars/create";
    }

    //Christoffer
    @GetMapping("/filter")
    public String filterCars(@RequestParam(defaultValue = "all") String status, Model model, HttpSession session) {

        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }
        try {
            ArrayList<Car> cars = carRepository.getCarsByStatus(status);
            model.addAttribute("carList", cars);
        } catch (Exception e) {
            model.addAttribute("error", "Kunne ikke hente biler.");
            model.addAttribute("carList", new ArrayList<Car>());
        }
        model.addAttribute("selectedStatus", status);
        return "carList";
    }

    //Christoffer
    @GetMapping("/edit/{vehicleNumber}")
    public String showEditForm(@PathVariable String vehicleNumber, Model model, HttpSession session) {
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/index";
        }
        Car car = carRepository.getCarByVehicleNumber(vehicleNumber);
        if (car != null) {
            model.addAttribute("car", car);
            return "editCar";
        } else {
            return "redirect:/cars/carList";
        }
    }

    //Christoffer
    @PostMapping("/edit")
    public String handleEdit(@ModelAttribute("car") Car car) {
        carRepository.updateCar(car);
        return "redirect:/cars/carList";
    }
}
