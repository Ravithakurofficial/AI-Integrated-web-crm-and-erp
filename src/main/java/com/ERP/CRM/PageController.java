package com.ERP.CRM;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*") // You can restrict to specific domains in production
public class PageController {

    @Autowired
    private UserService userService;
    @Autowired
    private LeadRepository leadRepository;

    // Check if the user is logged in and return the respective page
    private String checkLogin(HttpSession session, Model model, String page) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // If the user is not logged in, redirect to the login page
        if (loggedInUser == null) {
            return "redirect:/"; // Redirect to login page
        }

        model.addAttribute("user", loggedInUser); // Add the logged-in user to the model
        return page; // Return the respective page view
    }

    // Dashboard route
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Map<String, Long> leadStatusCounts = userService.getLeadStatusCounts();
        model.addAttribute("leadStatusCounts", leadStatusCounts);
        return "DashBoard";
        }

    // Stocks route
    @GetMapping("/stocks")
    public String showStocks(HttpSession session, Model model) {
        return checkLogin(session, model, "stocks"); // Return stocks page if logged in
    }

    // Leads route
    @GetMapping("/leads")
    public String showLeads(HttpSession session, Model model) {
        return checkLogin(session, model, "leads"); // Return leads page if logged in
    }

    // Templates route
    @GetMapping("/templates")
    public String showTemplates(HttpSession session, Model model) {
        return checkLogin(session, model, "templates"); // Return templates page if logged in
    }

    // Customers route
    @GetMapping("/customers")
    public String showCustomers(HttpSession session, Model model) {
        return checkLogin(session, model, "customer"); // Return customers page if logged in
    }

    // HR route
    @GetMapping("/hr")
    public String showHR(HttpSession session, Model model) {
        return checkLogin(session, model, "HUMANResource"); // Return HR page if logged in
    }

    // Transport route
    @GetMapping("/transport")
    public String showTransport(HttpSession session, Model model) {
        return checkLogin(session, model, "transport"); // Return transport page if logged in
    }

    // Optional: Log out route (already present in your previous controller)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate session to log out
        return "redirect:/"; // Redirect to login page after logout
    }
    @GetMapping("/")
    public String Login(){
        return "Login";
    }

    @GetMapping("/Pricing")
    public String showPricing(HttpSession session, Model model) {
        return checkLogin(session, model, "Prcing Page"); // Return customers page if logged in
    }

    @GetMapping("/BillPage")
    public String showBillPage(HttpSession session, Model model) {
        return checkLogin(session, model, "Bill Page"); // Return customers page if logged in
    }
}
