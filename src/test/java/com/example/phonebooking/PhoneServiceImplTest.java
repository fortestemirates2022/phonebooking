package com.example.phonebooking;

import com.example.phonebooking.exceptions.PhoneAlreadyBookedException;
import com.example.phonebooking.exceptions.PhoneNotBookedException;
import com.example.phonebooking.exceptions.PhoneNotFoundException;
import com.example.phonebooking.repository.PhonePO;
import com.example.phonebooking.repository.PhoneRepository;
import com.example.phonebooking.service.PhoneDTO;
import com.example.phonebooking.service.PhoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class PhoneServiceImplTest {

    @Mock
    private PhoneRepository phoneRepository;

    @InjectMocks
    private PhoneServiceImpl phoneService;

    private PhonePO samsungGalaxyS9;
    private PhonePO samsungGalaxyS8;
    private PhonePO motorolaNexus6;
    private PhonePO appleiPhone12;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        samsungGalaxyS9 = new PhonePO(1L, "Samsung", "Galaxy S9", false, null, null);
        samsungGalaxyS8 = new PhonePO(2L, "Samsung", "Galaxy S8", false, null, null);
        motorolaNexus6 = new PhonePO(3L, "Motorola", "Nexus 6", false, null, null);
        appleiPhone12 = new PhonePO(6L, "Apple", "iPhone 12", true, null, "Jane Doe");
    }

    @Test
    public void bookPhone_withValidInput_shouldReturnBookedPhone() {
        // Arrange
        String bookedBy = "John Doe";
        samsungGalaxyS9.setBooked(false);
        when(phoneRepository.findById(1L)).thenReturn(Optional.of(samsungGalaxyS9));
        when(phoneRepository.save(any(PhonePO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Mono<PhoneDTO> result = phoneService.bookPhone(1L, bookedBy);

        // Assert
        assertEquals(samsungGalaxyS9.getModel(), Objects.requireNonNull(result.block()).getModel());
        assertEquals(bookedBy, Objects.requireNonNull(result.block()).getBookedBy());
        assertTrue(Objects.requireNonNull(result.block()).isBooked());
    }

    @Test
    public void bookPhone_withNonExistingPhone_shouldThrowPhoneNotFoundException() {
        // Arrange
        String bookedBy = "John Doe";
        when(phoneRepository.findById(10L)).thenReturn(Optional.empty());

        // Act
        Mono<PhoneDTO> result = phoneService.bookPhone(10L, bookedBy);

        // Assert
        assertThrows(PhoneNotFoundException.class, result::block);
    }

    @Test
    public void bookPhone_withAlreadyBookedPhone_shouldThrowPhoneAlreadyBookedException() {
        // Arrange
        String bookedBy = "John Doe";
        when(phoneRepository.findById(6L)).thenReturn(Optional.of(appleiPhone12));
        Mono<PhoneDTO> result = phoneService.bookPhone(6L, bookedBy);

        // Assert
        assertThrows(PhoneAlreadyBookedException.class, result::block);
    }

    @Test
    public void returnPhone_withValidInput_shouldReturnReturnedPhone() {
        // Arrange
        samsungGalaxyS9.setBooked(true);
        samsungGalaxyS9.setBookedAt(LocalDateTime.now().minusMinutes(30));
        when(phoneRepository.findById(1L)).thenReturn(Optional.of(samsungGalaxyS9));
        when(phoneRepository.save(any(PhonePO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Mono<PhoneDTO> result = phoneService.returnPhone(1L);

        // Assert
        assertEquals(samsungGalaxyS9.getModel(), Objects.requireNonNull(result.block()).getModel());
        assertFalse(Objects.requireNonNull(result.block()).isBooked());
        assertNull(Objects.requireNonNull(result.block()).getBookedBy());
        assertNull(Objects.requireNonNull(result.block()).getBookedAt());
    }

    @Test
    public void returnPhone_withNonExistingPhone_shouldThrowPhoneNotFoundException() {
        // Arrange
        when(phoneRepository.findById(10L)).thenReturn(Optional.empty());

        // Act
        Mono<PhoneDTO> result = phoneService.returnPhone(10L);

        // Assert
        assertThrows(PhoneNotFoundException.class, result::block);
    }

    @Test
    public void returnPhone_withNotBookedPhone_shouldThrowPhoneNotBookedException() {
        // Arrange
        when(phoneRepository.findById(1L)).thenReturn(Optional.of(samsungGalaxyS9));

        // Act
        Mono<PhoneDTO> result = phoneService.returnPhone(1L);

        // Assert
        assertThrows(PhoneNotBookedException.class, result::block);
    }

    @Test
    public void getAllPhones_withNoPhones_shouldReturnEmptyFlux() {
        // Arrange
        List<PhonePO> phoneList = new ArrayList<>();
        when(phoneRepository.findAll()).thenReturn(phoneList);

        // Act
        Flux<PhoneDTO> result = phoneService.getAllPhones();

        // Assert
        assertEquals(0, result.count().block());
    }

    @Test
    public void getAllPhones_withMultiplePhones_shouldReturnFluxWithAllPhones() {
        // Arrange
        List<PhonePO> phoneList = new ArrayList<>();
        phoneList.add(samsungGalaxyS9);
        phoneList.add(samsungGalaxyS8);
        phoneList.add(motorolaNexus6);
        when(phoneRepository.findAll()).thenReturn(phoneList);

        // Act
        Flux<PhoneDTO> result = phoneService.getAllPhones();

        // Assert
        assertEquals(3, result.count().block());
    }
}

