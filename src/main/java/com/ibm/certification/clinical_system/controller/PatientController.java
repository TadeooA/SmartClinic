package com.ibm.certification.clinical_system.controller;

import com.ibm.certification.clinical_system.entity.Patient;
import com.ibm.certification.clinical_system.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {
    
    @Autowired
    private PatientService patientService;
    
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchPatient(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone) {
        
        if ((email == null || email.isEmpty()) && (phone == null || phone.isEmpty())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email or phone is required"));
        }
        
        Optional<Patient> patientOpt = patientService.getPatientByEmailOrPhone(email, phone);
        Map<String, Object> response = new HashMap<>();
        
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            response.put("found", true);
            response.put("patient", Map.of(
                "id", patient.getId(),
                "name", patient.getName(),
                "email", patient.getEmail(),
                "phone", patient.getPhone(),
                "address", patient.getAddress() != null ? patient.getAddress() : ""
            ));
        } else {
            response.put("found", false);
            response.put("message", "Patient not found");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        try {
            if (patientService.existsByEmail(patient.getEmail())) {
                return ResponseEntity.badRequest().build();
            }
            
            Patient savedPatient = patientService.savePatient(patient);
            return ResponseEntity.ok(savedPatient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search-by-name")
    public ResponseEntity<Map<String, Object>> searchPatientsByName(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Name is required"));
        }
        
        var patients = patientService.searchPatientsByName(name);
        Map<String, Object> response = new HashMap<>();
        response.put("patients", patients);
        response.put("count", patients.size());
        
        return ResponseEntity.ok(response);
    }
}