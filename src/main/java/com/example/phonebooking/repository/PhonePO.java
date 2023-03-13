package com.example.phonebooking.repository;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "phone")
public class PhonePO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "booked")
    private boolean booked;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

    @Column(name = "booked_by")
    private String bookedBy;

}

