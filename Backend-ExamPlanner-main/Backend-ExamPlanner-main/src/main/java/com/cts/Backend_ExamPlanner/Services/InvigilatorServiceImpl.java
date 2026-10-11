package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.dto.InvigilatorRequestDTO;
import com.cts.Backend_ExamPlanner.entities.Department;
import com.cts.Backend_ExamPlanner.entities.Invigilator;
import com.cts.Backend_ExamPlanner.entities.User;
import com.cts.Backend_ExamPlanner.repositories.DepartmentRepository;
import com.cts.Backend_ExamPlanner.repositories.InvigilatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class InvigilatorServiceImpl implements InvigilatorService {

    @Autowired
    private InvigilatorRepository invigilatorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Invigilator createInvigilator(InvigilatorRequestDTO dto) {
        // Check ID conflict
        if (invigilatorRepository.existsById(dto.getInvigilatorId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Invigilator ID already exists: " + dto.getInvigilatorId());
        }



        // Validate department
        Department dept = departmentRepository.findByDepartmentCode(dto.getDepartmentCode())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Department not found: " + dto.getDepartmentCode()));

        // Build nested User
        User newUser = new User();
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());

        newUser.setRole("INVIGILATOR");
        newUser.setPassword(passwordEncoder.encode("Password123"));

        // Build Invigilator
        Invigilator inv = new Invigilator();
        inv.setInvigilatorId(dto.getInvigilatorId());
        inv.setName(dto.getName());
        inv.setEmail(dto.getEmail());
        inv.setPhone(dto.getPhone());
        inv.setDepartment(dept);
        inv.setUser(newUser); // ensure cascade in entity mapping

        try {
            return invigilatorRepository.save(inv);
        } catch (DataIntegrityViolationException ex) {
            // In case the unique constraint on User.email triggers
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Conflict: Duplicate or invalid data");
        }
    }

    @Override
    public Page<Invigilator> getPaginatedInvigilator(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return invigilatorRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Invigilator updateInvigilator(InvigilatorRequestDTO dto) {
        Invigilator existing = invigilatorRepository.findById(dto.getInvigilatorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Invigilator not found with ID: " + dto.getInvigilatorId()));

        if (dto.getDepartmentCode() != null
                && !dto.getDepartmentCode().equals(existing.getDepartment().getDepartmentCode())) {
            Department newDept = departmentRepository.findByDepartmentCode(dto.getDepartmentCode())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Department not found: " + dto.getDepartmentCode()));
            existing.setDepartment(newDept);
        }


        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
        if(dto.getPhone()!=null) existing.setPhone(dto.getPhone());
        User user = existing.getUser();
        if (user != null) {
            if (dto.getName() != null) user.setName(dto.getName());
            if (dto.getEmail() != null) user.setEmail(dto.getEmail());

        }

        try {
            return invigilatorRepository.save(existing);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Conflict: Duplicate or invalid data");
        }
    }

    @Transactional
    @Override
    public void deleteInvigilaor(String id) {
        Invigilator inv = invigilatorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Invigilator with ID " + id + " not found"));
        invigilatorRepository.delete(inv);
    }

    @Override
    public List<InvigilatorRequestDTO> getAvailableInvigilators(String deptCode,LocalDate date, LocalTime startTime, int durationMinutes) {
        LocalTime endTime = startTime.plusMinutes(durationMinutes);

        List<Invigilator> available = invigilatorRepository.findAvailableInvigilators(deptCode,date, startTime, endTime);

        return available.stream()
                .map(i -> {
                    InvigilatorRequestDTO e = new InvigilatorRequestDTO();
                    e.setInvigilatorId(i.getInvigilatorId());
                    e.setName(i.getName());
                    e.setEmail(i.getEmail());
                    e.setPhone(i.getPhone());
                    e.setDepartmentCode(i.getDepartment().getDepartmentCode());
                    return e;
                }).toList();
    }
}