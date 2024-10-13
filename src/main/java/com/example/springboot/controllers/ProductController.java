package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductPostRequestBody;
import com.example.springboot.dtos.ProductPutRequestBody;
import com.example.springboot.models.ProductModel;
import com.example.springboot.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/products")
public class ProductController implements Serializable, ProductAPI {
    private final transient ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductPostRequestBody productPostRequestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.insert(productPostRequestBody));
    }


    @GetMapping
    public ResponseEntity<List<ProductModel>> findAll(@RequestParam(required = false) String productName) {
        List<ProductModel> list = productService.findAll(productName);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> findById(@PathVariable UUID id) {
        ProductModel product = productService.findByIdOrThrowBadRequestException(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @Override
    @PutMapping
    public ResponseEntity<ProductModel> updateProduct(@RequestBody @Valid ProductPutRequestBody productPutRequestBody) {
        return new ResponseEntity<>(productService.update(productPutRequestBody), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
