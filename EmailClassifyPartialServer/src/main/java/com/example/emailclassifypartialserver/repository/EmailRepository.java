package com.example.emailclassifypartialserver.repository;

import com.example.emailclassifypartialserver.entity.Email;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Integer> {
    List<Email> findBy(Pageable pageable);
    List<Email> findByLabelId(int labelId, Pageable pageable);
    List<Email> findByReceiverId(int receiverId, Pageable pageable);
    List<Email> findByReceiverIdAndLabelId(int receiverId, int labelId, Pageable pageable);
}
