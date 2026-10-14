package com.example.petclinic.service;

import com.example.petclinic.entity.Pet;
import com.example.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> findPets() {
        return petRepository.findAll();
    }

    /**
     * 현재 날짜 기준 생후 {@code year}년 미만인 해당 종류의 동물을 조회한다.
     * year 와 type 은 함께 전달되어야 하며, 하나만 전달되면 IllegalArgumentException 이 발생한다.
     */
    public List<Pet> findPets(Integer year, String type) {
        boolean hasType = type != null && !type.isBlank();
        if (year == null && !hasType) {
            return findPets();
        }
        if (year == null || !hasType) {
            throw new IllegalArgumentException("year and type must be provided together");
        }
        if (year <= 0) {
            throw new IllegalArgumentException("year must be greater than 0");
        }
        return petRepository.findByTypeNameIgnoreCaseAndBirthDateAfter(
                type.trim(), LocalDateTime.now().minusYears(year));
    }

    public Pet findPet(Long petId) {
        return verifyPetExists(petId);
    }

    private Pet verifyPetExists(Long petId) {
        return petRepository.findById(petId).orElseThrow(
                () -> new NoSuchElementException("Pet does not exist")
        );
    }
}
