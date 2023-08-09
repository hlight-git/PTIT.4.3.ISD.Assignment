package com.example.emailclassifypartialserver.controller;

import com.example.emailclassifypartialserver.Constant;
import com.example.emailclassifypartialserver.entity.Email;
import com.example.emailclassifypartialserver.entity.Sample;
import com.example.emailclassifypartialserver.repository.EmailRepository;
import com.example.emailclassifypartialserver.repository.LabelRepository;
import com.example.emailclassifypartialserver.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class SampleController {
    private final LabelRepository labelRepository;
    private final EmailRepository emailRepository;
    private final SampleRepository sampleRepository;

    @Autowired
    public SampleController(LabelRepository labelRepository, EmailRepository emailRepository, SampleRepository sampleRepository) {
        this.labelRepository = labelRepository;
        this.emailRepository = emailRepository;
        this.sampleRepository = sampleRepository;
    }

    @GetMapping
    String showView(Model model, @RequestParam(required = false) Integer page) {
        if (page == null){
            page = 1;
        }
        model.addAttribute("labels", labelRepository.findAll());
        model.addAttribute("curPage", page);
        model.addAttribute("curLabel", 0);
        model.addAttribute("curURL", "/");
        List<Email> emails = emailRepository.findBy(PageRequest.of(page - 1, Constant.ITEM_PER_PAGE));
        HashMap<Integer, Boolean> isSample = new HashMap<>();
        for (Email e:emails){
            isSample.put(e.getId(), sampleRepository.findByEmailId(e.getId()).isPresent());
        }
        model.addAttribute("emails", emails);
        model.addAttribute("isSample", isSample);
        return "emails";
    }
    @GetMapping("/detail/{id}")
    String showDetail(Model model,
                      @PathVariable("id") Integer emailId){
        Email email = emailRepository.findById(emailId).get();
        boolean isSample = sampleRepository.findByEmailId(emailId).isPresent();
        model.addAttribute("email", email);
        model.addAttribute("isSample", isSample);
        return "email";
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
        model.addAttribute("curURL", "/filter/" + labelId);
        List<Email> emails = emailRepository.findByLabelId(labelId, PageRequest.of(page - 1, Constant.ITEM_PER_PAGE));
        HashMap<Integer, Boolean> isSample = new HashMap<>();
        for (Email e:emails){
            isSample.put(e.getId(), sampleRepository.findByEmailId(e.getId()).isPresent());
        }
        model.addAttribute("emails", emails);
        model.addAttribute("isSample", isSample);
        return "emails";
    }
    @GetMapping("/new")
    String showNewSampleView(Model model){
        model.addAttribute("labels", labelRepository.findAll());
        return "addSample";
    }
    @PostMapping("/save")
    String update(@RequestParam Map<String, String> args){
        String redirect = "";
        for (String e: args.keySet()){
            if (e.equals("urlBeforeSave")){
                redirect = args.get(e);
                continue;
            }
            int emailId = Integer.parseInt(e);
            Optional<Sample> sampleFound = sampleRepository.findByEmailId(emailId);
            Sample sample;
            if (sampleFound.isEmpty() && args.get(e).equals("on")){
                Email email = emailRepository.findById(emailId).get();
                sample = new Sample();
                sample.setEmail(email);
                sample.setTrained(false);
                sampleRepository.save(sample);
            }
            else if (sampleFound.isPresent() && args.get(e).equals("off")){
                sample = sampleFound.get();
                sampleRepository.delete(sample);
            }
        }
        return "redirect:" + redirect;
    }
    @PostMapping("/save/new")
    String add(@RequestParam String subject, @RequestParam int labelId, @RequestParam String content){
        Email email = new Email();
        email.setSubject(subject);
        email.setContent(content);
        email.setLabel(labelRepository.findById(labelId).get());
        email = emailRepository.save(email);
        Sample sample = new Sample();
        sample.setEmail(email);
        sample.setTrained(false);
        sampleRepository.save(sample);
        return "redirect:..";
    }
}
