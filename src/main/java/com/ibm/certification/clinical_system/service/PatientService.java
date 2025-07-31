package com.ibm.certification.clinical_system.service;

import com.ibm.certification.clinical_system.entity.Patient;
import com.ibm.certification.clinical_system.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    
    @Autowired
    private PatientRepository patientRepository;
    
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }
    
    public Optional<Patient> getPatientByEmailOrPhone(String email, String phone) {
        if (email != null && !email.isEmpty()) {
            Optional<Patient> patientByEmail = patientRepository.findByEmail(email);
            if (patientByEmail.isPresent()) {
                return patientByEmail;
            }
        }
        
        if (phone != null && !phone.isEmpty()) {
            return patientRepository.findByPhone(phone);
        }
        
        return Optional.empty();
    }
    
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }
    
    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }
    
    public boolean existsByEmail(String email) {
        return patientRepository.findByEmail(email).isPresent();
    }
    
    public boolean existsByPhone(String phone) {
        return patientRepository.findByPhone(phone).isPresent();
    }
}