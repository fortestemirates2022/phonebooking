package com.example.phonebooking.exceptions;

public class PhoneAlreadyBookedException extends RuntimeException {

    public PhoneAlreadyBookedException() {
        super("Phone is already booked");
    }

}

