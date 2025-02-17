package com.example.springboot.utils;

import com.example.springboot.entities.product.ProductPostRequestBody;

public class ProductPostRequestBodyCreator {
    public static ProductPostRequestBody createProductPostRequestBody() {
        return new ProductPostRequestBody(
                ProductCreator.createProductToBeSaved().getName(),
                ProductCreator.createProductToBeSaved().getValueProduct());
    }
}

