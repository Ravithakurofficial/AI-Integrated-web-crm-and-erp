package com.ERP.CRM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@RestController
public class CatalogController {

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private CreateCatalog createCatalog;

    @PostMapping("/Catalog")
    public ResponseEntity<String> addCatalogEntry(@RequestParam("productName") String productName,
                                                  @RequestParam("productDescription") String productDescription,
                                                  @RequestParam("productWeight") String productWeight,
                                                  @RequestParam("productHeight") String productHeight,
                                                  @RequestParam("productLength") String productLength,
                                                  @RequestParam("productCategory") String productCategory,
                                                  @RequestPart(value = "productImage", required = false) MultipartFile productImage) {

        if (catalogRepository.findByProductName(productName).isPresent()) {
            return ResponseEntity.badRequest().body("Product Already Exists");
        }

        if (productName.isEmpty() || productDescription.isEmpty() || productWeight.isEmpty() ||
                productHeight.isEmpty() || productLength.isEmpty() || productCategory.isEmpty()) {
            return ResponseEntity.badRequest().body("All fields are required!");
        }

        try {
            double weight = Double.parseDouble(productWeight);
            double height = Double.parseDouble(productHeight);
            double length = Double.parseDouble(productLength);
            byte[] imageBytes = (productImage != null && !productImage.isEmpty()) ? productImage.getBytes() : null;
            double Price = 0;
            int product_Quantity = 0;

            CatalogDB newCatalogEntry = new CatalogDB(productName, productDescription, weight, height, length, productCategory, imageBytes,product_Quantity,Price);
            catalogRepository.save(newCatalogEntry);

            String base64Image = (imageBytes != null) ? Base64.getEncoder().encodeToString(imageBytes) : null;

            String catalogHtml = createCatalog.generateCatalog(productName, productDescription, weight, height, length, productCategory, base64Image);

            return ResponseEntity.ok().body(catalogHtml);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Weight, Height, and Length must be valid numbers!");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing image: " + e.getMessage());
        }
    }
}
