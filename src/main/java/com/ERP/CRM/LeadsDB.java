package com.ERP.CRM;

import jakarta.persistence.*;

@Entity
@Table(name = "LeadsDB")
public class LeadsDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LeadName", nullable = false)
    private String customerName;

    @Column(name = "PhoneNumber", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "Gmail", unique = true, nullable = true)
    private String gmail;

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "Status", nullable = false)
    private String status;

    // ✅ No-argument constructor (required by JPA)
    public LeadsDB() {
    }

    public LeadsDB(String status) {
        this.status = status;
    }

    public LeadsDB(Long id, String customerName, String phoneNumber, String gmail, String address, String status) {
        this.id = id;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.gmail = gmail;
        this.address = address;
        this.status = status;
    }

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
