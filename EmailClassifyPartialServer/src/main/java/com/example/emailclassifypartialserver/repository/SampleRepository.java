package com.example.emailclassifypartialserver.repository;

import com.example.emailclassifypartialserver.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SampleRepository extends JpaRepository<Sample, Integer> {
    Optional<Sample> findByEmailId(int emailId);
}
