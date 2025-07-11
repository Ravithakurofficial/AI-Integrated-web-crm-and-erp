package com.ERP.CRM;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*") // You can restrict to specific domains in production

public class LeadsController {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private EmailSender emailSender;

    @GetMapping("/DisplayLeads")
    public List<LeadsDB> displayAllLeads() {
        return leadRepository.findAll();
    }

    @PostMapping("/SendMail")
    public ResponseEntity<String> sendMail(@RequestParam String to) {
        String subject = "Congratulation";
        String htmlBody = "this is CRM Page and this will going to increase the overall sales. please understand the use contact us phone numbe 8700661852 name ravi company name lining solution<br><br>" +
                "Click this URL: <a href=\"https://ravithakurofficial.github.io/mailtracker.github.io/index.html\">Open Tracking Page</a>";


        try {
            emailSender.sendEmail(to, subject, htmlBody);

            try {
                Optional<LeadsDB> optionalLead = leadRepository.findByGmail(to);
                if (optionalLead.isPresent()) {
                    LeadsDB lead = optionalLead.get();
                    lead.setStatus("Sent");
                    leadRepository.save(lead);

                    return ResponseEntity.ok("Mail sent");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Lead with email not found.");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(" Failed to update lead status: " + e.getMessage());
            }

        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send mail: " + e.getMessage());
        }
    }


    @GetMapping("/track")
    public ResponseEntity<byte[]> trackEmail(@RequestParam String email) throws IOException {
        // Print the email received in the request
        System.out.println("📬 Email opened by: " + email);
        try {
            Optional<LeadsDB> optionalLead = leadRepository.findByGmail(email);
            if (optionalLead.isPresent()) {
                LeadsDB lead = optionalLead.get();
                lead.setStatus("OPEN");
                leadRepository.save(lead);
            } else {
                System.out.println(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Lead with email not found."));
            }
        } catch (Exception e) {
            System.out.println(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(" Failed to update lead status: " + e.getMessage()));
        }

        // Load the transparent image from the classpath
        ClassPathResource imageFile = new ClassPathResource("static/transparent.png");
        byte[] imageBytes = imageFile.getInputStream().readAllBytes();

        // Return the image as a response with proper headers
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header("ngrok-skip-browser-warning", "true")
                .body(imageBytes);
    }
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("https://ravithakurofficial.github.io") // Allow GitHub Pages
                    .allowedMethods("GET", "POST", "PUT", "DELETE");
        }
    }






}
