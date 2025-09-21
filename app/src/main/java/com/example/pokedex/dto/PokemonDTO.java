package com.example.pokedex.dto;

import java.util.List;

public class PokemonDTO {

    private int id;
    private String name;
    private int height;
    private int weight;
    private Sprites sprites;
    private List<StatDTO> stats;
    private List<TypeSlotDTO> types;

    public PokemonDTO(int id, String name, int height, int weight, Sprites sprites, List<StatDTO> stats, List<TypeSlotDTO> types) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.sprites = sprites;
        this.stats = stats;
        this.types = types;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public List<StatDTO> getStats() {
        return stats;
    }

    public void setStats(List<StatDTO> stats) {
        this.stats = stats;
    }

    public List<TypeSlotDTO> getTypes() {
        return types;
    }

    public void setTypes(List<TypeSlotDTO> types) {
        this.types = types;
    }
}
