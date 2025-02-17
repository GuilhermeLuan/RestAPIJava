CREATE TABLE tb_products
(
    id_product    UUID UNIQUE NOT NULL,
    name          VARCHAR(255),
    value_product DECIMAL,
    CONSTRAINT pk_tb_products PRIMARY KEY (id_product)
);

CREATE TABLE tb_users
(
    id TEXT PRIMARY KEY UNIQUE NOT NULL,
    login TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL
);