package com.netone.random_draw.service;


import com.netone.random_draw.dto.ApiResponse;
import com.netone.random_draw.dto.AuthenticationResponse;
import com.netone.random_draw.dto.LoginRequest;
import com.netone.random_draw.dto.RegistrationRequest;
import com.netone.random_draw.model.User;

import java.util.List;

public interface UserService {
    AuthenticationResponse<String> authenticateUser(LoginRequest loginRequest);
    AuthenticationResponse<String> registerUser(RegistrationRequest registrationRequest) throws Exception;
    ApiResponse<String> deleteUser(Long id);
    ApiResponse<User> getUserByUsername(String username, boolean activeStatus);

    ApiResponse<List<User>> getAllActiveUsers();
    ApiResponse<User> updateUser(Long id, RegistrationRequest editUserRequest);
}
