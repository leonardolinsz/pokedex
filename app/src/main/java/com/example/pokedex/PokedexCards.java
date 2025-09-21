package com.example.pokedex;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokedex.adapter.PokemonAdapter;
import com.example.pokedex.dto.PokemonDTO;
import com.example.pokedex.dto.PokemonListItemDTO;
import com.example.pokedex.dto.PokemonListResponseDTO;
import com.example.pokedex.dto.TypeSlotDTO;
import com.example.pokedex.feign.PokemonFeign;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokedexCards extends AppCompatActivity {

    private Retrofit retrofit;
    private RecyclerView recycleView;
    private List<PokemonDTO> pokemonDetalhesList = new ArrayList<>();
    private PokemonAdapter adapter;

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
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PokemonAdapter(pokemonDetalhesList, this::showPokemonDetails);
        recycleView.setAdapter(adapter);

        getPokemons();
    }

    private void getPokemons() {
        String url = "https://pokeapi.co/api/v2/";

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PokemonFeign pokemonFeign = retrofit.create(PokemonFeign.class);

        // Primeiro busca a lista de Pokémon
        Call<PokemonListResponseDTO> allPokemons = pokemonFeign.getAllPokemons(); // limit=50

        allPokemons.enqueue(new Callback<PokemonListResponseDTO>() {
            @Override
            public void onResponse(Call<PokemonListResponseDTO> call, Response<PokemonListResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PokemonListItemDTO> lista = response.body().getResults(); // CORRETO

                    // Para cada Pokémon da lista, busca os detalhes
                    for (PokemonListItemDTO item : lista) {
                        Call<PokemonDTO> detalheCall = pokemonFeign.getPokemonByName(item.getName());
                        detalheCall.enqueue(new Callback<PokemonDTO>() {
                            @Override
                            public void onResponse(Call<PokemonDTO> call, Response<PokemonDTO> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    pokemonDetalhesList.add(response.body());
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
        View view = getLayoutInflater().inflate(R.layout.activity_pokemon_detail_bottomsheet, null);

        ImageView img = view.findViewById(R.id.imgPokemonGif);
        TextView nome = view.findViewById(R.id.tvNomePokemon);
        TextView tipos = view.findViewById(R.id.tvTipos);
        TextView hp = view.findViewById(R.id.tvHP);
        TextView ataque = view.findViewById(R.id.tvAtaqueEspecial);
        TextView defesa = view.findViewById(R.id.tvDefesaEspecial);
        TextView ataqueEspecial = view.findViewById(R.id.tvAtaqueEspecial);
        TextView defesaEspecial = view.findViewById(R.id.tvDefesaEspecial);
        TextView velocidade = view.findViewById(R.id.tvVelocidade);

        Glide.with(this).load(pokemon.getSprites().getFront_default()).into(img);
        nome.setText(pokemon.getName());

        StringBuilder typeStr = new StringBuilder();
        for (TypeSlotDTO t : pokemon.getTypes()) {
            typeStr.append(t.getType().getName()).append(" ");
        }
        tipos.setText(typeStr.toString().trim());

        // Supondo que PokémonDTO tenha um método getStats() que retorna os stats
        pokemon.getStats().forEach(stat -> {
            switch (stat.getStat().getName().toLowerCase()) {
                case "hp": hp.setText("HP: " + stat.getBase_stat()); break;
                case "attack": ataque.setText("Ataque: " + stat.getBase_stat()); break;
                case "defense": defesa.setText("Defesa: " + stat.getBase_stat()); break;
                case "special-attack": ataqueEspecial.setText("Ataque Especial: " + stat.getBase_stat()); break;
                case "special-defense": defesaEspecial.setText("Defesa Especial: " + stat.getBase_stat()); break;
                case "speed": velocidade.setText("Velocidade: " + stat.getBase_stat()); break;
            }
        });

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

}
