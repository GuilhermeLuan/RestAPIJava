package com.example.springboot.integration;

import com.example.springboot.entities.product.ProductPostRequestBody;
import com.example.springboot.entities.product.ProductModel;
import com.example.springboot.entities.user.AuthenticationDTO;
import com.example.springboot.entities.user.LoginResponseDTO;
import com.example.springboot.entities.user.RegisterDTO;
import com.example.springboot.repositories.ProductRepository;
import com.example.springboot.util.ProductCreator;
import com.example.springboot.util.ProductPostRequestBodyCreator;
import com.example.springboot.util.UserPostRequestBodyCreator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("ittest")
//@Import(TestcontainersConfiguration.class)
class ProductControllerIT {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;

    private final String URL = "/v1/products";
    private String jwtToken;

    @BeforeEach()
    void setUpBearerToken() {
        RegisterDTO userRegisterBody = UserPostRequestBodyCreator.createUserWithRoleAdminPostRequestBody();
        AuthenticationDTO userLoginBody = new AuthenticationDTO(userRegisterBody.login(), userRegisterBody.password());

        testRestTemplate.postForEntity("/auth/register", userRegisterBody, RegisterDTO.class);

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity("/auth/login", userLoginBody, LoginResponseDTO.class);

        jwtToken = Objects.requireNonNull(login.getBody()).token();
    }

    @Test
    @DisplayName("saveProduct returns product when successful")
    void saveProduct_ReturnsProduct_WhenSuccessful() {
        ProductPostRequestBody productPostRequestBody = ProductPostRequestBodyCreator.createProductPostRequestBody();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        HttpEntity<ProductPostRequestBody> requestEntity =
                new HttpEntity<>(productPostRequestBody, headers);

        ResponseEntity<ProductModel> productResponseEntity =
                testRestTemplate.exchange(
                        URL,
                        HttpMethod.POST,
                        requestEntity,
                        ProductModel.class
                );

        Assertions.assertThat(productResponseEntity).isNotNull();
        Assertions.assertThat(productResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(productResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(productResponseEntity.getBody().getIdProduct()).isNotNull();
        Assertions.assertThat(productResponseEntity.getBody().getName()).isNotNull().isNotEmpty();
        Assertions.assertThat(productResponseEntity.getBody().getValueProduct()).isNotNull();
    }

    @Test
    @DisplayName("getAllProducts returns a list of all products when successful")
    void getAllProducts_ReturnsAllProducts_WhenSuccessful() {
        ProductModel productSaved = productRepository.save(ProductCreator.createProductToBeSaved());

        UUID expectedID = productSaved.getIdProduct();
        String expectedName = productSaved.getName();
        BigDecimal expectedValue = productSaved.getValueProduct();

        List<ProductModel> products = testRestTemplate.exchange(URL, HttpMethod.GET, null,
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
    void getAllProducts_ReturnsAnEmptyList_WhenThereIsNoProduct() {
        List<ProductModel> products = testRestTemplate.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProductModel>>() {
                }).getBody();

        Assertions.assertThat(products)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("getOneProductById returns product when successful")
    void getOneProductById_ReturnsProduct_WhenSuccessful() {
        ProductModel productSaved = productRepository.save(ProductCreator.createProductToBeSaved());

        UUID expectedID = productSaved.getIdProduct();
        String expectedName = productSaved.getName();
        BigDecimal expectedValue = productSaved.getValueProduct();

        ProductModel productModel = testRestTemplate.getForObject(URL + "/{id}", ProductModel.class, expectedID);

        Assertions.assertThat(productModel).isNotNull();

        Assertions.assertThat(productModel.getIdProduct()).isNotNull().isEqualTo(expectedID);
        Assertions.assertThat(productModel.getName()).isNotEmpty().isNotNull().isEqualTo(expectedName);
        Assertions.assertThat(productModel.getValueProduct()).isEqualTo(expectedValue);

    }

    @Test
    @DisplayName("getOneProduct returns bad request when unsuccessful")
    void getOneProductById_ReturnsBadRequest_WhenUnsuccessful() {


        ResponseEntity<ProductModel> responseEntity = testRestTemplate.getForEntity(URL + "/{id}", ProductModel.class, "1231");

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

        ResponseEntity<ProductModel> productModel = testRestTemplate.exchange(URL, HttpMethod.PUT, new HttpEntity<>(productSaved), ProductModel.class);

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

        ResponseEntity<Void> productModel = testRestTemplate.exchange(URL + "/{id}", HttpMethod.DELETE, null, Void.class, productSaved.getIdProduct());

        Assertions.assertThat(productModel).isNotNull();
        Assertions.assertThat(productModel.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deletes returns bad request when unsuccessful")
    void deleteProduct_ReturnsBadRequest_WhenUnsuccessful() {
        ResponseEntity<Void> productModel = testRestTemplate.exchange(URL + "/{id}", HttpMethod.DELETE, null, Void.class, "123");

        Assertions.assertThat(productModel).isNotNull();
        Assertions.assertThat(productModel.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}