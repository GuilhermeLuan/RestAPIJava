package com.example.springboot.controllers;

import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serial;
import java.io.Serializable;

@RestController
public class ProductController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Autowired
    ProductRepository productRepository;



}
