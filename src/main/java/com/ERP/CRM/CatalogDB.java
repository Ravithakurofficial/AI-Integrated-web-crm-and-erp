package com.ERP.CRM;

import jakarta.persistence.*;

@Entity
@Table(name = "catlogdb")
public class CatalogDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", unique = true, nullable = false)
    private String productName;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "product_weight", nullable = false)
    private double productWeight;

    @Column(name = "product_height", nullable = false)
    private double productHeight;

    @Column(name = "product_length", nullable = false)
    private double productLength;

    @Column(name = "product_category", nullable = false)
    private String productCategory;
    @Column(name = "product_Quantity",nullable = false)
    private Integer productQuantity;

    @Column(name = "Price",nullable = true)
    private Double Price;



    @Lob
    @Column(name = "product_image", columnDefinition = "LONGBLOB") // Store large binary data
    private byte[] productImage;

    public CatalogDB() {}

    public CatalogDB(String productName, String productDescription, double productWeight, double productHeight, double productLength, String productCategory, byte[] productImage, Integer productQuantity, double Price) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productWeight = productWeight;
        this.productHeight = productHeight;
        this.productLength = productLength;
        this.productCategory = productCategory;
        this.productImage = productImage;
        this.productQuantity = productQuantity;
        this.Price = Price;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public double getProductWeight() { return productWeight; }
    public void setProductWeight(double productWeight) { this.productWeight = productWeight; }

    public double getProductHeight() { return productHeight; }
    public void setProductHeight(double productHeight) { this.productHeight = productHeight; }

    public double getProductLength() { return productLength; }
    public void setProductLength(double productLength) { this.productLength = productLength; }

    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }

    public byte[] getProductImage() { return productImage; }
    public void setProductImage(byte[] productImage) { this.productImage = productImage; }

    public int getProductQuantity() { return productQuantity; }
    public  void setProductQuantity(int productQuantity) { this.productQuantity = productQuantity;}

    public double getPrice() { return Price; }
    public void setPrice(double Price) { this.Price = Price; }


}
