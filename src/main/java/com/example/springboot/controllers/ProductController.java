package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductPutRequestBody;
import com.example.springboot.dtos.ProductPostRequestBody;
import com.example.springboot.models.ProductModel;
import com.example.springboot.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@RestController
public class ProductController implements Serializable, ProductAPI {
    private final transient ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductPostRequestBody productPostRequestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.insert(productPostRequestBody));
    }


    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> list = productService.findAll();
        for (ProductModel productModel : list) {
            UUID id = productModel.getIdProduct();
            productModel.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductModel> getOneProduct(@PathVariable UUID id) {
        ProductModel product = productService.findByIdOrThrowBadRequestException(id);
        product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @Override
    @PutMapping("/products")
    public ResponseEntity<ProductModel> updateProduct(@RequestBody @Valid ProductPutRequestBody productPutRequestBody) {
        return new ResponseEntity<>(productService.update(productPutRequestBody), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
