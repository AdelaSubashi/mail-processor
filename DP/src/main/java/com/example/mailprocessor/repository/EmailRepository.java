package com.example.mailprocessor.repository;

import com.example.mailprocessor.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
   boolean existsBySubject(String subject);  // metode per te shmangur duplikimet
}
