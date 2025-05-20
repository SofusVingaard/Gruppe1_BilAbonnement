package org.example.bilabonnement_gruppe1.controller;

import jakarta.servlet.http.HttpSession;
import org.example.bilabonnement_gruppe1.model.Customer;
import org.example.bilabonnement_gruppe1.model.User;
import org.example.bilabonnement_gruppe1.repository.CarRepository;
import org.example.bilabonnement_gruppe1.repository.CustomerRepository;
import org.example.bilabonnement_gruppe1.repository.RentalAgreementRepository;
import org.example.bilabonnement_gruppe1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping ("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomerRepository customerRepository;



    @GetMapping("/createUser")
    public String createUser() {
        return "createUser";
    }

    @PostMapping("/createUser")
    public String createUser
            (@RequestParam ("userLogin") String userLogin,
             @RequestParam ("name") String name,
             @RequestParam ("password") String password,
             RedirectAttributes redirectAttributes) {

        User doesExist= userRepository.findByUserLogin(userLogin);

        if( doesExist!=null){
            redirectAttributes.addFlashAttribute("errorMessage", "Der findes allerede en bruger med dette brugernavn");
            return "redirect:/user/createUser";
        }

        User user = new User(userLogin, name, password);
        userRepository.createUser(user);

        return "redirect:/index";
    }

    @GetMapping ("/login")
    public String loginPage(){
        return "index";
    }


    @PostMapping("/login")
    public String login(
            HttpSession session,
            RedirectAttributes redirectAttributes,
            @RequestParam("userLogin") String userLogin,
            @RequestParam("password") String password,
            Model model) {

        User user = userRepository.findByUserLogin(userLogin);

        if (user != null && user.getPassword() !=null && user.getPassword().equals(password)) {
            session.setAttribute("currentUser", user);
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User login er forkert/Bruger eksistere ikke");
            return "redirect:/index";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("logoutMessage", "Du er nu logget ud.");
        return "redirect:/index"; 
    }

    @GetMapping("/createCustomer")
    public String createCustomer() {

        return "createCustomer";
    }

    @PostMapping("/createCustomer")
    public String createCustomer(@RequestParam("name")String name,
                                     @RequestParam("email")String email,
                                     @RequestParam("phoneNumbeer") int phoneNumber) {
        Customer customer = new Customer(name, email, phoneNumber);
        customerRepository.createCustomer(customer);
        return "redirect:/dashboard";
        }
}
