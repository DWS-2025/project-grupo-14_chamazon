package es.urjc.chamazon.dto;

public record UserIndividualDTO (
        String firstName,
        String surName,
        String userName,
        String email,
        String phone,
        String address){
}
