package com.example.springboot.mapper;

import com.example.springboot.entities.product.ProductPostRequestBody;
import com.example.springboot.entities.product.ProductPutRequestBody;
import com.example.springboot.entities.product.ProductModel;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class ProductMapper {
    public static final ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    public abstract ProductModel toProduct(ProductPostRequestBody productPostRequestBody);

    public abstract ProductModel toProduct(ProductPutRequestBody ProductPutRequestBody);

}
