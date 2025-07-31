package com.ibm.certification.clinical_system.repository;

import com.ibm.certification.clinical_system.entity.Appointment;
import com.ibm.certification.clinical_system.entity.Doctor;
import com.ibm.certification.clinical_system.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctor(Doctor doctor);
    
    List<Appointment> findByPatient(Patient patient);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND DATE(a.appointmentTime) = :date ORDER BY a.appointmentTime")
    List<Appointment> findByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);
    
    @Query("SELECT a FROM Appointment a JOIN a.patient p " +
           "WHERE p.email = :email OR p.phone = :phone " +
           "ORDER BY a.appointmentTime DESC")
    List<Appointment> findByPatientEmailOrPhone(@Param("email") String email, @Param("phone") String phone);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.appointmentTime BETWEEN :startTime AND :endTime")
    List<Appointment> findByDoctorAndTimeBetween(@Param("doctorId") Long doctorId, 
                                               @Param("startTime") LocalDateTime startTime, 
                                               @Param("endTime") LocalDateTime endTime);
}