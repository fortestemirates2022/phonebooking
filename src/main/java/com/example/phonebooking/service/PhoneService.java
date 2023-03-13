package com.example.phonebooking.service;

import com.example.phonebooking.exceptions.PhoneAlreadyBookedException;
import com.example.phonebooking.exceptions.PhoneNotBookedException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing Phone entities.
 */
public interface PhoneService {

    /**
     * Books a Phone with the given ID.
     *
     * @param id the ID of the Phone to book
     * @param bookedBy the name of the person booking the Phone
     * @return a Mono containing the updated Phone entity
     * @throws PhoneAlreadyBookedException if the Phone is already booked
     */
    Mono<PhoneDTO> bookPhone(Long id, String bookedBy);

    /**
     * Returns a Phone with the given ID.
     *
     * @param id the ID of the Phone to return
     * @return a Mono containing the updated Phone entity
     * @throws PhoneNotBookedException if the Phone is not already booked
     */
    Mono<PhoneDTO> returnPhone(Long id);

    /**
     * Retrieves all Phone entities.
     *
     * @return a Flux of all Phone entities
     */
    Flux<PhoneDTO> getAllPhones();
}

