package com.example.j200_labshvidkiy.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
    private Integer addressid;
    private String ip;
    private String mac;
    private String model;
    private String address;
    private Integer clientid;
}
