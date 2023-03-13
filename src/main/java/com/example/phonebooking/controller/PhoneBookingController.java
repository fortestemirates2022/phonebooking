package com.example.phonebooking.controller;

import com.example.phonebooking.converter.PhoneConverter;
import com.example.phonebooking.exceptions.PhoneNotFoundException;
import com.example.phonebooking.exceptions.PhoneAlreadyBookedException;
import com.example.phonebooking.exceptions.PhoneNotBookedException;
import com.example.phonebooking.service.PhoneService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/phones")
@Api(tags = "Phone Controller")
public class PhoneBookingController {

    private final PhoneService phoneService;

    public PhoneBookingController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @PostMapping("/{id}/book")
    @ApiOperation("Book a phone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Phone successfully booked"),
            @ApiResponse(code = 400, message = "Invalid input data"),
            @ApiResponse(code = 404, message = "Phone not found"),
            @ApiResponse(code = 409, message = "Phone already booked"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Mono<ResponseEntity<PhoneRO>> bookPhone(
            @ApiParam(value = "ID of the phone to book", example = "1") @PathVariable Long id,
            @ApiParam(value = "Name of the person booking the phone", example = "John Doe") @RequestParam String bookedBy) {
        return phoneService.bookPhone(id, bookedBy)
                .map(phone -> ResponseEntity.ok().body(PhoneConverter.toRO(phone)))
                .onErrorResume(PhoneNotFoundException.class, e -> Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(PhoneAlreadyBookedException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()))
                .onErrorResume(Exception.class, e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @PostMapping("/{id}/return")
    @ApiOperation("Return a phone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Phone successfully returned"),
            @ApiResponse(code = 400, message = "Invalid input data"),
            @ApiResponse(code = 404, message = "Phone not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Mono<ResponseEntity<PhoneRO>> returnPhone(
            @ApiParam(value = "ID of the phone to return", example = "1") @PathVariable Long id) {
        return phoneService.returnPhone(id)
                .map(phone -> ResponseEntity.ok().body(PhoneConverter.toRO(phone)))
                .onErrorResume(PhoneNotFoundException.class, e -> Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(PhoneNotBookedException.class, e -> Mono.just(ResponseEntity.badRequest().build()))
                .onErrorResume(Exception.class, e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @GetMapping
    @ApiOperation("Get all phones")
    @ApiResponse(code = 200, message = "Successfully retrieved phones")
    public ResponseEntity<Flux<PhoneRO>> getAllPhones() {
        return ResponseEntity.ok().body(phoneService.getAllPhones().map(PhoneConverter::toRO));
    }

}

