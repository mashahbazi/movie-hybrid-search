CREATE EXTENSION IF NOT EXISTS vector;

create sequence if not exists syncs_seq;

create table if not exists syncs
(
    id     bigint  not null primary key default nextval('syncs_seq'),
    cached boolean not null,
    synced boolean not null,
    year   integer not null,

    UNIQUE (year)
);

create sequence if not exists movies_seq;

create table if not exists movies
(
    id       bigint        not null primary key default nextval('movies_seq'),
    embedded boolean       not null,
    overview varchar(1000) not null,
    title    varchar(1000) not null
);

create table if not exists movies_index(
    id bigint not null primary key generated always as identity,
    movie_id bigint references movies(id),
    content text,
    fts tsvector generated always as (to_tsvector('english', content)) stored,
    embedding vector(512)
);

create index on movies_index using gin(fts);

create index on movies_index using hnsw (embedding vector_cosine_ops);