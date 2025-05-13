package es.urjc.chamazon.dto;

public record UserDTO (
        Long id,
        String firstName,
        String surName,
        String userName,
        String password,
        String email,
        String phone,
        String address){
}
