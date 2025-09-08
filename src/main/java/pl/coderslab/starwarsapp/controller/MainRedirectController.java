package pl.coderslab.starwarsapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainRedirectController {

    @GetMapping
    public String redirect() {
        return "redirect:/main.html";
    }

}
