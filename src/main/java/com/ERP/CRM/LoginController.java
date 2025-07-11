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
        if (userService.authenticate(gmail, password)) {
            User user = userService.getUserByGmail(gmail);
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard"; // ✅ Must match the mapping in PageController
        } else {
            model.addAttribute("error", "Invalid Gmail or Password");
            return "Login"; // ✅ Make sure Login.html exists and is named with capital "L" if used
        }
    }

    // 👥 Add Employee
    @PostMapping("/addEmp")
    public String addUser(@RequestParam("gmail") String gmail,
                          @RequestParam("password") String password,
                          @RequestParam("phone") String phone,
                          RedirectAttributes redirectAttributes) {

        if (gmail.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "All fields are required!");
            return "redirect:/hr"; // ✅ redirect to mapped /hr which returns HUMANResource.html
        }

        Optional<User> userOptional = userRepository.findByGmail(gmail);
        if (userOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "User Already Exists");
            return "redirect:/hr";
        }

        User newUser = new User(gmail, password, phone);
        try {
            userRepository.save(newUser);
            redirectAttributes.addFlashAttribute("success", "User Added Successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error while adding user: " + e.getMessage());
        }

        return "redirect:/hr"; // ✅ /hr is mapped in PageController → HUMANResource.html
    }

    // 📋 Add Lead
    @PostMapping("/addLead")
    public String addLead(@RequestParam("email") String gmail,
                          @RequestParam("phone") String phone,
                          @RequestParam("leadName") String leadName,
                          @RequestParam("address") String address,
                          @RequestParam("status") String status,
                          RedirectAttributes redirectAttributes) {

        if (gmail.isEmpty() || phone.isEmpty() || leadName.isEmpty() || address.isEmpty() || status.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "All fields are required!");
            return "redirect:/leads"; // ✅ mapped in PageController
        }

        boolean userExists = userRepository.findByGmail(gmail).isPresent();
        boolean leadExists = leadRepository.findByGmail(gmail).isPresent();

        if (userExists || leadExists) {
            redirectAttributes.addFlashAttribute("error", "Lead email already exists");
            return "redirect:/leads";
        }

        LeadsDB newLead = new LeadsDB();
        newLead.setCustomerName(leadName);
        newLead.setAddress(address);
        newLead.setPhoneNumber(phone);
        newLead.setGmail(gmail);
        newLead.setStatus(status);

        try {
            leadRepository.save(newLead);
            redirectAttributes.addFlashAttribute("success", "Lead Added Successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error while adding lead: " + e.getMessage());
        }

        return "redirect:/leads";
    }

    // 📦 Get All Users (API)
    @GetMapping("/displayData")
    @ResponseBody
    public List<User> displayData() {
        return userService.getAllUsers();
    }

    // ❌ Delete Employee (API)
    @DeleteMapping("/employees/{id}")
    @ResponseBody
    public String deleteEmployee(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return "Employee deleted successfully";
        }
        return "User not found";
    }
}
