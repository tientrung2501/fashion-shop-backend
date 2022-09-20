package com.capstone.fashionshop.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Token {
    private String otp;
    private LocalDateTime exp;
}
