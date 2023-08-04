package com.example.emailclassifypartialserver.api;

import com.example.emailclassifypartialserver.entity.User;
import com.example.emailclassifypartialserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {
    private final UserRepository userRepository;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping("/{id}")
    User find(@PathVariable int id){
        return userRepository.findById(id).get();
    }
    @GetMapping
    List<User> findAll(){
        return userRepository.findAll();
    }
    @PostMapping(value = "/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
    User validate(@RequestBody User user){
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
