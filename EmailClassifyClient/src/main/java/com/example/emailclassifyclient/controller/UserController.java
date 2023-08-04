package com.example.emailclassifyclient.controller;

import com.example.emailclassifyclient.entity.User;
import com.example.emailclassifyclient.service.EmailService;
import com.example.emailclassifyclient.service.LabelService;
import com.example.emailclassifyclient.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final LabelService labelService;

    @Autowired
    public UserController(UserService userService, LabelService labelService) {
        this.userService = userService;
        this.labelService = labelService;
    }

    @GetMapping
    String showView(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new User());
            return "login";
        }
        return "redirect:/emails";
    }

    @PostMapping("/login")
    String validate(@ModelAttribute User user, HttpSession session){
        User validated = userService.validate(user);
        if (validated != null){
            validated.setLabels(labelService.getAllLabelOfUser(validated));
            session.setAttribute("user", validated);
        }
        return "redirect:";
    }

    @GetMapping("/logout")
    String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:";
    }
}
