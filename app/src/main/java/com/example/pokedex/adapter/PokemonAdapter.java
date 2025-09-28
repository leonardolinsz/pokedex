package com.example.pokedex.adapter;

import static com.example.pokedex.utils.TypeUtils.getColorRes;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;
import com.example.pokedex.dto.PokemonDTO;
import com.example.pokedex.dto.TypeSlotDTO;
import com.example.pokedex.utils.TypeUtils;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Locale;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private final List<PokemonDTO> pokemonList;
    private final OnItemClickListener listener;

    public PokemonAdapter(List<PokemonDTO> pokemonList, OnItemClickListener listener) {
        this.pokemonList = pokemonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        PokemonDTO pokemon = pokemonList.get(position);

        holder.nomePokemon.setText(TypeUtils.capitalize(pokemon.getName()));

        String formattedId = String.format(Locale.US, "#%03d", pokemon.getId());
        holder.idPokemon.setText(formattedId);

        List<TypeSlotDTO> types = pokemon.getTypes();
        if (types != null && !types.isEmpty()) {
            String type1 = types.get(0).getType().getName();

            holder.chip1.setText(TypeUtils.capitalize(type1));
            setChipColor(holder.chip1, type1);
            holder.chip1.setVisibility(View.VISIBLE);

            if (types.size() > 1) {
                String type2 = types.get(1).getType().getName();
                holder.chip2.setText(TypeUtils.capitalize(type2));
                setChipColor(holder.chip2, type2);
                holder.chip2.setVisibility(View.VISIBLE);
            } else {
                holder.chip2.setVisibility(View.GONE);
            }

            setCardColor(holder.cardView, type1);
        } else {
            holder.chip1.setVisibility(View.GONE);
            holder.chip2.setVisibility(View.GONE);
            setCardColor(holder.cardView, null);
        }

        Glide.with(holder.imgPhoto.getContext())
                .load(pokemon.getSprites().getFront_default())
                .into(holder.imgPhoto);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(pokemon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public class PokemonViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView nomePokemon, idPokemon;
        Chip chip1, chip2;
        CardView cardView;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            idPokemon = itemView.findViewById(R.id.idPokemon);
            cardView = itemView.findViewById(R.id.cardView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            nomePokemon = itemView.findViewById(R.id.nomePokemon);
            chip1 = itemView.findViewById(R.id.type1);
            chip2 = itemView.findViewById(R.id.type2);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PokemonDTO pokemon);
    }

    private void setChipColor(Chip chip, String typeName) {
        int colorRes = getColorRes(typeName, true); // true = chip
        int color = ContextCompat.getColor(chip.getContext(), colorRes);
        chip.setChipBackgroundColor(ColorStateList.valueOf(color));
    }

    private void setCardColor(CardView cardView, String typeName) {
        int colorRes = getColorRes(typeName, false); // false = card
        int color = ContextCompat.getColor(cardView.getContext(), colorRes);
        cardView.setCardBackgroundColor(color);
    }

}