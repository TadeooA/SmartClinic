package com.ibm.certification.clinical_system.controller;

import com.ibm.certification.clinical_system.entity.Prescription;
import com.ibm.certification.clinical_system.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPrescription(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> prescriptionData) {
        
        try {
            String token = authHeader.replace("Bearer ", "");
            
            Long doctorId = Long.valueOf(prescriptionData.get("doctorId").toString());
            Long patientId = Long.valueOf(prescriptionData.get("patientId").toString());
            String medication = prescriptionData.get("medication").toString();
            String dosage = prescriptionData.get("dosage").toString();
            String instructions = prescriptionData.get("instructions") != null ? 
                prescriptionData.get("instructions").toString() : "";
            
            Prescription prescription = prescriptionService.savePrescription(
                token, doctorId, patientId, medication, dosage, instructions);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("prescriptionId", prescription.getId());
            response.put("message", "Prescription created successfully");
            response.put("prescription", Map.of(
                "id", prescription.getId(),
                "doctorName", prescription.getDoctor().getName(),
                "patientName", prescription.getPatient().getName(),
                "medication", prescription.getMedication(),
                "dosage", prescription.getDosage(),
                "instructions", prescription.getInstructions() != null ? prescription.getInstructions() : "",
                "createdAt", prescription.getCreatedAt()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Map<String, Object>> getPrescriptionsByDoctor(@PathVariable Long doctorId) {
        try {
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctor(doctorId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("doctorId", doctorId);
            response.put("prescriptions", prescriptions.stream().map(prescription -> Map.of(
                "id", prescription.getId(),
                "patientName", prescription.getPatient().getName(),
                "patientEmail", prescription.getPatient().getEmail(),
                "medication", prescription.getMedication(),
                "dosage", prescription.getDosage(),
                "instructions", prescription.getInstructions() != null ? prescription.getInstructions() : "",
                "createdAt", prescription.getCreatedAt()
            )).toList());
            response.put("count", prescriptions.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getPrescriptionsByPatient(@PathVariable Long patientId) {
        try {
            List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatient(patientId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("patientId", patientId);
            response.put("prescriptions", prescriptions.stream().map(prescription -> Map.of(
                "id", prescription.getId(),
                "doctorName", prescription.getDoctor().getName(),
                "doctorSpecialty", prescription.getDoctor().getSpecialty(),
                "medication", prescription.getMedication(),
                "dosage", prescription.getDosage(),
                "instructions", prescription.getInstructions() != null ? prescription.getInstructions() : "",
                "createdAt", prescription.getCreatedAt()
            )).toList());
            response.put("count", prescriptions.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePrescription(@PathVariable Long id) {
        try {
            prescriptionService.deletePrescription(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Prescription deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}