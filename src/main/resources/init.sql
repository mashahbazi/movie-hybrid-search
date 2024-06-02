CREATE EXTENSION IF NOT EXISTS vector;

create sequence if not exists movies_seq;

create table if not exists movies
(
    id       bigint        not null primary key default nextval('movies_seq'),
    title    varchar(1000) not null unique,
    overview varchar(1000) not null
);

create sequence if not exists movies_index_seq;

create table if not exists movies_index(
    id          bigint      not null primary key default nextval('movies_index_seq'),
    movie_id    bigint      not null references movies(id),
    content     text        not null,
    fts         tsvector    not null generated always as (to_tsvector('english', content)) stored,
    embedding   vector(1999) not null
);

create index on movies_index using gin(fts);

create index on movies_index using hnsw (embedding vector_cosine_ops);