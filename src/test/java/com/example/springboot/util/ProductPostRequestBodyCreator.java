package com.example.springboot.util;

import com.example.springboot.dtos.ProductPostRequestBody;

public class ProductPostRequestBodyCreator {
    public static ProductPostRequestBody createProductPostRequestBody() {
        return new ProductPostRequestBody(
                ProductCreator.createProductToBeSaved().getName(),
                ProductCreator.createProductToBeSaved().getValue());
    }
}

