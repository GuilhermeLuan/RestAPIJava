package com.example.springboot.services;

import com.example.springboot.Exception.BadRequestException;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepositoryMock;

    private String name = "Iphone 15";

    @BeforeEach
    void setUp() {
        BDDMockito.when(productRepositoryMock.save(ArgumentMatchers.any(ProductModel.class)))
                .thenReturn(ProductCreator.createValidProduct());

        BDDMockito.when(productRepositoryMock.findAll())
                .thenReturn(List.of(ProductCreator.createValidProduct()));

        BDDMockito.when(productRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(Optional.of(ProductCreator.createValidProduct()));


        BDDMockito.doNothing().when(productRepositoryMock).deleteById(ArgumentMatchers.any(UUID.class));
    }

    @Test
    @DisplayName("saveProduct returns product when successful")
    void saveProduct_ReturnsProduct_WhenSuccessful() {
        ProductModel productModel = productService.insert(ProductPostRequestBodyCreator.createProductPostRequestBody());

        Assertions.assertThat(productModel).isNotNull().isEqualTo(ProductCreator.createValidProduct());
    }

    @Test
    @DisplayName("getAllProducts returns a list of all products when successful")
    void getAllProducts_ReturnsAllProducts_WhenSuccessful() {
        UUID expectedID = ProductCreator.createValidProduct().getIdProduct();
        String expectedName = ProductCreator.createValidProduct().getName();
        BigDecimal expectedValue = ProductCreator.createValidProduct().getValueProduct();
        List<ProductModel> products = productService.findAll(null);

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
        BDDMockito.when(productRepositoryMock.findAll())
                .thenReturn(Collections.emptyList());

        List<ProductModel> products = productService.findAll(name);

        Assertions.assertThat(products)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("getOneProductById returns product when successful")
    void getOneProductById_ReturnsProduct_WhenSuccessful() {
        UUID expectedId = ProductCreator.createValidProduct().getIdProduct();
        String expectedName = ProductCreator.createValidProduct().getName();
        BigDecimal expectedValue = ProductCreator.createValidProduct().getValueProduct();
        ProductModel productModel = productService.findByIdOrThrowBadRequestException(UUID.fromString("6f403211-288c-4188-867b-aa2ee769da8c"));

        Assertions.assertThat(productModel).isNotNull();

        Assertions.assertThat(productModel.getIdProduct()).isNotNull().isEqualTo(expectedId);
        Assertions.assertThat(productModel.getName()).isNotNull().isEqualTo(expectedName);
        Assertions.assertThat(productModel.getValueProduct()).isEqualTo(expectedValue);

    }

    @Test
    @DisplayName("getOneProduct returns bad request when unsuccessful")
    void getOneProductById_ReturnsBadRequest_WhenUnsuccessful() {
        BDDMockito.when(productRepositoryMock.findById(ArgumentMatchers.any()))
                .thenThrow(BadRequestException.class);

        Assertions.assertThatThrownBy(() ->
                        productService.findByIdOrThrowBadRequestException(UUID.fromString("6f403211-288c-4188-867b-aa2ee769da8c"))
                )
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("updates anime when successful")
    void updateProduct_UpdatesProduct_WhenSuccessful() {
        Assertions.assertThatCode(ProductPutRequestBodyCreator::createPutRequestBody).doesNotThrowAnyException();

        UUID expectedId = ProductCreator.createValidUpdateProduct().getIdProduct();
        String expectedName = ProductCreator.createValidUpdateProduct().getName();
        BigDecimal expectedValue = ProductCreator.createValidUpdateProduct().getValueProduct();

        ProductModel productModel = productService.update(ProductPutRequestBodyCreator.createPutRequestBody());

        Assertions.assertThat(productModel.getIdProduct()).isNotNull().isEqualTo(expectedId);
        Assertions.assertThat(productModel.getName()).isNotNull().isEqualTo(expectedName);
        Assertions.assertThat(productModel.getValueProduct()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("updates returns bad request when unsuccessful")
    void updateProduct_ReturnsBadRequest_WhenUnsuccessful() {
        BDDMockito.when(productRepositoryMock.save(ArgumentMatchers.any()))
                .thenThrow(BadRequestException.class);

        Assertions.assertThatThrownBy(() ->
                        productService.update(ProductPutRequestBodyCreator.createPutRequestBody())
                )
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("deletes removes product when successful")
    void deleteProduct_RemovesProduct_WhenSuccessful() {
        Assertions.assertThatCode(() -> productService.deleteById(UUID.fromString("6f403211-288c-4188-867b-aa2ee769da8c"))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("deletes returns bad request when unsuccessful")
    void deleteProduct_ReturnsBadRequest_WhenUnsuccessful() {

        BDDMockito.doThrow(BadRequestException.class).when(productRepositoryMock).deleteById(ArgumentMatchers.any(UUID.class));


        Assertions.assertThatThrownBy(() ->
                        productService.deleteById(UUID.fromString("6f403211-288c-4188-867b-aa2ee769da8c"))
                )
                .isInstanceOf(BadRequestException.class);
    }
}
