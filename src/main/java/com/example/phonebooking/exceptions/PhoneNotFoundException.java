package com.example.phonebooking.exceptions;

public class PhoneNotFoundException extends RuntimeException {

    public PhoneNotFoundException() {
        super("Phone not found");
    }

}

