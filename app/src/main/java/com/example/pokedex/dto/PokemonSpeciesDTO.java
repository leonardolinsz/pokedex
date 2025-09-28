package com.example.pokedex.dto;

import com.google.gson.annotations.SerializedName;

// PokemonSpeciesDTO.java

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PokemonSpeciesDTO {

    @SerializedName("flavor_text_entries")
    private List<FlavorTextEntry> flavorTextEntries;

    // Getters e Setters
    public List<FlavorTextEntry> getFlavorTextEntries() {
        return flavorTextEntries;
    }

    public void setFlavorTextEntries(List<FlavorTextEntry> flavorTextEntries) {
        this.flavorTextEntries = flavorTextEntries;
    }

    // Subclasse interna para o texto de descrição
    public static class FlavorTextEntry {
        @SerializedName("flavor_text")
        private String flavorText;

        private Language language;

        // Getters e Setters
        public String getFlavorText() {
            return flavorText;
        }

        public Language getLanguage() {
            return language;
        }

        // ... (Setters, se você precisar)
    }

    // Subclasse interna para o idioma
    public static class Language {
        private String name;

        // Getters e Setters
        public String getName() {
            return name;
        }

        // ... (Setters, se você precisar)
    }
}

