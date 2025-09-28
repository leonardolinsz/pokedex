package com.example.pokedex;

import java.util.HashSet;
import java.util.Set;

public class FavoritesManager {
    private static final Set<Integer> favoritePokemonIds = new HashSet<>();

    private FavoritesManager() {
    }

    public static boolean toggleFavorite(int pokemonId) {
        if (favoritePokemonIds.contains(pokemonId)) {
            favoritePokemonIds.remove(pokemonId);
            return false;
        } else {
            favoritePokemonIds.add(pokemonId);
            return true;
        }
    }

    public static boolean isFavorite(int pokemonId) {
        return favoritePokemonIds.contains(pokemonId);
    }

    public static Set<Integer> getFavoriteIds() {
        return favoritePokemonIds;
    }
}
