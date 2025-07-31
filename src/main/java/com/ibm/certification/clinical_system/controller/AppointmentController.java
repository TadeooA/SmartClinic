package com.ibm.certification.clinical_system.controller;

import com.ibm.certification.clinical_system.entity.Appointment;
import com.ibm.certification.clinical_system.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> bookAppointment(@RequestBody Map<String, Object> appointmentData) {
        try {
            Long doctorId = Long.valueOf(appointmentData.get("doctorId").toString());
            Long patientId = Long.valueOf(appointmentData.get("patientId").toString());
            String appointmentTimeStr = appointmentData.get("appointmentTime").toString();
            String notes = appointmentData.get("notes") != null ? appointmentData.get("notes").toString() : "";
            
            LocalDateTime appointmentTime = LocalDateTime.parse(appointmentTimeStr);
            
            Appointment appointment = appointmentService.bookAppointment(doctorId, patientId, appointmentTime, notes);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("appointmentId", appointment.getId());
            response.put("message", "Appointment booked successfully");
            response.put("appointment", Map.of(
                "id", appointment.getId(),
                "doctorName", appointment.getDoctor().getName(),
                "patientName", appointment.getPatient().getName(),
                "appointmentTime", appointment.getAppointmentTime(),
                "status", appointment.getStatus(),
                "notes", appointment.getNotes() != null ? appointment.getNotes() : ""
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<Map<String, Object>> getAppointmentsByDoctorAndDate(
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByDoctorAndDate(doctorId, date);
            
            Map<String, Object> response = new HashMap<>();
            response.put("doctorId", doctorId);
            response.put("date", date);
            response.put("appointments", appointments.stream().map(appointment -> Map.of(
                "id", appointment.getId(),
                "patientName", appointment.getPatient().getName(),
                "patientEmail", appointment.getPatient().getEmail(),
                "appointmentTime", appointment.getAppointmentTime(),
                "status", appointment.getStatus(),
                "notes", appointment.getNotes() != null ? appointment.getNotes() : ""
            )).toList());
            response.put("count", appointments.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/patient/search")
    public ResponseEntity<Map<String, Object>> getAppointmentsByPatientCredentials(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone) {
        
        if ((email == null || email.isEmpty()) && (phone == null || phone.isEmpty())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email or phone is required"));
        }
        
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByPatientCredentials(email, phone);
            
            Map<String, Object> response = new HashMap<>();
            response.put("appointments", appointments.stream().map(appointment -> Map.of(
                "id", appointment.getId(),
                "doctorName", appointment.getDoctor().getName(),
                "doctorSpecialty", appointment.getDoctor().getSpecialty(),
                "appointmentTime", appointment.getAppointmentTime(),
                "status", appointment.getStatus(),
                "notes", appointment.getNotes() != null ? appointment.getNotes() : ""
            )).toList());
            response.put("count", appointments.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateAppointmentStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, String> statusUpdate) {
        
        try {
            var appointmentOpt = appointmentService.getAppointmentById(id);
            if (!appointmentOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Appointment appointment = appointmentOpt.get();
            String newStatus = statusUpdate.get("status");
            appointment.setStatus(com.ibm.certification.clinical_system.entity.AppointmentStatus.valueOf(newStatus));
            
            appointmentService.updateAppointment(appointment);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Appointment status updated successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}