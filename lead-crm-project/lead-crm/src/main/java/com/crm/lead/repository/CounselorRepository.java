package com.crm.lead.repository;

import com.crm.lead.model.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {
    Optional<Counselor> findByEmail(String email);
    List<Counselor> findByActive(boolean active);
    List<Counselor> findByNameContainingIgnoreCase(String name);
}
