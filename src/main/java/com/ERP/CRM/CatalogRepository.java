package com.ERP.CRM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CatalogRepository extends JpaRepository<CatalogDB, Long> {
    Optional<CatalogDB> findByProductName(String productName);
}
