package com.ibm.certification.clinical_system.repository;

import com.ibm.certification.clinical_system.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);
    
    List<Doctor> findBySpecialty(String specialty);
    
    @Query("SELECT d FROM Doctor d WHERE d.availableTimes IS NOT NULL AND d.availableTimes != ''")
    List<Doctor> findAvailableDoctors();
    
    @Query("SELECT d FROM Doctor d LEFT JOIN d.appointments a " +
           "WHERE d.id = :doctorId AND " +
           "(a.appointmentTime IS NULL OR " +
           "a.appointmentTime NOT BETWEEN :startTime AND :endTime)")
    List<Doctor> findDoctorAvailableAtTime(@Param("doctorId") Long doctorId, 
                                          @Param("startTime") LocalDateTime startTime, 
                                          @Param("endTime") LocalDateTime endTime);
}