package com.ERP.CRM;

import jakarta.persistence.*;

@Entity
@Table(name = "transport_data") // You can change this table name as needed
public class Transportdb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "to_location", nullable = false)
    private String to;

    @Column(name = "from_location", nullable = false)
    private String from;

    @Column(name = "fare", nullable = false)
    private double fare;

    // Constructors
    public Transportdb() {}

    public Transportdb(String to, String from, double fare) {
        this.to = to;
        this.from = from;
        this.fare = fare;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

}
