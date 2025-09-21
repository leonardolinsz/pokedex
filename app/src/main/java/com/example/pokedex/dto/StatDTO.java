package com.example.pokedex.dto;

public class StatDTO {

    private int base_stat;
    private StatDetail stat;

    public StatDTO(int base_stat, StatDetail stat) {
        this.base_stat = base_stat;
        this.stat = stat;
    }

    public int getBase_stat() {
        return base_stat;
    }

    public void setBase_stat(int base_stat) {
        this.base_stat = base_stat;
    }

    public StatDetail getStat() {
        return stat;
    }

    public void setStat(StatDetail stat) {
        this.stat = stat;
    }
}
