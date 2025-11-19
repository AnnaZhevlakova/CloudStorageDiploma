create schema IF NOT exists netology;


CREATE TABLE IF NOT exists netology.users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT exists netology.file_info (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    file_name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    file_data bytea NOT NULL,
    upload_date TIMESTAMP,
    file_size int8 NULL,
    hash varchar(64) NULL,
    FOREIGN KEY (user_id) REFERENCES netology.users(id)
);

insert into netology.users(login,password)
values('test2',  'test')