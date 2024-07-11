package com.example.springboot.services;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductModel> findAll(){
        return productRepository.findAll();
    }

    public ProductModel insert(ProductModel productModel) {
        return  productRepository.save(productModel);
    }

    public Optional<ProductModel> findById(UUID id){
        return productRepository.findById(id);
    }

    public void deleteById(UUID id){
        productRepository.deleteById(id);
    }

    public ProductModel update(UUID id, ProductModel productModel){
        ProductModel product = productRepository.getReferenceById(id);
        updateData(product, productModel);
        return productRepository.save(productModel);
    }
    public void updateData(ProductModel product, ProductModel obj){
        product.setName(obj.getName());
        product.setValue(obj.getValue());
    }

}
