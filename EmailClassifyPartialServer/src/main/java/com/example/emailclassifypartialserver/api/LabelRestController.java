package com.example.emailclassifypartialserver.api;

import com.example.emailclassifypartialserver.entity.Label;
import com.example.emailclassifypartialserver.entity.LabelOfUser;
import com.example.emailclassifypartialserver.repository.LabelOfUserRepository;
import com.example.emailclassifypartialserver.repository.LabelRepository;
import com.example.emailclassifypartialserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/label", produces = MediaType.APPLICATION_JSON_VALUE)
public class LabelRestController {
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final LabelOfUserRepository labelOfUserRepository;

    @Autowired
    public LabelRestController(UserRepository userRepository, LabelRepository labelRepository, LabelOfUserRepository labelOfUserRepository) {
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
        this.labelOfUserRepository = labelOfUserRepository;
    }

    @GetMapping
    List<LabelOfUser> findLabels(@RequestParam Integer userId){
        return labelOfUserRepository.findAllByUserId(userId);
    }

    @GetMapping(value = "/add")
    Label add(@RequestParam("userId") int userId, @RequestParam("labelName") String labelName){
        Optional<Label> foundLabel = labelRepository.findByName(labelName);
        Label label;
        if (foundLabel.isEmpty()){
            label = new Label();
            label.setName(labelName);
            label = labelRepository.save(label);
        }
        else{
            label = foundLabel.get();
        }
        Optional<LabelOfUser> foundLou = labelOfUserRepository.findByUserIdAndLabelId(userId, label.getId());
        if (foundLou.isEmpty()){
            LabelOfUser labelOfUser = new LabelOfUser();
            labelOfUser.setLabel(label);
            labelOfUser.setUser(userRepository.findById(userId).get());
            labelOfUserRepository.save(labelOfUser);
        }
        return label;
    }

    @PostMapping(value = "/delete")
    boolean delete(@RequestParam("id") int id){
        return true;
    }
}
