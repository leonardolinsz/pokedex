package com.example.pokedex.dto;

import java.util.List;

public class PokemonListResponseDTO {
    private int count;
    private String next;
    private String previous;
    private List<PokemonListItemDTO> results;

    public PokemonListResponseDTO(int count, String next, String previous, List<PokemonListItemDTO> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PokemonListItemDTO> getResults() {
        return results;
    }

    public void setResults(List<PokemonListItemDTO> results) {
        this.results = results;
    }
}
