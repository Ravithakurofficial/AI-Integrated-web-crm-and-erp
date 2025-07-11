package com.ERP.CRM;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailSender {

    private final JavaMailSender mailSender;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${cohere.api.key}")
    private String apiKey;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public Map<String, String> generateEmailContent(String prompt) {
        Map<String, String> result = new HashMap<>();

        String coherePrompt = String.format(
                "Improve the following email for marketing purposes. Strictly return only a minified JSON like {\\\"subject\\\":\\\"...\\\",\\\"body\\\":\\\"...\\\"}. No markdown, no explanation.\n\n%s",
                prompt
        );

        try {
            String cohereResponse = callCohereAPI(coherePrompt);
            System.out.println("Cohere API raw response: " + cohereResponse);

            JsonNode root = objectMapper.readTree(cohereResponse);
            String textResponse = root.has("text") ? root.get("text").asText() : "";

            textResponse = textResponse.replaceAll("```json", "").replaceAll("```", "").trim();

            int startIndex = textResponse.indexOf("{");
            int endIndex = textResponse.lastIndexOf("}") + 1;

            if (startIndex != -1 && endIndex > startIndex) {
                String jsonString = textResponse.substring(startIndex, endIndex);

                JsonNode json = objectMapper.readTree(jsonString);

                result.put("subject", json.has("subject") ? json.get("subject").asText() : "Default Subject");
                result.put("body", json.has("body") ? json.get("body").asText() : "Default Body");
            } else {
                System.err.println("Failed to extract JSON from response text.");
                result.put("subject", "Invitation to Increase Your Sales");
                result.put("body", "Hi there, Thank you for your continued support. We are excited to share with you an innovative tool that will take your sales figures to the next level. Give our CRM platform a try and experience the difference for yourself. Feel free to get in touch with our representative, Ravi, through our website or this phone number: 8700661852. We can't wait to work with you! Sincerely, Lining Solutions. Simply click on the following URL to access the tracking page: https://ravithakurofficial.github.io/mailtracker.github.io/index.html\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("subject", "Default Subject");
            result.put("body", "Default Body");
        }

        return result;
    }

    private String callCohereAPI(String input) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String jsonPayload = objectMapper.writeValueAsString(Map.of(
                "model", "command",
                "prompt", input,
                "max_tokens", 300,
                "temperature", 0.7
        ));

        RequestBody body = RequestBody.create(jsonPayload, JSON);

        Request request = new Request.Builder()
                .url("https://api.cohere.ai/generate")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            return response.body().string();
        }
    }

    public void sendEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        Map<String, String> aiContent = generateEmailContent("Subject: " + subject + "\nBody: " + htmlContent);

        // 👇 Add tracking image
        String trackingPixel = "<img src='https://ai-integrated-web-crm-and-erp-production.up.railway.app/track?email=" + toEmail + "' width='1' height='1' style='display:none;' />";
        String finalBody = aiContent.get("body") + trackingPixel;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject(aiContent.get("subject"));
        helper.setText(finalBody, true); // Enable HTML body

        try {
            mailSender.send(message);
            System.out.println("✅ Email sent to " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            throw new MessagingException("Failed to send email", e);
        }
    }
}
