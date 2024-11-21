package com.netone.random_draw.dto;


public record RegistrationRequest(String firstName,

                                  String lastName,
                                  String email,
                                  String username,

                                  String role
                                 ) {
}
