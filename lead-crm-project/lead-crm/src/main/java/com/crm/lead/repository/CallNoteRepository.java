package com.crm.lead.repository;

import com.crm.lead.model.CallNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallNoteRepository extends JpaRepository<CallNote, Long> {
    List<CallNote> findByLeadIdOrderByCalledAtDesc(Long leadId);
    List<CallNote> findByCounselorId(Long counselorId);
}
