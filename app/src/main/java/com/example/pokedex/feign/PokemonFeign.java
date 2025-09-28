package com.example.pokedex.feign;

import com.example.pokedex.dto.PokemonDTO;
import com.example.pokedex.dto.PokemonListResponseDTO;
import com.example.pokedex.dto.PokemonSpeciesDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokemonFeign {

    @GET("pokemon?limit=100&offset=0")
    Call<PokemonListResponseDTO> getAllPokemons();

    @GET("pokemon")
    Call<PokemonListResponseDTO> getAllPokemons(@Query("limit") int limit, @Query("offset") int offset);

    @GET("pokemon/{name}")
    Call<PokemonDTO> getPokemonByName(@Path("name") String name);

    @GET("pokemon-species/{id}")
    Call<PokemonSpeciesDTO> getPokemonSpeciesById(@Path("id") Integer id);


}
