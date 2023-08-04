package com.example.emailclassifypartialserver.repository;

import com.example.emailclassifypartialserver.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Integer> {
    Optional<Label> findByName(String name);
}
