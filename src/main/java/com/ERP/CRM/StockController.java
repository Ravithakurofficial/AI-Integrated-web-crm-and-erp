package com.ERP.CRM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class StockController {

    @Autowired
    private CatalogRepository catalogRepository;

    // ✅ Update stock quantity
    @PostMapping("/UpdateStock")
    public ResponseEntity<String> updateStock(@RequestParam("id") Long id,
                                              @RequestParam("quantity") int quantity) {
        try {
            Optional<CatalogDB> optionalCatalog = catalogRepository.findById(id);
            if (optionalCatalog.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Stock with ID " + id + " not found.");
            }

            CatalogDB stock = optionalCatalog.get();
            stock.setProductQuantity(stock.getProductQuantity() + quantity);
            catalogRepository.save(stock);

            System.out.println("✅ Updated stock ID: " + id + " by quantity: " + quantity);
            return ResponseEntity.ok("Stock updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to update stock: " + e.getMessage());
        }
    }

    // ✅ Update stock price
    @PostMapping("/UpdateStockPrice")
    public ResponseEntity<String> updateStockPrice(@RequestParam("id") Long id,
                                                   @RequestParam("price") double price) {
        try {
            Optional<CatalogDB> optionalCatalog = catalogRepository.findById(id);
            if (optionalCatalog.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Stock with ID " + id + " not found.");
            }

            CatalogDB stock = optionalCatalog.get();
            stock.setPrice(price);
            catalogRepository.save(stock);

            System.out.println("✅ Updated stock ID: " + id + " with new price: " + price);
            return ResponseEntity.ok("Stock price updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to update stock price: " + e.getMessage());
        }
    }

    // ✅ Return available stock data
    @GetMapping("/AvailableStock")
    @ResponseBody
    public List<StockDTO> displayData() {
        List<CatalogDB> catalogList = catalogRepository.findAll();
        return catalogList.stream().map(this::convertToStockDTO).collect(Collectors.toList());
    }

    // ✅ Return price data
    @GetMapping("/StockPrice")
    @ResponseBody
    public List<StockPriceDTO> displayPriceData() {
        List<CatalogDB> catalogList = catalogRepository.findAll();
        return catalogList.stream().map(this::convertToPriceDTO).collect(Collectors.toList());
    }

    // ✅ Convert to Stock DTO
    private StockDTO convertToStockDTO(CatalogDB catalog) {
        String base64Image = catalog.getProductImage() != null
                ? Base64.getEncoder().encodeToString(catalog.getProductImage())
                : null;

        return new StockDTO(
                catalog.getId(),
                catalog.getProductName(),
                catalog.getProductWeight(),
                catalog.getProductHeight(),
                catalog.getProductDescription(),
                catalog.getProductQuantity(),
                base64Image
        );
    }

    // ✅ Convert to Price DTO
    private StockPriceDTO convertToPriceDTO(CatalogDB catalog) {
        String base64Image = catalog.getProductImage() != null
                ? Base64.getEncoder().encodeToString(catalog.getProductImage())
                : null;

        return new StockPriceDTO(
                catalog.getId(),
                catalog.getProductName(),
                catalog.getProductWeight(),
                catalog.getProductHeight(),
                catalog.getProductDescription(),
                catalog.getPrice(),
                base64Image
        );
    }
}
