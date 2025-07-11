package com.ERP.CRM;

import com.ERP.CRM.Transportdb;
import com.ERP.CRM.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportService {

    @Autowired
    private TransportRepository transportRepository;

    public Transportdb saveTransport(Transportdb transport) {
        return transportRepository.save(transport);
    }

    public List<Transportdb> getAllTransports() {
        return transportRepository.findAll();
    }

    public void deleteTransport(int id) {
        transportRepository.deleteById(id);
    }

    public Transportdb getById(int id) {
        return transportRepository.findById(id).orElse(null);
    }
}
