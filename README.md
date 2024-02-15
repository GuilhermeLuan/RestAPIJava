# Minha API

Bem-vindo à documentação da Minha API!

## Descrição

Esta API é responsável por um CRUD simples de produtos.

## Endpoints

Aqui estão os endpoints disponíveis nesta API:

### `GET /api/products`
Recupera uma lista de todos os produtos.

Exemplo de Resposta
```json
[
  {
    "idProduct": "ece548dc-13a1-487d-b468-13740b658599",
    "name": "Spotify",
    "value": 22.00,
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8080/products/ece548dc-13a1-487d-b468-13740b658599"
        }
    ]
},
    "idProduct": "2de63670-64b8-4333-9365-30b980cc3f16",
    "name": "Iphone 14 PRO",
    "value": 7900.00,
    "links": [
        {
            "rel": "self",
            "href": "http://localhost:8080/products/2de63670-64b8-4333-9365-30b980cc3f16"
        }
    ]
    }
]
```

### `GET /api/products/{UUID}`
Recupera um produto específico de acordo com seu identificado único (UUID).

Exemplo de resposta.
```json
{
  "idProduct": "2de63670-64b8-4333-9365-30b980cc3f16",
  "name": "Iphone 14 PRO",
  "value": 7900.00,
  "_links": {
    "Products List": {
      "href": "http://localhost:8080/products"
    }
  }
}
```

### `PUT /api/products/{UUID}`
Modifica um produto específico de acordo com seu identificado único (UUID).

Corpo sa solicitação.
```json
{
	"name": "Iphone 14 PRO Red",
	"value": 7900
}
```
Exemplo de resposta.
```json
{
	"idProduct": "2de63670-64b8-4333-9365-30b980cc3f16",
	"name": "Iphone 14 PRO Red",
	"value": 7900
}

```

### `POST /api/products`
Cria um produto no banco de dados.

Corpo da solicitação
```json
{
  "name": "Iphone 14",
  "value": 5000
}
```
Exemplo de resposta
```json
{
  "idProduct": "2de63670-64b8-4333-9365-30b980cc3f16",
  "name": "Iphone 14",
  "value": 5000
}
```
### `DELETE /api/products/{UUID}`
Deleta um produto específico de acordo com seu identificado único (UUID).

Exemplo de resposta.
```json
Product deleted successfully
```
