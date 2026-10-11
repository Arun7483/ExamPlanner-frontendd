package com.cts.Backend_ExamPlanner.Services;

import com.cts.Backend_ExamPlanner.dto.HallDTO;
import com.cts.Backend_ExamPlanner.entities.HallAllocation;
import com.cts.Backend_ExamPlanner.repositories.HallAllocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallAllocationService {
    private final HallAllocationRepository repository;

    public HallAllocationService(HallAllocationRepository repository) {
        this.repository = repository;
    }

    public List<HallAllocation> getAll() {
        return repository.findAll();
    }

    public HallAllocation save(HallAllocation hall) {
        if (repository.existsById(hall.getHallId())) {
            throw new RuntimeException("Hall with ID " + hall.getHallId() + " already exists.");
        }
        return repository.save(hall);
    }

    public void delete(String hallId) {
        if (!repository.existsById(hallId)) {
            throw new RuntimeException("Hall not found.");
        }
        repository.deleteById(hallId);
    }

    public HallAllocation update(String hallId, HallAllocation details) {
        return repository.findById(hallId)
                .map(existing -> {
                    existing.setCapacity(details.getCapacity());
                    existing.setFloor(details.getFloor());
                    existing.setAvailability(details.getAvailability());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Hall not found with ID " + hallId));
    }


    //public List<HallDTO> findAvailableHalls(){
        //List<HallAllocation> availableHalls=repository.findByAvailabilityIgnoreCase("yes");
        //return availableHalls.stream().map(hallAllocation -> new HallDTO(hallAllocation.getHallId(), hallAllocation.getCapacity(), hallAllocation.getFloor(), hallAllocation.getAvailability())).toList();
    //}
}
