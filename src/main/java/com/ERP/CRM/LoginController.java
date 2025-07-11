package com.ERP.CRM;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@CrossOrigin(origins = "*") // You can restrict to specific domains in production
public class LoginController {

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeadRepository leadRepository;

    // 🔐 Login authentication
    @PostMapping("/auth")
    public String authenticateUser(@RequestParam("gmail") String gmail,
                                   @RequestParam("password") String password,
                                   HttpSession session,
                                   Model model) {
        try {
            if (userService.authenticate(gmail, password)) {
                session.setAttribute("loggedInUser", userService.getUserByGmail(gmail));
                return "redirect:/hr"; // PageController maps this
            } else {
                model.addAttribute("error", "Invalid Gmail or Password");
                return "Login"; // Make sure Login.html exists
            }
        } catch (Exception e) {
            model.addAttribute("error", "Server error: " + e.getMessage());
            return "Login";
        }
    }

    // 👥 Add Employee
    @PostMapping("/addEmp")
    public String addUser(@RequestParam("gmail") String gmail,
                          @RequestParam("password") String password,
                          @RequestParam("phone") String phone,
                          RedirectAttributes redirectAttributes) {

        if (gmail.isBlank() || password.isBlank() || phone.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "All fields are required!");
            return "redirect:/hr";
        }

        if (userRepository.findByGmail(gmail).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "User already exists!");
            return "redirect:/hr";
        }

        try {
            userRepository.save(new User(gmail, password, phone));
            redirectAttributes.addFlashAttribute("success", "User added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add user: " + e.getMessage());
        }

        return "redirect:/hr";
    }

    // 📋 Add Lead
    @PostMapping("/addLead")
    public String addLead(@RequestParam("email") String gmail,
                          @RequestParam("phone") String phone,
                          @RequestParam("leadName") String leadName,
                          @RequestParam("address") String address,
                          @RequestParam("status") String status,
                          RedirectAttributes redirectAttributes) {

        if (gmail.isBlank() || phone.isBlank() || leadName.isBlank() || address.isBlank() || status.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "All fields are required!");
            return "redirect:/leads";
        }

        if (userRepository.findByGmail(gmail).isPresent() || leadRepository.findByGmail(gmail).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Lead email already exists.");
            return "redirect:/leads";
        }

        try {
            LeadsDB lead = new LeadsDB();
            lead.setCustomerName(leadName);
            lead.setAddress(address);
            lead.setPhoneNumber(phone);
            lead.setGmail(gmail);
            lead.setStatus(status);
            leadRepository.save(lead);
            redirectAttributes.addFlashAttribute("success", "Lead added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add lead: " + e.getMessage());
        }

        return "redirect:/leads";
    }

    // 📦 Get All Users (JSON for frontend/API)
    @GetMapping("/displayData")
    @ResponseBody
    public List<User> displayData() {
        return userService.getAllUsers();
    }

    // ❌ Delete Employee (JSON API)
    @DeleteMapping("/employees/{id}")
    @ResponseBody
    public String deleteEmployee(@PathVariable Long id) {
        return userService.deleteUser(id)
                ? "Employee deleted successfully"
                : "User not found";
    }
}
