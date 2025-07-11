package com.ERP.CRM;


import org.springframework.stereotype.Service;

@Service
public class CreateCatalog {
    public String generateCatalog(String productName, String productDescription, double productWeight, double productHeight, double productLength, String productCategory,String base64Image) {

        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Product Catalog</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; margin: 0; padding: 20px; }" +
                "        .catalog { background: white; padding: 20px; width: 400px; margin: auto; box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); border-radius: 10px; }" +
                "        img { max-width: 100%; border-radius: 10px; }" +
                "        h2 { color: #333; }" +
                "        p { color: #666; line-height: 1.6; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='catalog'>" +
                (base64Image != null && !base64Image.isEmpty() ? "        <img src='data:image/png;base64," + base64Image + "' alt='Product Image'>" : "") +
                "        <h2>" + productName + "</h2>" +
                "        <p><strong>Description:</strong> " + productDescription + "</p>" +
                "        <p><strong>Weight:</strong> " + productWeight + " kg</p>" +
                "        <p><strong>Height:</strong> " + productHeight + " cm</p>" +
                "        <p><strong>Length:</strong> " + productLength + " cm</p>" +
                "        <p><strong>Category:</strong> " + productCategory + "</p>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}
