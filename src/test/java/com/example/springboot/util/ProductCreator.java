package com.example.springboot.util;

import com.example.springboot.models.ProductModel;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductCreator {
    public static ProductModel createProductToBeSaved() {
        return ProductModel.builder()
                .name("Iphone 15")
                .valueProduct(BigDecimal.valueOf(5400.77))
                .build();
    }

    public static ProductModel createValidProduct() {
        return ProductModel.builder()
                .idProduct(UUID.fromString("dd0e2cf3-d1d3-4bf5-a9a6-d0ef691c8892"))
                .name("Iphone 15")
                .valueProduct(BigDecimal.valueOf(5000.99))
                .build();
    }

    public static ProductModel createValidUpdateProduct() {
        return ProductModel.builder()
                .idProduct(UUID.fromString("dd0e2cf3-d1d3-4bf5-a9a6-d0ef691c8892"))
                .name("Iphone 15")
                .valueProduct(BigDecimal.valueOf(5000.99))
                .build();
    }
}
