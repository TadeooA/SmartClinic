package com.ibm.certification.clinical_system.service;

import com.ibm.certification.clinical_system.entity.Doctor;
import com.ibm.certification.clinical_system.entity.Patient;
import com.ibm.certification.clinical_system.entity.Prescription;
import com.ibm.certification.clinical_system.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {
    
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private TokenService tokenService;
    
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }
    
    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }
    
    public Prescription savePrescription(String authToken, Long doctorId, Long patientId, 
                                       String medication, String dosage, String instructions) {
        if (!tokenService.validateToken(authToken)) {
            throw new RuntimeException("Invalid or expired token");
        }
        
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        
        if (doctorOpt.isPresent() && patientOpt.isPresent()) {
            Prescription prescription = new Prescription(
                doctorOpt.get(),
                patientOpt.get(),
                medication,
                dosage,
                instructions
            );
            
            return prescriptionRepository.save(prescription);
        } else {
            throw new RuntimeException("Doctor or Patient not found");
        }
    }
    
    public List<Prescription> getPrescriptionsByDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }
    
    public List<Prescription> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }
    
    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }
}