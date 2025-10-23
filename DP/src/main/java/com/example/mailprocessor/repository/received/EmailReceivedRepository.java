package com.example.mailprocessor.repository.received;

import com.example.mailprocessor.entity.EmailReceived;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailReceivedRepository extends JpaRepository<EmailReceived, Long> {
    boolean existsBySubject(String subject);
}
