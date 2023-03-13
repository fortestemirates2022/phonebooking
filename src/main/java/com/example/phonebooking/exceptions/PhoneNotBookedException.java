package com.example.phonebooking.exceptions;

public class PhoneNotBookedException extends RuntimeException {

    public PhoneNotBookedException() {
        super("Phone is not booked");
    }

}

