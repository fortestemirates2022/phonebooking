package com.example.phonebooking.service;


import com.example.phonebooking.converter.PhoneConverter;
import com.example.phonebooking.exceptions.PhoneAlreadyBookedException;
import com.example.phonebooking.exceptions.PhoneNotBookedException;
import com.example.phonebooking.exceptions.PhoneNotFoundException;
import com.example.phonebooking.repository.PhonePO;
import com.example.phonebooking.repository.PhoneRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;

    public PhoneServiceImpl(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public Mono<PhoneDTO> bookPhone(Long id, String bookedBy) {
        Optional<PhonePO> phonePO = phoneRepository.findById(id);
        if (phonePO.isEmpty()) {
            return Mono.error(new PhoneNotFoundException());
        }
        PhoneDTO phone = PhoneConverter.toDTO(phonePO.get());
        if (phone.isBooked()) {
            return Mono.error(new PhoneAlreadyBookedException());
        }
        phone.setBookedBy(bookedBy);
        phone.setBookedAt(LocalDateTime.now());
        phone.setBooked(true);
        return Mono.justOrEmpty(PhoneConverter.toDTO(phoneRepository.save(PhoneConverter.toPO(phone))));
    }

    @Override
    public Mono<PhoneDTO> returnPhone(Long id) {
        Optional<PhonePO> phonePO = phoneRepository.findById(id);
        if (phonePO.isEmpty()) {
            return Mono.error(new PhoneNotFoundException());
        }
        PhoneDTO phone = PhoneConverter.toDTO(phonePO.get());
        if (!phone.isBooked()) {
            return Mono.error(new PhoneNotBookedException());
        }
        phone.setBookedBy(null);
        phone.setBooked(false);
        phone.setBookedAt(null);
        return Mono.justOrEmpty(PhoneConverter.toDTO(phoneRepository.save(PhoneConverter.toPO(phone))));
    }

    @Override
    public Flux<PhoneDTO> getAllPhones() {
        return Flux.fromIterable(PhoneConverter.toDTOs(phoneRepository.findAll()));
    }
}


