package com.example.springboot.integration;

import com.example.springboot.dtos.ProductPostRequestBody;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import com.example.springboot.util.ProductCreator;
import com.example.springboot.util.ProductPostRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class ProductControllerIT {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;

    private String url = "/v1/products";

    @Test
    @DisplayName("saveProduct returns product when successful")
    void saveProduct_ReturnsProduct_WhenSuccessful() {
        ProductPostRequestBody productPostRequestBody = ProductPostRequestBodyCreator.createProductPostRequestBody();
        ResponseEntity<ProductModel> productResponseEntity = testRestTemplate.postForEntity(url, productPostRequestBody, ProductModel.class);

        Assertions.assertThat(productResponseEntity).isNotNull();
        Assertions.assertThat(productResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(productResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(productResponseEntity.getBody().getIdProduct()).isNotNull();
        Assertions.assertThat(productResponseEntity.getBody().getName()).isNotNull().isNotEmpty();
        Assertions.assertThat(productResponseEntity.getBody().getValueProduct()).isNotNull();
    }

    @Test
    @DisplayName("getAllProducts returns a list of all products when successful")
    void findAllProducts_ReturnsAll_WhenSuccessful() {
        ProductModel productSaved = productRepository.save(ProductCreator.createProductToBeSaved());

        UUID expectedID = productSaved.getIdProduct();
        String expectedName = productSaved.getName();
        BigDecimal expectedValue = productSaved.getValueProduct();

        List<ProductModel> products = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProductModel>>() {
                }).getBody();

        Assertions.assertThat(products)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(products.get(0).getIdProduct()).isEqualTo(expectedID);
        Assertions.assertThat(products.get(0).getName()).isEqualTo(expectedName);
        Assertions.assertThat(products.get(0).getValueProduct()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("getAllProducts returns empty list when there is no product ")
    void findAll_ReturnsAnEmptyList_WhenThereIsNoProduct() {
        List<ProductModel> products = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProductModel>>() {
                }).getBody();

        Assertions.assertThat(products)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("getOneProductById returns product when successful")
    void findById_WhenSuccessful() {
        ProductModel productSaved = productRepository.save(ProductCreator.createProductToBeSaved());

        UUID expectedID = productSaved.getIdProduct();
        String expectedName = productSaved.getName();
        BigDecimal expectedValue = productSaved.getValueProduct();

        ProductModel productModel = testRestTemplate.getForObject(url + "/{id}", ProductModel.class, expectedID);

        Assertions.assertThat(productModel).isNotNull();

        Assertions.assertThat(productModel.getIdProduct()).isNotNull().isEqualTo(expectedID);
        Assertions.assertThat(productModel.getName()).isNotEmpty().isNotNull().isEqualTo(expectedName);
        Assertions.assertThat(productModel.getValueProduct()).isEqualTo(expectedValue);

    }

    @Test
    @DisplayName("getOneProduct returns bad request when unsuccessful")
    void findByIdById_ReturnsBadRequest_WhenUnsuccessful() {


        ResponseEntity<ProductModel> responseEntity = testRestTemplate.getForEntity(url + "/{id}", ProductModel.class, "1231");

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("updates anime when successful")
    void updateProduct_UpdatesProduct_WhenSuccessful() {
        ProductModel productSaved = productRepository.save(ProductCreator.createProductToBeSaved());

        UUID expectedID = productSaved.getIdProduct();
        String savedName = "Test";
        productSaved.setName(savedName);
        BigDecimal expectedValue = productSaved.getValueProduct();

        ResponseEntity<ProductModel> productModel = testRestTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(productSaved), ProductModel.class);

        Assertions.assertThat(productModel).isNotNull();
        Assertions.assertThat(productModel.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(productModel.getBody().getIdProduct()).isNotNull().isEqualTo(expectedID);
        Assertions.assertThat(productModel.getBody().getName()).isNotEmpty().isNotNull().isEqualTo(savedName);
        Assertions.assertThat(productModel.getBody().getValueProduct()).isEqualTo(expectedValue);

    }


    @Test
    @DisplayName("deletes removes product when successful")
    void deleteProduct_RemovesProduct_WhenSuccessful() {
        ProductModel productSaved = productRepository.save(ProductCreator.createProductToBeSaved());

        ResponseEntity<Void> productModel = testRestTemplate.exchange(url + "/{id}", HttpMethod.DELETE, null, Void.class, productSaved.getIdProduct());

        Assertions.assertThat(productModel).isNotNull();
        Assertions.assertThat(productModel.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deletes returns bad request when unsuccessful")
    void deleteProduct_ReturnsBadRequest_WhenUnsuccessful() {
        ResponseEntity<Void> productModel = testRestTemplate.exchange(url + "/{id}", HttpMethod.DELETE, null, Void.class, "123");

        Assertions.assertThat(productModel).isNotNull();
        Assertions.assertThat(productModel.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


}