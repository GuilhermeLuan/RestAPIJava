package com.example.springboot.integration;

import com.example.springboot.config.RestAssuredConfig;
import com.example.springboot.config.TestcontainersConfiguration;
import com.example.springboot.entities.product.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import com.example.springboot.utils.FileUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("ittest")
@Import(TestcontainersConfiguration.class)
class ProductControllerIT {
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProductRepository productRepository;

    private final String URL_PRODUCTS = "/v1/products";
    private final String URL_REGISTER = "/auth/register";
    private final String URL_LOGIN = "/auth/login";
    private static String jwtToken = "";

    @Autowired
    @Qualifier(value = "requestSpecificationAdminUser")
    public RequestSpecification requestSpecificationAdminUser;

    @BeforeEach
    void setUp() {
        RestAssured.requestSpecification = requestSpecificationAdminUser;


        String registerRequest = fileUtils.readResourceFile("users/post-user-register-request-200.json");
        String loginRequest = fileUtils.readResourceFile("users/post-user-login-request-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .when()
                .post(URL_REGISTER)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all();

        var generateToken = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post(URL_LOGIN)
                .jsonPath().get("token");

        jwtToken = generateToken.toString();

    }

    @Test
    @DisplayName("POST /v1/products saves product when successful")
    void saveProduct_ReturnsProduct_WhenSuccessful() {
        String request = fileUtils.readResourceFile("products/request/post-product-201.json");
        String expectedResponse = fileUtils.readResourceFile("products/response/post-product-201.json");

        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(request)
                .when()
                .post(URL_PRODUCTS)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().body().asString();

        assertThatJson(response)
                .whenIgnoringPaths("idProduct")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET /v1/products returns a list of all products when successful")
    @Sql(value = "/sql/init_three_products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_products.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllProducts_ReturnsAllProducts_WhenSuccessful() {
        String expectedResponse = fileUtils.readResourceFile("products/response/get-products-200.json");

        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(URL_PRODUCTS)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().body().asString();

        assertThatJson(response)
                .when(Option.IGNORING_ARRAY_ORDER)
                .whenIgnoringPaths("[*].idProduct")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("GET /v1/products returns empty list when there is no product ")
    void getAllProducts_ReturnsAnEmptyList_WhenThereIsNoProduct() {
        String expectedResponse = fileUtils.readResourceFile("products/response/get-product-empty-200.json");

        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(URL_PRODUCTS)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().body().asString();

        assertThatJson(response)
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }


    @Test
    @DisplayName("GET /v1/products/{id} returns product when successful")
    @Sql(value = "/sql/init_three_products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_products.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getOneProductById_ReturnsProduct_WhenSuccessful() {
        var expectedProduct = productRepository.findAll().get(0);

        String expectedResponse = fileUtils.readResourceFile("products/response/get-one-product-200.json");

        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .pathParam("id", expectedProduct.getIdProduct())
                .get(URL_PRODUCTS + "/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().body().asString();

        assertThatJson(response)
                .when(Option.IGNORING_ARRAY_ORDER)
                .whenIgnoringPaths("[*].idProduct")
                .isEqualTo(expectedResponse);

    }

    @Test
    @DisplayName("GET /v1/products/{id} returns bad request when unsuccessful")
    void getOneProductById_ReturnsBadRequest_WhenUnsuccessful() {
        String expectedResponse = fileUtils.readResourceFile("products/response/get-one-product-400.json");

        var id = "946f70e7-34d4-403e-9421-76c223dc2505";

        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .pathParam("id", id)
                .get(URL_PRODUCTS + "/{id}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().body().asString();

        assertThatJson(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("PUT /v1/products returns OK when successful")
    @Sql(value = "/sql/init_three_products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_products.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateProduct_UpdatesProduct_WhenSuccessful() {
        String request = fileUtils.readResourceFile("products/request/put-product-201.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(request)
                .when()
                .put(URL_PRODUCTS)
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract().body().asString();
    }

    @Test
    @DisplayName("DELETE /v1/products removes product when successful")
    @Sql(value = "/sql/init_three_products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_products.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteProduct_RemovesProduct_WhenSuccessful() {
        List<ProductModel> productsList = productRepository.findAll();
        ProductModel productToDelete = productsList.get(0);

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .pathParam("id", productToDelete.getIdProduct())
                .when()
                .delete(URL_PRODUCTS + "/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all()
                .extract().body().asString();

        productsList = productRepository.findAll();
        Assertions.assertFalse(productsList.contains(productToDelete));
    }

    @Test
    @DisplayName("DELETE /v1/products returns bad request when unsuccessful")
    void deleteProduct_ReturnsBadRequest_WhenUnsuccessful() {
        String expectedResponse = fileUtils.readResourceFile("products/response/delete-product-400.json");

        var id = "946f70e7-34d4-403e-9421-76c223dc2505";

        String response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .pathParam("id", id)
                .delete(URL_PRODUCTS + "/{id}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().body().asString();

        assertThatJson(response)
                .isEqualTo(expectedResponse);
    }
}