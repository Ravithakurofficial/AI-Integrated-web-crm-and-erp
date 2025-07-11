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

@Controller // Change @RestController to @Controller
public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeadRepository leadRepository;

    @PostMapping("/auth")
    public String authenticateUser(@RequestParam("gmail") String gmail,
                                   @RequestParam("password") String password,
                                   HttpSession session, Model model) {
        if (userService.authenticate(gmail, password)) {
            User user = userService.getUserByGmail(gmail);
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard"; // Redirect to DashBoard
        } else {
            model.addAttribute("error", "Invalid Gmail or Password");
            return "login"; // Remains on the login page
        }
    }

    @PostMapping("/addEmp")
    public String addUser(@RequestParam("gmail") String gmail,
                          @RequestParam("password") String password,
                          @RequestParam("phone") String phone,
                          Model model) {
        Optional<User> userOptional = userRepository.findByGmail(gmail);

        if (userOptional.isPresent()) {
            model.addAttribute("error", "User Already Exists");
            return "HUMANResource";
        }

        if (gmail.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            model.addAttribute("error", "All fields are required!");
            return "HUMANResource";
        }

        User newUser = new User(gmail, password, phone);

        try {
            userRepository.save(newUser);
            model.addAttribute("success", "User Added Successfully");
        } catch (Exception e) {
            model.addAttribute("error", "Error while adding user: " + e.getMessage());
        }

        return "redirect:/HUMANResource"; // Redirect back to HR page
    }
    @PostMapping("/addLead")
    public String addLead(@RequestParam("email") String gmail,
                          @RequestParam("phone") String phone,
                          @RequestParam("leadName") String leadName,
                          @RequestParam("address") String address,
                          @RequestParam("status") String status,
                          RedirectAttributes redirectAttributes) {

        if (gmail.isEmpty() || phone.isEmpty() || leadName.isEmpty() || address.isEmpty() || status.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "All fields are required!");
            return "redirect:/leads";
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



    @GetMapping("/displayData")
    @ResponseBody // Since this returns JSON data
    public List<User> displayData() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/employees/{id}")
    @ResponseBody // Since this returns JSON data
    public String deleteEmployee(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return "Employee deleted successfully";
        }
        return "User not found";
    }
}
