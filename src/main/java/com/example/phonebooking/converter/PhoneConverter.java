package com.example.phonebooking.converter;

import com.example.phonebooking.controller.PhoneRO;
import com.example.phonebooking.repository.PhonePO;
import com.example.phonebooking.service.PhoneDTO;

import java.util.ArrayList;
import java.util.List;

public class PhoneConverter {
    private PhoneConverter(){
        super();
    }
    public static PhoneDTO toDTO(PhonePO source) {
        if(source ==null){
            return null;
        }
        return PhoneDTO.builder()
                .id(source.getId())
                .brand(source.getBrand())
                .model(source.getModel())
                .booked(source.isBooked())
                .bookedAt(source.getBookedAt())
                .bookedBy(source.getBookedBy())
                .build();
    }
    public static List<PhoneDTO> toDTOs(List<PhonePO> source) {
        List<PhoneDTO> target = new ArrayList<>();
        if (source != null) {
            source.forEach(s -> target.add(PhoneDTO.builder()
                    .id(s.getId())
                    .brand(s.getBrand())
                    .model(s.getModel())
                    .booked(s.isBooked())
                    .bookedAt(s.getBookedAt())
                    .bookedBy(s.getBookedBy())
                    .build()));

        }
        return target;
    }

    public static PhonePO toPO(PhoneDTO source) {
        if(source ==null){
            return null;
        }
        return PhonePO.builder()
                .id(source.getId())
                .brand(source.getBrand())
                .model(source.getModel())
                .booked(source.isBooked())
                .bookedAt(source.getBookedAt())
                .bookedBy(source.getBookedBy())
                .build();
    }
    public static PhoneRO toRO(PhoneDTO source) {
        if(source ==null){
            return null;
        }
        return PhoneRO.builder()
                .id(source.getId())
                .brand(source.getBrand())
                .model(source.getModel())
                .booked(source.isBooked())
                .bookedAt(source.getBookedAt())
                .bookedBy(source.getBookedBy())
                .build();
    }

}
