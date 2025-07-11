package com.ERP.CRM;

import com.ERP.CRM.Transportdb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepository extends JpaRepository<Transportdb, Integer> {
}
