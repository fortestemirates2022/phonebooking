package com.example.phonebooking.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneRO {

    private Long id;

    private String brand;

    private String model;

    private boolean booked;

    private LocalDateTime bookedAt;

    private String bookedBy;

}

