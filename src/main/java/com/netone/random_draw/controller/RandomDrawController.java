package com.netone.random_draw.controller;

import com.netone.random_draw.dto.ApiResponse;
import com.netone.random_draw.exception.ResourceNotFoundException;
import com.netone.random_draw.exception.ValidationException;
import com.netone.random_draw.model.RandomDraw;
import com.netone.random_draw.service.RandomDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class RandomDrawController {
    @Autowired
    private RandomDrawService randomDrawService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<List<RandomDraw>>> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<RandomDraw> savedRegisters = randomDrawService.saveRegistersFromExcel(file);
            return ResponseEntity.ok(new ApiResponse<>(true, "Excel file uploaded successfully!", savedRegisters));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error uploading Excel file: " + e.getMessage(), null));
        }
    }

    @GetMapping("/registers")
    public ResponseEntity<ApiResponse<List<RandomDraw>>> getAllRegisters() {
        try {
            List<RandomDraw> registers = randomDrawService.getAllRegisters();
            return ResponseEntity.ok(new ApiResponse<>(true, "Registers retrieved successfully", registers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error retrieving registers: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RandomDraw>> getRegisterById(@PathVariable Long id) {
        try {
            RandomDraw register = randomDrawService.getRegisterById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Register retrieved successfully", register));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred: " + e.getMessage(), null));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RandomDraw>>
    updateRegister(@PathVariable Long id, @RequestBody RandomDraw updatedRegister) {
        try {
            RandomDraw updated = randomDrawService.updateRegister(id, updatedRegister);
            return ResponseEntity.ok(new ApiResponse<>(true, "Register updated successfully", updated));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred: " + e.getMessage(), null));
        }
    }

}