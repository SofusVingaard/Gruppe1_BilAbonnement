package org.example.bilabonnement_gruppe1.controller;

import org.example.bilabonnement_gruppe1.model.User;
import org.example.bilabonnement_gruppe1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping ("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/createUser")
    public String createUser() {
        return "createUser";
    }

    @PostMapping("/createUser")
    public String createUser
            (@RequestParam ("userName") String userName,
             @RequestParam ("name") String name,
             @RequestParam ("password") String password,
             RedirectAttributes redirectAttributes
            ) {


        User user = new User(userName, name, password);
        userRepository.createUser(user);

        return "redirect:/index";
    }

}
