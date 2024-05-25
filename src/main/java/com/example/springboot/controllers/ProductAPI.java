package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto);

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
    ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id);

    @Operation(
            summary = "Atualiza um produto",
            description = "Atualiza os dados de um produto existente a partir do seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                         @RequestBody @Valid ProductRecordDto productRecordDto);

    @Operation(
            summary = "Deleta um produto",
            description = "Deleta um produto existente a partir do seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id);
}
