package com.example.pokedex.feign;

import com.example.pokedex.dto.PokemonDTO;
import com.example.pokedex.dto.PokemonListResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokemonFeign {

    @GET("pokemon?limit=50")
    Call<PokemonListResponseDTO> getAllPokemons();

    @GET("pokemon/{nomePokemon}")
    Call<PokemonDTO> getPokemonByName(@Path("nomePokemon") String nome);

}
