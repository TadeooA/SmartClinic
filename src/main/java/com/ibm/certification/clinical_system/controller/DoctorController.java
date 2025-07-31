package com.ibm.certification.clinical_system.controller;

import com.ibm.certification.clinical_system.entity.Doctor;
import com.ibm.certification.clinical_system.service.DoctorService;
import com.ibm.certification.clinical_system.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private TokenService tokenService;
    
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
    
    @GetMapping("/availability/{doctorId}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        if (!doctorOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        List<String> availableSlots = doctorService.getAvailableTimeSlots(doctorId, date);
        
        Map<String, Object> response = new HashMap<>();
        response.put("doctorId", doctorId);
        response.put("doctorName", doctorOpt.get().getName());
        response.put("date", date);
        response.put("availableTimeSlots", availableSlots);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateDoctorCredentials(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }
        
        boolean isValid = doctorService.validateDoctorCredentials(email);
        Map<String, Object> response = new HashMap<>();
        
        if (isValid) {
            String token = tokenService.generateJWTToken(email, "DOCTOR");
            response.put("valid", true);
            response.put("token", token);
            response.put("role", "DOCTOR");
        } else {
            response.put("valid", false);
            response.put("message", "Invalid doctor credentials");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialty(@PathVariable String specialty) {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialty(specialty);
        return ResponseEntity.ok(doctors);
    }
    
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        try {
            Doctor savedDoctor = doctorService.saveDoctor(doctor);
            return ResponseEntity.ok(savedDoctor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}