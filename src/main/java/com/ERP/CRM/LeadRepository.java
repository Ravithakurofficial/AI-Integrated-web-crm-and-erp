package com.ERP.CRM;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ERP.CRM.LeadsDB;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LeadRepository extends JpaRepository<LeadsDB,Long> {
    Optional<LeadsDB> findByGmail(String Gmail);
    long countByStatus(String Status);
}
