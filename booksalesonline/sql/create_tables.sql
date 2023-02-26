DROP DATABASE IF EXISTS bookSalesOnline;

CREATE DATABASE booksalesonline;

\c booksalesonline

CREATE TABLE country (
    id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL,
    nationality varchar(50) NOT NULL
);

CREATE TABLE author (
    id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL,
    resume varchar(510) NOT NULL,
    id_country INTEGER NOT NULL REFERENCES country(id)
);

INSERT INTO public.country(name, nationality) VALUES ('Argentina', 'Argentina');
INSERT INTO public.country(name, nationality) VALUES ('Brasil', 'Brasileira');
INSERT INTO public.country(name, nationality) VALUES ('Chile', 'Chilena');
INSERT INTO public.country(name, nationality) VALUES ('México', 'Mexicana');
INSERT INTO public.country(name, nationality) VALUES ('Estados Unidos', 'Americana');
INSERT INTO public.country(name, nationality) VALUES ('Canadá', 'Canadense');
INSERT INTO public.country(name, nationality) VALUES ('Índia', 'Indiana');
INSERT INTO public.country(name, nationality) VALUES ('China', 'Chinesa');
INSERT INTO public.country(name, nationality) VALUES ('Japão', 'Japonesa');
INSERT INTO public.country(name, nationality) VALUES ('Coréia do Sul', 'Sul-Coreana');
INSERT INTO public.country(name, nationality) VALUES ('Egito', 'Egípcia');
INSERT INTO public.country(name, nationality) VALUES ('Marrocos', 'Marroquina');
INSERT INTO public.country(name, nationality) VALUES ('Africa do Sul', 'Sul-Africana');
INSERT INTO public.country(name, nationality) VALUES ('Alemanha', 'Alemã');
INSERT INTO public.country(name, nationality) VALUES ('França', 'Francesa');
INSERT INTO public.country(name, nationality) VALUES ('Espanha', 'Espanhola');
INSERT INTO public.country(name, nationality) VALUES ('Inglaterra', 'Inglesa');
INSERT INTO public.country(name, nationality) VALUES ('Portugal', 'Portuguesa');
INSERT INTO public.country(name, nationality) VALUES ('Austrália', 'Australiana');
INSERT INTO public.country(name, nationality) VALUES ('Nova Zelândia', 'Neo-Zelândes');

INSERT INTO public.author(name, resume, id_country) VALUES ('J.R.R Tolkien', '...', 17);
INSERT INTO public.author(name, resume, id_country) VALUES ('Pramod J. Sadalage', '...', 7);
INSERT INTO public.author(name, resume, id_country) VALUES ('William Shakespeare', '...', 17);
INSERT INTO public.author(name, resume, id_country) VALUES ('Akira Toryiama', '...', 9);
INSERT INTO public.author   (name, resume, id_country) VALUES ('Cecília Meireles', '...', 2);