package com.example.j200_labshvidkiy.dto;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ClientDto {
    private Integer clientId;
    private String clientName;
    private String clientType;
    private String added;
}
