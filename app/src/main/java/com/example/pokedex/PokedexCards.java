package com.example.pokedex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.adapter.PokemonAdapter;
import com.example.pokedex.dto.PokemonDTO;
import com.example.pokedex.dto.PokemonListItemDTO;
import com.example.pokedex.dto.PokemonListResponseDTO;
import com.example.pokedex.feign.PokemonFeign;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokedexCards extends AppCompatActivity implements PokemonFilterBottomSheet.FilterListener {

    private Retrofit retrofit;
    private RecyclerView recycleView;

    private List<PokemonDTO> allPokemonList = new ArrayList<>();

    private List<PokemonDTO> pokemonDetalhesList = new ArrayList<>();

    private PokemonAdapter adapter;
    private TextInputEditText searchEditText;

    private Set<String> currentSelectedTypes = new HashSet<>();
    private boolean currentShowFavorites = false;
    private String currentOrder = "Menor ID primeiro";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pokedex_cards);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recycleView = findViewById(R.id.cardsPokemon);
        recycleView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new PokemonAdapter(pokemonDetalhesList, this::showPokemonDetails);
        recycleView.setAdapter(adapter);

        searchEditText = findViewById(R.id.nomePokemonBuscado);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty() && (currentShowFavorites || !currentSelectedTypes.isEmpty())) {
                    currentShowFavorites = false;
                    currentSelectedTypes.clear();
                    currentOrder = "Menor ID primeiro";
                }
                filterByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        getPokemons();
    }

    private void filterByName(String text) {
        String query = text.toLowerCase(Locale.ROOT).trim();

        List<PokemonDTO> baseList = new ArrayList<>(allPokemonList);

        pokemonDetalhesList.clear();

        if (query.isEmpty()) {
            applyAllFilters();
        } else {
            for (PokemonDTO pokemon : allPokemonList) {
                if (pokemon.getName().toLowerCase(Locale.ROOT).contains(query)) {
                    pokemonDetalhesList.add(pokemon);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void getPokemons() {
        String url = "https://pokeapi.co/api/v2/";

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PokemonFeign pokemonFeign = retrofit.create(PokemonFeign.class);

        Call<PokemonListResponseDTO> allPokemons = pokemonFeign.getAllPokemons();

        allPokemons.enqueue(new Callback<PokemonListResponseDTO>() {
            @Override
            public void onResponse(Call<PokemonListResponseDTO> call, Response<PokemonListResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PokemonListItemDTO> lista = response.body().getResults();

                    for (PokemonListItemDTO item : lista) {
                        Call<PokemonDTO> detalheCall = pokemonFeign.getPokemonByName(item.getName());
                        detalheCall.enqueue(new Callback<PokemonDTO>() {
                            @Override
                            public void onResponse(Call<PokemonDTO> call, Response<PokemonDTO> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    PokemonDTO pokemon = response.body();

                                    allPokemonList.add(pokemon);
                                    pokemonDetalhesList.add(pokemon);

                                    adapter.notifyItemInserted(pokemonDetalhesList.size() - 1);
                                }
                            }

                            @Override
                            public void onFailure(Call<PokemonDTO> call, Throwable t) {
                                Toast.makeText(PokedexCards.this, "Erro ao buscar Pokémon: " + item.getName(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<PokemonListResponseDTO> call, Throwable t) {
                Toast.makeText(PokedexCards.this, "Erro ao listar Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPokemonDetails(PokemonDTO pokemon) {
        Gson gson = new Gson();
        String pokemonJson = gson.toJson(pokemon);

        Intent intent = new Intent(this, PokemonDetails.class);
        intent.putExtra("pokemon_data_json", pokemonJson);
        startActivity(intent);
    }

    public void abrirBottomSheetParaFiltrar(View view) {
        searchEditText.setText("");

        PokemonFilterBottomSheet bottomSheet = new PokemonFilterBottomSheet();
        bottomSheet.show(getSupportFragmentManager(), "FilterBottomSheet");
    }

    @Override
    public void onFilterApplied(Set<String> selectedTypes, boolean showFavorites, String order) {
        this.currentSelectedTypes = selectedTypes;
        this.currentShowFavorites = showFavorites;
        this.currentOrder = order;

        searchEditText.setText("");

        applyAllFilters();
        Toast.makeText(this, "Filtro Aplicado! Tipos: " + selectedTypes.size(), Toast.LENGTH_SHORT).show();
    }

    private void applyAllFilters() {
        List<PokemonDTO> filteredList = new ArrayList<>(allPokemonList);

        if (currentShowFavorites) {
            filteredList.removeIf(pokemon -> !FavoritesManager.isFavorite(pokemon.getId()));
        }

        if (!currentSelectedTypes.isEmpty()) {
            filteredList.removeIf(pokemon -> {
                boolean matchesType = false;
                for (String selectedType : currentSelectedTypes) {
                    for (com.example.pokedex.dto.TypeSlotDTO typeSlot : pokemon.getTypes()) {
                        if (typeSlot.getType().getName().equals(selectedType)) {
                            matchesType = true;
                            break;
                        }
                    }
                    if (matchesType) break;
                }
                return !matchesType;
            });
        }

        sortList(filteredList, currentOrder);

        pokemonDetalhesList.clear();
        pokemonDetalhesList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }

    private void sortList(List<PokemonDTO> list, String order) {
        switch (order) {
            case "A-Z (Nome)":
                list.sort(Comparator.comparing(PokemonDTO::getName));
                break;
            case "Z-A (Nome)":
                list.sort(Comparator.comparing(PokemonDTO::getName, Collections.reverseOrder()));
                break;
            case "Menor ID primeiro":
                list.sort(Comparator.comparing(PokemonDTO::getId));
                break;
            case "Maior ID primeiro":
                list.sort(Comparator.comparing(PokemonDTO::getId, Collections.reverseOrder()));
                break;
        }
    }
}