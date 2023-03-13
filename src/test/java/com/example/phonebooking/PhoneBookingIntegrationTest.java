package com.example.phonebooking;


import com.example.phonebooking.controller.PhoneRO;
import com.example.phonebooking.repository.PhonePO;
import com.example.phonebooking.repository.PhoneRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PhoneBookingIntegrationTest {

    private static final String FLYWAY_LOCATION = "classpath:db/migration";
    private static final String FLYWAY_SCHEMA = "phonebooking";
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private PhoneRepository phoneRepository;

    @BeforeEach
    public void setUp() {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "")
                .locations(FLYWAY_LOCATION)
                .schemas(FLYWAY_SCHEMA)
                .load();
        flyway.migrate();
    }

    @Test
    public void bookPhone_shouldReturnBookedPhone() {
        // Create a phone in the database
        PhonePO phone = new PhonePO();
        phone.setBrand("Samsung");
        phone.setModel("Galaxy S9");
        phone.setBookedAt(LocalDateTime.now());
        phoneRepository.save(phone);

        // Book the phone using the API
        String bookedBy = "John Doe";
        webTestClient.post().uri("/phones/{id}/book?bookedBy={bookedBy}", phone.getId(), bookedBy)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PhoneRO.class)
                .value(p -> {
                    assertEquals(phone.getId(), p.getId());
                    assertTrue(p.isBooked());
                    assertEquals(bookedBy, p.getBookedBy());
                    assertNotNull(p.getBookedAt());
                });
    }

    @Test
    public void bookPhone_withInvalidPhoneId_shouldReturnNotFound() {
        // Book a phone with an invalid ID using the API
        String bookedBy = "John Doe";
        webTestClient.post().uri("/phones/{id}/book?bookedBy={bookedBy}", 999, bookedBy)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void bookPhone_withAlreadyBookedPhone_shouldReturnBadRequest() {
        // Create a phone in the database and book it
        PhonePO phone = new PhonePO();
        phone.setBrand("Samsung");
        phone.setModel("Galaxy S9");
        phone.setBooked(true);
        phone.setBookedBy("Jane Doe");
        phone.setBookedAt(LocalDateTime.now());
        phoneRepository.save(phone);

        // Attempt to book the phone again using the API
        String bookedBy = "John Doe";
        webTestClient.post().uri("/phones/{id}/book?bookedBy={bookedBy}", phone.getId(), bookedBy)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void returnPhone_shouldReturnReturnedPhone() {
        // Create a phone in the database and book it
        PhonePO phone = new PhonePO();
        phone.setBrand("Samsung");
        phone.setModel("Galaxy S9");
        phone.setBooked(true);
        phone.setBookedBy("John Doe");
        phone.setBookedAt(LocalDateTime.now());
        phoneRepository.save(phone);

        // Return the phone using the API
        webTestClient.post().uri("/phones/{id}/return", phone.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(PhoneRO.class)
                .value(p -> {
                    assertEquals(phone.getId(), p.getId());
                    assertFalse(p.isBooked());
                    assertNull(p.getBookedBy());
                    assertNull(p.getBookedAt());
                });
    }

    @Test
    public void returnPhone_withInvalidPhoneId_shouldReturnNotFound() {
        // Return a phone with an invalid ID using the API
        webTestClient.post().uri("/phones/{id}/return", 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void returnPhone_withNotBookedPhone_shouldReturnBadRequest() {
        // Create a phone in the database and mark it as not booked
        PhonePO phone = new PhonePO();
        phone.setBrand("Samsung");
        phone.setModel("Galaxy S9");
        phoneRepository.save(phone);

        // Attempt to return the phone using the API
        webTestClient.post().uri("/phones/{id}/return", phone.getId())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void getAllPhones_shouldReturnAllPhones() {
        // Create some phones in the database
        PhonePO phonePO1 = new PhonePO();
        phonePO1.setBrand("Samsung");
        phonePO1.setModel("Galaxy S9");
        phoneRepository.save(phonePO1);

        PhonePO phonePO2 = new PhonePO();
        phonePO2.setBrand("Apple");
        phonePO2.setModel("iPhone 12");
        phonePO2.setBooked(true);
        phonePO2.setBookedBy("John Doe");
        phonePO2.setBookedAt(LocalDateTime.now());
        phoneRepository.save(phonePO2);

        // Get all phones using the API
        webTestClient.get().uri("/phones")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PhoneRO.class)
                .hasSize(12);
    }

}
