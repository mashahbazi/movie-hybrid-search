

create or replace function hybrid_search(
    query_text text,
    query_embedding text,
    match_count integer,
    full_text_weight double precision DEFAULT 1,
    semantic_weight double precision DEFAULT 1,
    rrf_k integer DEFAULT 50
) returns SETOF movies
    language sql
as
$$
with full_text as (select movie_id,
                          -- Note: ts_rank_cd is not indexable but will only rank matches of the where clause
                          -- which shouldn't be too big
                          row_number() over (order by ts_rank_cd(fts, websearch_to_tsquery(query_text)) desc) as rank_ix
                   from movies_index
                   where fts @@ websearch_to_tsquery(query_text)
                   order by rank_ix
                   limit least(match_count, 30) * 2),
     semantic as (select movie_id,
                         row_number() over (order by embedding <#> cast(query_embedding as vector)) as rank_ix
                  from movies_index
                  order by rank_ix
                  limit least(match_count, 30) * 2)
select movies.*
from full_text
         full outer join semantic
                         on full_text.movie_id = semantic.movie_id
         join movies
              on coalesce(full_text.movie_id, semantic.movie_id) = movies.id
order by coalesce(1.0 / (rrf_k + full_text.rank_ix), 0.0) * full_text_weight +
         coalesce(1.0 / (rrf_k + semantic.rank_ix), 0.0) * semantic_weight
        desc
limit least(match_count, 30)
$$;

alter function hybrid_search(text, text, integer, double precision, double precision, integer) owner to postgres;

