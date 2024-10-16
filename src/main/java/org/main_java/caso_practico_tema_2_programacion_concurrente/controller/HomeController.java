package org.main_java.caso_practico_tema_2_programacion_concurrente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/home")
public class HomeController {
    @GetMapping
    public String index() {
        return "home";
    }
}


