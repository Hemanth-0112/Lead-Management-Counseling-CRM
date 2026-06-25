package com.crm.lead.repository;

import com.crm.lead.enums.LeadSource;
import com.crm.lead.enums.LeadStatus;
import com.crm.lead.model.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    List<Lead> findByStatus(LeadStatus status);

    List<Lead> findByAssignedCounselorId(Long counselorId);

    List<Lead> findBySource(LeadSource source);

    List<Lead> findByCourseContainingIgnoreCase(String course);

    List<Lead> findByCityContainingIgnoreCase(String city);

    @Query("SELECT l FROM Lead l WHERE " +
           "(:status IS NULL OR l.status = :status) AND " +
           "(:source IS NULL OR l.source = :source) AND " +
           "(:counselorId IS NULL OR l.assignedCounselor.id = :counselorId) AND " +
           "(:course IS NULL OR LOWER(l.course) LIKE LOWER(CONCAT('%', :course, '%'))) AND " +
           "(:city IS NULL OR LOWER(l.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:keyword IS NULL OR LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "  OR LOWER(l.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "  OR LOWER(l.phone) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Lead> searchLeads(@Param("status") LeadStatus status,
                           @Param("source") LeadSource source,
                           @Param("counselorId") Long counselorId,
                           @Param("course") String course,
                           @Param("city") String city,
                           @Param("keyword") String keyword);

    List<Lead> findByNextFollowUpBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT l.status, COUNT(l) FROM Lead l GROUP BY l.status")
    List<Object[]> countByStatus();

    @Query("SELECT l.source, COUNT(l) FROM Lead l GROUP BY l.source")
    List<Object[]> countBySource();

    @Query("SELECT l.assignedCounselor.name, COUNT(l) FROM Lead l WHERE l.assignedCounselor IS NOT NULL GROUP BY l.assignedCounselor.name")
    List<Object[]> countByCounselor();

    long countByStatus(LeadStatus status);
}
