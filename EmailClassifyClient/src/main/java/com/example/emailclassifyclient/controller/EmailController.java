package com.example.emailclassifyclient.controller;

import com.example.emailclassifyclient.entity.Email;
import com.example.emailclassifyclient.entity.Label;
import com.example.emailclassifyclient.entity.User;
import com.example.emailclassifyclient.service.EmailService;
import com.example.emailclassifyclient.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/emails")
public class EmailController {
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public EmailController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping
    String showView(HttpSession session, Model model, @RequestParam(required = false) Integer page) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:";
        }
        if (page == null){
            page = 1;
        }
        model.addAttribute("curPage", page);
        model.addAttribute("curLabel", 0);
        model.addAttribute("curURL", "/emails");
        model.addAttribute("emails", emailService.getEmails(user.getId(), page));
        return "emails";
    }
    @GetMapping("/filter/{id}")
    String showFilteredView(HttpSession session, Model model,
                    @PathVariable("id") Integer labelId,
                    @RequestParam(required = false) Integer page
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null || labelId == 0) {
            return "redirect:";
        }
        if (page == null){
            page = 1;
        }
        model.addAttribute("curPage", page);
        model.addAttribute("curLabel", labelId);
        model.addAttribute("curURL", "/emails/filter/" + labelId);
        model.addAttribute("emails", emailService.getEmails(user.getId(), page, labelId));
        return "emails";
    }
    @GetMapping("/{id}")
    String showEmailDetail(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("email", emailService.getEmail(id));
        return "email";
    }
    @GetMapping("/send")
    String showSendMailView(Model model){
        model.addAttribute("users", userService.getAll());
        model.addAttribute("email", new Email());
        return "sendEmail";
    }
    @PostMapping("/send")
    String sendEmail(HttpSession session,
                     @RequestParam("userId") int userId,
                     @RequestParam("subject") String subject,
                     @RequestParam("content") String content){
        Email email = new Email();
        email.setReceiver(userService.get(userId));
        email.setSender(((User) session.getAttribute("user")).getEmail());
        email.setSentTime("Just a moment");
        email.setSubject(subject);
        Label label = new Label();
        label.setId(2);
        label.setName("all documents");
        email.setLabel(label);
        email.setContent(content);
        emailService.saveEmail(email);
        return "redirect:/";
    }
}
