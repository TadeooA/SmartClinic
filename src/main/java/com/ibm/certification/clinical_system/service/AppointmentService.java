package com.ibm.certification.clinical_system.service;

import com.ibm.certification.clinical_system.entity.Appointment;
import com.ibm.certification.clinical_system.entity.Doctor;
import com.ibm.certification.clinical_system.entity.Patient;
import com.ibm.certification.clinical_system.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private PatientService patientService;
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }
    
    public Appointment bookAppointment(Long doctorId, Long patientId, LocalDateTime appointmentTime, String notes) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        
        if (doctorOpt.isPresent() && patientOpt.isPresent()) {
            if (!doctorService.isDoctorAvailableAtTime(doctorId, appointmentTime)) {
                throw new RuntimeException("Doctor is not available at the requested time");
            }
            
            Appointment appointment = new Appointment(
                doctorOpt.get(), 
                patientOpt.get(), 
                appointmentTime, 
                notes
            );
            
            return appointmentRepository.save(appointment);
        } else {
            throw new RuntimeException("Doctor or Patient not found");
        }
    }
    
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        if (doctorOpt.isPresent()) {
            return appointmentRepository.findByDoctor(doctorOpt.get());
        }
        throw new RuntimeException("Doctor not found");
    }
    
    public List<Appointment> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorAndDate(doctorId, date);
    }
    
    public List<Appointment> getAppointmentsByPatientCredentials(String email, String phone) {
        return appointmentRepository.findByPatientEmailOrPhone(email, phone);
    }
    
    public Appointment updateAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
    
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}