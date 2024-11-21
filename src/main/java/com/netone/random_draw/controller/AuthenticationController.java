package com.netone.random_draw.controller;

import com.netone.random_draw.dto.ApiResponse;
import com.netone.random_draw.dto.AuthenticationResponse;
import com.netone.random_draw.dto.LoginRequest;
import com.netone.random_draw.dto.RegistrationRequest;
import com.netone.random_draw.model.User;
import com.netone.random_draw.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse<String>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse<String>> registerUser(@RequestBody RegistrationRequest registrationRequest) throws Exception {
        return ResponseEntity.ok(userService.registerUser(registrationRequest));
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable String username) {
        ApiResponse<User> apiResponse = userService.getUserByUsername(username, true);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllActiveUsers() {
        ApiResponse<List<User>> apiResponse = userService.getAllActiveUsers();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }


    @PutMapping("/user/update/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long id,
            @RequestBody RegistrationRequest updateRequest) {
        ApiResponse<User> apiResponse = userService.updateUser(id, updateRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}