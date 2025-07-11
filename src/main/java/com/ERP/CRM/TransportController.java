package com.ERP.CRM;

import com.ERP.CRM.Transportdb;
import com.ERP.CRM.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // Use @Controller for view/redirect behavior
@CrossOrigin(origins = "*")
public class TransportController {

    @Autowired
    private TransportService transportService;

    @PostMapping("/AddTransData")
    public String addTransportViaForm(@ModelAttribute Transportdb transport) {
        transportService.saveTransport(transport);
        return "redirect:/transport"; // 🔁 Redirect to /transport after saving
    }

    @ResponseBody
    @GetMapping("/DisplayTransData")
    public List<Transportdb> getAllTransportData() {
        return transportService.getAllTransports();
    }
}
