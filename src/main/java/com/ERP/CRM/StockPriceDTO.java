package com.ERP.CRM;

public class StockPriceDTO {
    private Long id;
    private String name;
    private double weight;
    private double height;
    private String description;
    private Double Price;
    private String base64Image;

    public StockPriceDTO(Long id, String name, double weight, double height, String description,
                         double Price, String base64Image) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.description = description;
        this.Price = Price;
        this.base64Image = base64Image;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return Price; }
    public void setPrice(Double Price) { this.Price = Price; }

    public String getBase64Image() { return base64Image; }
    public void setBase64Image(String base64Image) { this.base64Image = base64Image; }
}
