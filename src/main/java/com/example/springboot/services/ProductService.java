package com.example.springboot.services;

import com.example.springboot.entities.product.ProductPostRequestBody;
import com.example.springboot.entities.product.ProductPutRequestBody;
import com.example.springboot.exception.BadRequestException;
import com.example.springboot.mapper.ProductMapper;
import com.example.springboot.entities.product.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final transient ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductModel> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public ProductModel insert(ProductPostRequestBody productPostRequestBody) {
        ProductModel productModel = ProductMapper.INSTANCE.toProduct(productPostRequestBody);
        return productRepository.save(productModel);
    }

    public ProductModel findByIdOrThrowBadRequestException(UUID id) {
        return productRepository.findById(id).
                orElseThrow(() -> new BadRequestException("Product not found"));
    }

    public void deleteById(UUID id) {
        productRepository.deleteById(findByIdOrThrowBadRequestException(id).getIdProduct());
    }

    public ProductModel update(ProductPutRequestBody productPutRequestBody) {
        ProductModel productSaved = findByIdOrThrowBadRequestException(UUID.fromString(productPutRequestBody.idProduct()));
        ProductModel productModel = ProductMapper.INSTANCE.toProduct(productPutRequestBody);
        productModel.setIdProduct(productSaved.getIdProduct());
        return productRepository.save(productModel);
    }
}
