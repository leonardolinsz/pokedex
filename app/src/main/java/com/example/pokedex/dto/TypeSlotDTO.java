package com.example.pokedex.dto;

public class TypeSlotDTO {
    private int slot;
    private TypeDetail type;

    public TypeSlotDTO(int slot, TypeDetail type) {
        this.slot = slot;
        this.type = type;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public TypeDetail getType() {
        return type;
    }

    public void setType(TypeDetail type) {
        this.type = type;
    }
}
