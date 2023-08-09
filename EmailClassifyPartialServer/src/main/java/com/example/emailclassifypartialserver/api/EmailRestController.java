package com.example.emailclassifypartialserver.api;

import com.example.emailclassifypartialserver.Constant;
import com.example.emailclassifypartialserver.entity.Email;
import com.example.emailclassifypartialserver.entity.Sample;
import com.example.emailclassifypartialserver.repository.EmailRepository;
import com.example.emailclassifypartialserver.repository.LabelOfUserRepository;
import com.example.emailclassifypartialserver.repository.LabelRepository;
import com.example.emailclassifypartialserver.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/email", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailRestController {
    private final EmailRepository emailRepository;
    private final LabelRepository labelRepository;
    private final SampleRepository sampleRepository;
    private final LabelOfUserRepository labelOfUserRepository;
    @Autowired
    public EmailRestController(EmailRepository emailRepository, LabelRepository labelRepository, SampleRepository sampleRepository, LabelOfUserRepository labelOfUserRepository) {
        this.emailRepository = emailRepository;
        this.labelRepository = labelRepository;
        this.sampleRepository = sampleRepository;
        this.labelOfUserRepository = labelOfUserRepository;
    }

    @GetMapping("/detail")
    Email find(@RequestParam Integer id){
        return emailRepository.findById(id).get();
    }

    @GetMapping
    List<Email> findAll(
            @RequestParam Integer page,
            @RequestParam Integer receiverId,
            @RequestParam(required = false) Integer labelId
    ){
        if (page > 0) {
            Pageable pageable = PageRequest.of(page - 1, Constant.ITEM_PER_PAGE);
            return labelId == null ?
                    emailRepository.findByReceiverId(receiverId, pageable) :
                    emailRepository.findByReceiverIdAndLabelId(receiverId, labelId, pageable);
        }
        return null;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody Email email){
        // TODO: Classification start here...
        email.setLabel(labelRepository.findByName("all documents").get());
        // End
        emailRepository.save(email);
    }

    @PostMapping(value = "/update")
    void update(@RequestParam int emailId, @RequestParam int labelId){
        Email email = emailRepository.findById(emailId).get();
        email.setLabel(labelRepository.findById(labelId).get());
        emailRepository.save(email);
        Sample sample;
        Optional<Sample> sampleFound = sampleRepository.findByEmailId(emailId);
        if (sampleFound.isEmpty()){
            sample = new Sample();
            sample.setEmail(email);
        }
        else{
            sample = sampleFound.get();
        }
        sample.setTrained(false);
        sampleRepository.save(sample);
    }
}
