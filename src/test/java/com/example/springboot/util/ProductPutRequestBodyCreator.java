package com.example.springboot.util;

import com.example.springboot.entities.product.ProductPutRequestBody;

public class ProductPutRequestBodyCreator {
    public static ProductPutRequestBody createPutRequestBody() {
        return new ProductPutRequestBody(
                ProductCreator.createValidUpdateProduct().getIdProduct().toString(),
                ProductCreator.createValidUpdateProduct().getName(),
                ProductCreator.createValidUpdateProduct().getValueProduct());
    }
}

