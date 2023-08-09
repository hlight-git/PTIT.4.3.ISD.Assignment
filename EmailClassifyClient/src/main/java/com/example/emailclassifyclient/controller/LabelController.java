package com.example.emailclassifyclient.controller;

import com.example.emailclassifyclient.entity.Label;
import com.example.emailclassifyclient.entity.User;
import com.example.emailclassifyclient.service.LabelService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/labels")
public class LabelController {
    private final LabelService labelService;

    @Autowired
    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping
    String showLabels(){
        return "labels";
    }
    @PostMapping("/add")
    String addLabel(HttpSession session, @RequestParam String labelName){
        User user = (User) session.getAttribute("user");
        labelService.addLabel(user.getId(), labelName);
        user.setLabels(labelService.getAllLabelOfUser(user));
        session.setAttribute("user", user);
        return showLabels();
    }
    @GetMapping("/delete/{id}")
    String deleteLabel(@SessionAttribute User user, @PathVariable("id") int id){
        labelService.removeLabel(id, user.getId());
        user.setLabels(labelService.getAllLabelOfUser(user));
        return showLabels();
    }
}
