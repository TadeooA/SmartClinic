package com.ibm.certification.clinical_system.service;

import com.ibm.certification.clinical_system.entity.Doctor;
import com.ibm.certification.clinical_system.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }
    
    public boolean validateDoctorCredentials(String email) {
        return doctorRepository.findByEmail(email).isPresent();
    }
    
    public List<String> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        List<String> timeSlots = new ArrayList<>();
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            String availableTimes = doctor.getAvailableTimes();
            
            if (availableTimes != null && !availableTimes.isEmpty()) {
                String[] slots = availableTimes.split(",");
                for (String slot : slots) {
                    timeSlots.add(slot.trim());
                }
            } else {
                for (int hour = 9; hour <= 17; hour++) {
                    timeSlots.add(String.format("%02d:00", hour));
                }
            }
        }
        
        return timeSlots;
    }
    
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
    
    public List<Doctor> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty);
    }
    
    public boolean isDoctorAvailableAtTime(Long doctorId, LocalDateTime appointmentTime) {
        LocalDateTime startTime = appointmentTime.minusMinutes(30);
        LocalDateTime endTime = appointmentTime.plusMinutes(30);
        
        List<Doctor> availableDoctors = doctorRepository.findDoctorAvailableAtTime(
            doctorId, startTime, endTime);
        
        return !availableDoctors.isEmpty();
    }
}