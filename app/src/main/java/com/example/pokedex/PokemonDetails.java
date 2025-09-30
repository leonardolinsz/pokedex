package com.example.pokedex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.pokedex.dto.PokemonDTO;
import com.example.pokedex.dto.PokemonSpeciesDTO;
import com.example.pokedex.dto.TypeSlotDTO;
import com.example.pokedex.feign.PokemonFeign;
import com.example.pokedex.utils.TypeUtils;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokemonDetails extends AppCompatActivity {

    private TextView descricao;
    private PokemonFeign pokemonFeign;
    private PokemonDTO currentPokemon;
    private ImageButton favoriteButton;
    private LinearLayout layoutTipos;
    private ProgressBar progressHp, progressAtk, progressDef, progressSpAtk, progressSpDef, progressSpeed;
    private TextView valueHp, valueAtk, valueDef, valueSpAtk, valueSpDef, valueSpeed;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pokemon_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.meu_layout_principal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String pokemonJson = getIntent().getStringExtra("pokemon_data_json");
        if (pokemonJson == null) {
            Toast.makeText(this, "Erro: Dados do Pokémon não encontrados.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Gson gson = new Gson();
        PokemonDTO pokemon = gson.fromJson(pokemonJson, PokemonDTO.class);
        if (pokemon == null) {
            Toast.makeText(this, "Erro: Falha ao carregar dados do Pokémon.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        currentPokemon = pokemon;

        favoriteButton = findViewById(R.id.favoriteDetails);
        ImageView img = findViewById(R.id.imagePokemonDetails);
        TextView idPokemon = findViewById(R.id.idPokemonDetail);
        TextView nome = findViewById(R.id.nomePokemonDetails);
        descricao = findViewById(R.id.descriptionDetails);
        layoutTipos = findViewById(R.id.layoutTipos);

        progressHp = findViewById(R.id.progressHp);
        progressAtk = findViewById(R.id.progressAtk);
        progressDef = findViewById(R.id.progressDef);
        progressSpAtk = findViewById(R.id.progressSpAtk);
        progressSpDef = findViewById(R.id.progressSpDef);
        progressSpeed = findViewById(R.id.progressSpeed);

        valueHp = findViewById(R.id.valueHp);
        valueAtk = findViewById(R.id.valueAtk);
        valueDef = findViewById(R.id.valueDef);
        valueSpAtk = findViewById(R.id.valueSpAtk);
        valueSpDef = findViewById(R.id.valueSpDef);
        valueSpeed = findViewById(R.id.valueSpeed);

        updateFavoriteIcon(currentPokemon.getId());

        String gifUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/" + pokemon.getId() + ".gif";

        Glide.with(this)
                .asGif()
                .load(gifUrl)
                .error(Glide.with(this).load(pokemon.getSprites().getFront_default()))
                .into(img);

        // Gradiente de fundo
        if (!pokemon.getTypes().isEmpty()) {
            String primaryTypeName = pokemon.getTypes().get(0).getType().getName();
            int colorResourceId = TypeUtils.getColorRes(primaryTypeName, false);
            int typeColor = ContextCompat.getColor(this, colorResourceId);
            int whiteColor = ContextCompat.getColor(this, R.color.white);

            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{typeColor, whiteColor});

            View mainLayout = findViewById(R.id.meu_layout_principal);
            if (mainLayout != null) {
                mainLayout.setBackground(gd);
            }
        }

        String formattedId = String.format(Locale.US, "#%03d", pokemon.getId());
        idPokemon.setText(formattedId);
        nome.setText(pokemon.getName());

        displayPokemonTypes(pokemon);

        pokemon.getStats().forEach(stat -> {
            int statValue = stat.getBase_stat();
            switch (stat.getStat().getName().toLowerCase()) {
                case "hp":
                    progressHp.setProgress(statValue);
                    valueHp.setText(String.valueOf(statValue));
                    break;
                case "attack":
                    progressAtk.setProgress(statValue);
                    valueAtk.setText(String.valueOf(statValue));
                    break;
                case "defense":
                    progressDef.setProgress(statValue);
                    valueDef.setText(String.valueOf(statValue));
                    break;
                case "special-attack":
                    progressSpAtk.setProgress(statValue);
                    valueSpAtk.setText(String.valueOf(statValue));
                    break;
                case "special-defense":
                    progressSpDef.setProgress(statValue);
                    valueSpDef.setText(String.valueOf(statValue));
                    break;
                case "speed":
                    progressSpeed.setProgress(statValue);
                    valueSpeed.setText(String.valueOf(statValue));
                    break;
            }
        });

        buscarDescricao(pokemon.getId());
    }

    private void displayPokemonTypes(PokemonDTO pokemon) {
        layoutTipos.removeAllViews();
        for (TypeSlotDTO t : pokemon.getTypes()) {
            String typeName = t.getType().getName();
            Chip chip = new Chip(this);
            chip.setText(TypeUtils.capitalize(typeName));

            int colorRes = TypeUtils.getColorRes(typeName, true);
            int color = ContextCompat.getColor(this, colorRes);
            chip.setChipBackgroundColor(ColorStateList.valueOf(color));
            chip.setTextColor(ContextCompat.getColor(this, R.color.white));
            chip.setChipStrokeWidth(0);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, (int) getResources().getDimension(R.dimen.chip_spacing_details), 0);
            chip.setLayoutParams(params);

            layoutTipos.addView(chip);
        }
    }

    private void buscarDescricao(Integer pokemonId) {
        String url = "https://pokeapi.co/api/v2/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pokemonFeign = retrofit.create(PokemonFeign.class);

        Call<PokemonSpeciesDTO> speciesCall = pokemonFeign.getPokemonSpeciesById(pokemonId);
        speciesCall.enqueue(new Callback<PokemonSpeciesDTO>() {
            @Override
            public void onResponse(Call<PokemonSpeciesDTO> call, Response<PokemonSpeciesDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonSpeciesDTO species = response.body();
                    String descriptionText = "Descrição não encontrada.";

                    for (PokemonSpeciesDTO.FlavorTextEntry entry : species.getFlavorTextEntries()) {
                        if ("en".equals(entry.getLanguage().getName())) {
                            descriptionText = entry.getFlavorText().replace("\n", " ").replace("\f", " ");
                            break;
                        }
                    }
                    if ("Descrição não encontrada.".equals(descriptionText) && !species.getFlavorTextEntries().isEmpty()) {
                        descriptionText = species.getFlavorTextEntries().get(0).getFlavorText().replace("\n", " ").replace("\f", " ");
                    }
                    descricao.setText(descriptionText);
                } else {
                    descricao.setText("Falha ao carregar a descrição.");
                }
            }

            @Override
            public void onFailure(Call<PokemonSpeciesDTO> call, Throwable t) {
                descricao.setText("Erro de rede ao buscar a descrição.");
            }
        });
    }

    public void voltar(View view) {
        Intent intent = new Intent(this, PokedexCards.class);
        startActivity(intent);
        finish();
    }

    private void updateFavoriteIcon(int pokemonId) {
        if (FavoritesManager.isFavorite(pokemonId)) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    public void favoritar(View view) {
        if (currentPokemon == null) return;

        int pokemonId = currentPokemon.getId();
        boolean isNowFavorite = FavoritesManager.toggleFavorite(pokemonId);
        updateFavoriteIcon(pokemonId);

        String message = isNowFavorite ?
                currentPokemon.getName() + " foi adicionado aos favoritos!" :
                currentPokemon.getName() + " foi removido dos favoritos.";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
