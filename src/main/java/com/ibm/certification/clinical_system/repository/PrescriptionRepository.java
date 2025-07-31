package com.ibm.certification.clinical_system.repository;

import com.ibm.certification.clinical_system.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByDoctorId(Long doctorId);
    
    List<Prescription> findByPatientId(Long patientId);
}