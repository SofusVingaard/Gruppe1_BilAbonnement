package org.example.bilabonnement_gruppe1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/index")
    public String index() {
        return "index"; // viser templates/index.html
    }
    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard"; 
    }
}

