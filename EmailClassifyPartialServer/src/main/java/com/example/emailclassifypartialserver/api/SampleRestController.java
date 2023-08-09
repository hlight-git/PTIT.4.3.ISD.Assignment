package com.example.emailclassifypartialserver.api;

import com.example.emailclassifypartialserver.entity.Sample;
import com.example.emailclassifypartialserver.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/sample", produces = MediaType.APPLICATION_JSON_VALUE)
public class SampleRestController {
    private final SampleRepository sampleRepository;

    @Autowired
    public SampleRestController(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @GetMapping
    List<Sample> find(){
        return sampleRepository.findAll();
    }
}
