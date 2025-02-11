package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductPostRequestBody;
import com.example.springboot.dtos.ProductPutRequestBody;
import com.example.springboot.models.product.ProductModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "Product", description = "API de produtos")
public interface ProductAPI {

    @Operation(
            summary = "Registra um produto",
            description = "Registra uma entidade produto com seus dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductPostRequestBody productPostRequestBody);

    @Operation(
            summary = "Busca todos os produtos",
            description = "Busca todas as entidades de produtos e seus dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<List<ProductModel>> getAllProducts();

    @Operation(
            summary = "Busca um produto",
            description = "Busca um produto a partir do seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<ProductModel> getOneProduct(@PathVariable(value = "id") UUID id);

    @Operation(
            summary = "Atualiza um produto",
            description = "Atualiza os dados de um produto existente a partir do seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<ProductModel> updateProduct(@RequestBody @Valid ProductPutRequestBody productPutRequestBody);

    @Operation(
            summary = "Deleta um produto",
            description = "Deleta um produto existente a partir do seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<Void> deleteProduct(@PathVariable(value = "id") UUID id);
}
