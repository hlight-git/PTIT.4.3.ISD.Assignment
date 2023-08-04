package com.example.emailclassifypartialserver.controller;

import com.example.emailclassifypartialserver.Constant;
import com.example.emailclassifypartialserver.repository.EmailRepository;
import com.example.emailclassifypartialserver.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class SampleController {
    private final LabelRepository labelRepository;
    private final EmailRepository emailRepository;

    @Autowired
    public SampleController(LabelRepository labelRepository, EmailRepository emailRepository) {
        this.labelRepository = labelRepository;
        this.emailRepository = emailRepository;
    }

    @GetMapping
    String showView(Model model, @RequestParam(required = false) Integer page) {
        if (page == null){
            page = 1;
        }
        model.addAttribute("labels", labelRepository.findAll());
        model.addAttribute("curPage", page);
        model.addAttribute("curLabel", 0);
        model.addAttribute("curURL", "/emails");
        model.addAttribute("emails", emailRepository.findBy(PageRequest.of(page, Constant.ITEM_PER_PAGE)));
        return "emails";
    }
    @GetMapping("/filter/{id}")
    String showFilteredView(Model model,
                            @PathVariable("id") Integer labelId,
                            @RequestParam(required = false) Integer page
    ) {
        if (page == null){
            page = 1;
        }
        model.addAttribute("labels", labelRepository.findAll());
        model.addAttribute("curPage", page);
        model.addAttribute("curLabel", labelId);
        model.addAttribute("curURL", "/emails/filter/" + labelId);
        model.addAttribute("emails", emailRepository.findByLabelId(labelId, PageRequest.of(page, Constant.ITEM_PER_PAGE)));
        return "emails";
    }
}
