 package com.ontu.lab6.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping(value = "/index")
    public String index(Model model) {
        String[][] gameField = {
            {"X", "O", "X"},
            {"X", "O", "O"},
            {"O", "X", "X"}
        };
        model.addAttribute("gameField", gameField);
        return "index";
    }
}
