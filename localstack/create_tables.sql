DROP DATABASE IF EXISTS bookSalesOnline;

CREATE DATABASE booksalesonline;

\c booksalesonline

CREATE TABLE language(
    "id" SERIAL PRIMARY KEY, 
    "name" VARCHAR(50) NOT NULL
);
CREATE TABLE country(
    "id" SERIAL PRIMARY KEY, 
    "name" VARCHAR(50) NOT NULL, 
    "gentilic" VARCHAR(255) NOT NULL
);
CREATE TABLE subject(
    "id" SERIAL PRIMARY KEY, 
    "name" VARCHAR(50) NOT NULL, 
    "description" VARCHAR(510) NOT NULL
);
CREATE TABLE publication_type(
    "id" SERIAL PRIMARY KEY, 
    "name" VARCHAR(50) NOT NULL, 
    "description" VARCHAR(510) NOT NULL
);
CREATE TABLE author(
    "id" SERIAL PRIMARY KEY, 
    "name" VARCHAR(75) NOT NULL, 
    "resume" VARCHAR(510) NOT NULL, 
    "id_country" INTEGER NOT NULL REFERENCES country(id)
);
CREATE TABLE publisher(
    "id" SERIAL PRIMARY KEY, 
    "name" VARCHAR(50) NOT NULL, 
    "history" VARCHAR(510) NOT NULL, 
    "id_country" INTEGER NOT NULL REFERENCES country(id)
);
CREATE TABLE publication(
    "id" SERIAL PRIMARY KEY, 
    "isbn10" VARCHAR(10) NOT NULL, 
    "isbn13" VARCHAR(14) NOT NULL, 
    "pages" INTEGER NOT NULL, 
    "material" VARCHAR(50) NOT NULL,
    "name" VARCHAR(50) NOT NULL,
    "summary" VARCHAR(510) NOT NULL, 
    "id_language" INTEGER NOT NULL REFERENCES language(id),
    "id_publisher" INTEGER NOT NULL REFERENCES publisher(id),
    "id_publication_type" INTEGER NOT NULL REFERENCES publication_type(id)
);
CREATE TABLE publication_author(
    "id_publication" INTEGER NOT NULL REFERENCES publication(id),
    "id_author" INTEGER NOT NULL REFERENCES author(id)
);
CREATE TABLE publication_subject(
    "id_publication" INTEGER NOT NULL REFERENCES publication(id),
    "id_subject" INTEGER NOT NULL REFERENCES subject(id)
);
CREATE TABLE image(
    "id" SERIAL PRIMARY KEY, 
    "name" VARCHAR(50) NOT NULL, 
    "url" VARCHAR(510) NOT NULL,
    "id_publication" INTEGER NOT NULL REFERENCES publication(id)
);

INSERT INTO public.language("name") VALUES ('Português (pt-br)');
INSERT INTO public.language("name") VALUES ('Português (pt-pt)');
INSERT INTO public.language("name") VALUES ('Inglês (USA)') ;
INSERT INTO public.language("name") VALUES ('Inglês (UK)');
INSERT INTO public.language("name") VALUES ('Japonês');

INSERT INTO public.country("name", "gentilic") VALUES ('Inglaterra', 'Inglesa');
INSERT INTO public.country("name", "gentilic") VALUES ('Brasil', 'Brasileira');
INSERT INTO public.country("name", "gentilic") VALUES ('Estados Unidos', 'Americana');
INSERT INTO public.country("name", "gentilic") VALUES ('Portugal', 'Português');
INSERT INTO public.country("name", "gentilic") VALUES ('Japão', 'Japonesa');
INSERT INTO public.country("name", "gentilic") VALUES ('Índia', 'Indiana');

INSERT INTO public.subject("name", "description") VALUES ('Fantasia', '');
INSERT INTO public.subject("name", "description") VALUES ('Aventura', '');
INSERT INTO public.subject("name", "description") VALUES ('Policial', '');
INSERT INTO public.subject("name", "description") VALUES ('Romance', '');
INSERT INTO public.subject("name", "description") VALUES ('Terror', '');
INSERT INTO public.subject("name", "description") VALUES ('Ação', '');

INSERT INTO public.publication_type("name", "description") VALUES ('Livro físico', '');
INSERT INTO public.publication_type("name", "description") VALUES ('Livro digital', '');
INSERT INTO public.publication_type("name", "description") VALUES ('Mangá', '');

INSERT INTO public.author("name", "resume", "id_country") VALUES ('J.R.R Tolkien', '', 1);
INSERT INTO public.author("name", "resume", "id_country") VALUES ('Akira Toryiama', '', 5);

INSERT INTO public.publisher("name", "history", "id_country") VALUES ('HarperCollins - Brasil', '', 2);
INSERT INTO public.publisher("name", "history", "id_country") VALUES ('Addison-Wesley Professional', '', 1);
INSERT INTO public.publisher("name", "history", "id_country") VALUES ('Companhia das Letras', '', 2);

INSERT INTO public.publication(
    "isbn10", "isbn13", "pages", "material", "name", "summary", 
    "id_language", "id_publisher", "id_publication_type"
) VALUES (
    '8595084750', '978-8595084759', 576, 'Capa dura', 'O Senhor dos Anéis: A Sociedade do Anel', 
    'A Sociedade do Anel começa no Condado, a região rural do oeste da Terra-média onde vivem os diminutos e pacatos hobbits. Bilbo Bolseiro, um dos raros aventureiros desse povo, cujas peripécias foram contadas em O Hobbit, resolve ir embora do Condado e deixa sua considerável herança nas mãos de seu jovem parente Frodo.', 
    1, 1, 1
);
INSERT INTO public.publication_author("id_publication", "id_author") VALUES (1, 1);
INSERT INTO public.publication_subject("id_publication", "id_subject") VALUES (1, 1);
INSERT INTO public.publication_subject("id_publication", "id_subject") VALUES (1, 2);

SELECT 
    pub.isbn10, 
    pub.isbn13,
    pub.pages,
    pub.material,
    pub.name,
    --pub.summary,
    publ.name "publisher",
    lang.name "language",
    pubType.name "publication_type"
FROM public.publication pub JOIN public.publisher publ
ON pub.id_publisher = publ.id JOIN public.language lang
ON pub.id_language = lang.id JOIN public.publication_type pubType
ON pub.id_publication_type = pubType.id;