package com.example.springboot.controllers;

import com.example.springboot.Exception.BadRequestException;
import com.example.springboot.dtos.ProductPostRequestBody;
import com.example.springboot.dtos.ProductPutRequestBody;
import com.example.springboot.models.ProductModel;
import com.example.springboot.services.ProductService;
import com.example.springboot.util.ProductCreator;
import com.example.springboot.util.ProductPostRequestBodyCreator;
import com.example.springboot.util.ProductPutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
class ProductControllerTest {
    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductService productServiceMock;

    @BeforeEach
    void setUp() {
        BDDMockito.when(productServiceMock.insert(ArgumentMatchers.any(ProductPostRequestBody.class)))
                .thenReturn(ProductCreator.createValidProduct());

        BDDMockito.when(productServiceMock.findAll())
                .thenReturn(List.of(ProductCreator.createValidProduct()));

        BDDMockito.when(productServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any(UUID.class)))
                .thenReturn(ProductCreator.createValidProduct());


        BDDMockito.when(productServiceMock.update(ArgumentMatchers.any(ProductPutRequestBody.class)))
                .thenReturn(ProductCreator.createValidUpdateProduct());

        BDDMockito.doNothing().when(productServiceMock).deleteById(ArgumentMatchers.any(UUID.class));
    }

    @Test
    @DisplayName("save returns product when successful")
    void save_ReturnsProduct_WhenSuccessful() {
        ProductModel productModel = productController.saveProduct(ProductPostRequestBodyCreator.createProductPostRequestBody()).getBody();

        Assertions.assertThat(productModel).isNotNull().isEqualTo(ProductCreator.createValidProduct());
    }

    @Test
    @DisplayName("getAllProducts returns a list of all products when successful")
    void getAllProducts_ReturnsAllProducts_WhenSuccessful() {
        UUID expectedID = ProductCreator.createValidProduct().getIdProduct();
        String expectedName = ProductCreator.createValidProduct().getName();
        BigDecimal expectedValue = ProductCreator.createValidProduct().getValue();
        List<ProductModel> products = productController.getAllProducts().getBody();

        Assertions.assertThat(products)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(products.get(0).getIdProduct()).isEqualTo(expectedID);
        Assertions.assertThat(products.get(0).getName()).isEqualTo(expectedName);
        Assertions.assertThat(products.get(0).getValue()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("getAllProducts returns empty list when there is any product ")
    void getAllProducts_ReturnsAnEmptyList_WhenThereIsAnyProduct() {
        BDDMockito.when(productServiceMock.findAll())
                .thenReturn(Collections.emptyList());

        List<ProductModel> products = productController.getAllProducts().getBody();

        Assertions.assertThat(products)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("getOneProduct returns product when successful")
    void getOneProductById_ReturnsProduct_WhenSuccessful() {
        UUID expectedId = ProductCreator.createValidProduct().getIdProduct();
        String expectedName = ProductCreator.createValidProduct().getName();
        BigDecimal expectedValue = ProductCreator.createValidProduct().getValue();
        ProductModel productModel = productController.getOneProduct(UUID.fromString("6f403211-288c-4188-867b-aa2ee769da8c")).getBody();

        Assertions.assertThat(productModel).isNotNull();

        Assertions.assertThat(productModel.getIdProduct()).isNotNull().isEqualTo(expectedId);
        Assertions.assertThat(productModel.getName()).isNotNull().isEqualTo(expectedName);
        Assertions.assertThat(productModel.getValue()).isEqualTo(expectedValue);

    }

    @Test
    @DisplayName("getOneProduct returns bad request when unsuccessful")
    void getOneProductById_ReturnsBadRequest_WhenUnsuccessful() {
        BDDMockito.when(productServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any()))
                .thenThrow(BadRequestException.class);

        Assertions.assertThatThrownBy(() ->
                        productController.getOneProduct(UUID.fromString("6f403211-288c-4188-867b-aa2ee769da8c"))
                )
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("updates anime when successful")
    void updateProduct_UpdatesProduct_WhenSuccessful() {
        Assertions.assertThatCode(ProductPutRequestBodyCreator::createPutRequestBody).doesNotThrowAnyException();

        UUID expectedId = ProductCreator.createValidUpdateProduct().getIdProduct();
        String expectedName = ProductCreator.createValidUpdateProduct().getName();
        BigDecimal expectedValue = ProductCreator.createValidUpdateProduct().getValue();

        ProductModel productModel = productController.updateProduct(ProductPutRequestBodyCreator.createPutRequestBody()).getBody();

        Assertions.assertThat(productModel.getIdProduct()).isNotNull().isEqualTo(expectedId);
        Assertions.assertThat(productModel.getName()).isNotNull().isEqualTo(expectedName);
        Assertions.assertThat(productModel.getValue()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("updates returns bad request when unsuccessful")
    void updateProduct_ReturnsBadRequest_WhenUnsuccessful() {
        BDDMockito.when(productServiceMock.update(ArgumentMatchers.any()))
                .thenThrow(BadRequestException.class);

        Assertions.assertThatThrownBy(() ->
                        productController.updateProduct(ProductPutRequestBodyCreator.createPutRequestBody())
                )
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("deletes removes product when successful")
    void deleteProduct_RemovesProduct_WhenSuccessful() {
        Assertions.assertThatCode(() -> productController.deleteProduct(UUID.fromString("6f403211-288c-4188-867b-aa2ee769da8c"))).doesNotThrowAnyException();

        ResponseEntity<Void> entity = productController.deleteProduct(UUID.fromString("6f403211-288c-4188-867b-aa2ee769da8c"));
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deletes returns bad request when unsuccessful")
    void deleteProduct_ReturnsBadRequest_WhenUnsuccessful() {

        BDDMockito.doThrow(BadRequestException.class).when(productServiceMock).deleteById(ArgumentMatchers.any(UUID.class));


        Assertions.assertThatThrownBy(() ->
                        productController.deleteProduct(UUID.fromString("6f403211-288c-4188-867b-aa2ee769da8c"))
                )
                .isInstanceOf(BadRequestException.class);
    }
}