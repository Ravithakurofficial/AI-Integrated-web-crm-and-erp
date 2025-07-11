package com.ERP.CRM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LeadRepository leadRepository;

    public Map<String, Long> getLeadStatusCounts() {
        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("NEW", leadRepository.countByStatus("NEW"));
        statusCounts.put("Open", leadRepository.countByStatus("Open"));
        statusCounts.put("Sent", leadRepository.countByStatus("Sent"));
        statusCounts.put("In_Progress", leadRepository.countByStatus("In_Progress"));
        statusCounts.put("CLOSED", leadRepository.countByStatus("Customer"));

        return statusCounts;
    }
    public boolean authenticate(String gmail, String password) {
        Optional<User> userOpt = userRepository.findByGmail(gmail);
        return userOpt.isPresent() && userOpt.get().getPassword().equals(password);
    }

    public User getUserByGmail(String gmail) {
        return userRepository.findByGmail(gmail).orElse(null);
    }

    public boolean addUser(String gmail, String password, String phone) {
        if (userRepository.findByGmail(gmail).isPresent()) {
            return false;
        }
        User newUser = new User(gmail, password, phone);
        userRepository.save(newUser);
        return true;
    }

    @ResponseBody
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
