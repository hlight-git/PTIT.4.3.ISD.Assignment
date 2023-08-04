package com.example.emailclassifypartialserver.api;

import com.example.emailclassifypartialserver.Constant;
import com.example.emailclassifypartialserver.entity.Email;
import com.example.emailclassifypartialserver.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/email", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailRestController {
    private final EmailRepository emailRepository;
    @Autowired
    public EmailRestController(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
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
        emailRepository.save(email);
    }
}
