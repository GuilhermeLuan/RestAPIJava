package com.example.springboot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductPutRequestBody(
        @NotBlank @NotEmpty
        String idProduct,
        @NotBlank @NotEmpty
        String name,
        @NotNull
        BigDecimal value) {
}
