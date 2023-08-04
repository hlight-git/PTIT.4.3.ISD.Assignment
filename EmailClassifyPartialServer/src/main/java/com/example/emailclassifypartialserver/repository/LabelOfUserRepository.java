package com.example.emailclassifypartialserver.repository;

import com.example.emailclassifypartialserver.entity.LabelOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelOfUserRepository extends JpaRepository<LabelOfUser, Integer> {
    List<LabelOfUser> findAllByUserId(int userId);
    Optional<LabelOfUser> findByUserIdAndLabelId(int userId, int labelId);
}
