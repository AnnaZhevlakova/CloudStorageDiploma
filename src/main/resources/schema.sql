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
values('test',  '$2a$10$gDTY0F.a6osJxkyeU/o.yunZNhyNxQlyAcH94AX01uurGAhPXHlXO') --test


MERGE INTO netology.users AS target
USING (VALUES
('test',  '$2a$10$gDTY0F.a6osJxkyeU/o.yunZNhyNxQlyAcH94AX01uurGAhPXHlXO')
) AS source(login,password)
ON target.login = source.login
WHEN NOT MATCHED THEN
  INSERT (login,password)
  VALUES (source.login, source.password)
  WHEN MATCHED THEN
      UPDATE SET password = source.password;


CREATE UNIQUE INDEX IF NOT EXISTS uq_file_info_user_id_file_name ON netology.file_info(user_id, file_name);
