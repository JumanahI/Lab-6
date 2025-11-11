package com.example.Employee_Management_System.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {

    @NotEmpty(message = "ID cannot be empty")
    @Size(min = 2, message = "Id must be longer then 2 characters")
    private String ID;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 4, message = "Name must be longer then 4 characters")
    @Pattern(regexp = "^(?!.*\\d).{5,}$",
            message = "Name must be more than 4 characters and contain no numbers")
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^05\\d{8}$",
            message = "Phone number must start with 05 and be exactly 10 digits")
    private String phoneNumber;

    @NotNull(message = "Age cannot be null")
    @Min(value = 25, message = "Age must be at least 25")
    private int age;

    @NotEmpty(message = "Position cannot be empty")
    @Pattern(regexp = "^(supervisor|coordinator)$",
            message = "Position must be either 'supervisor',or 'coordinator'")
    private String position;

    @AssertFalse
    private boolean onLeave;

    @NotNull(message = "hire date cannot be empty")
    @PastOrPresent(message = "Hire date must past or present")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    @NotNull(message = "Annual leave cannot be null")
    @PositiveOrZero(message = "Annual leave must a positive number")
    private int annualLeave;

}





