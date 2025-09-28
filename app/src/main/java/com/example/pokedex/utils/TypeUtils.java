package com.example.pokedex.utils;

import com.example.pokedex.R;
import java.util.Locale;

public class TypeUtils {

    public static int getColorRes(String typeName, boolean isChip) {
        if (typeName == null) return isChip ? R.color.type_default : R.color.card_default;

        typeName = typeName.trim().toLowerCase(Locale.ROOT);

        switch (typeName) {
            case "normal": return isChip ? R.color.type_normal : R.color.card_normal;
            case "fire": return isChip ? R.color.type_fire : R.color.card_fire;
            case "water": return isChip ? R.color.type_water : R.color.card_water;
            case "electric": return isChip ? R.color.type_electric : R.color.card_electric;
            case "grass": return isChip ? R.color.type_grass : R.color.card_grass;
            case "ice": return isChip ? R.color.type_ice : R.color.card_ice;
            case "fighting": return isChip ? R.color.type_fighting : R.color.card_fighting;
            case "poison": return isChip ? R.color.type_poison : R.color.card_poison;
            case "ground": return isChip ? R.color.type_ground : R.color.card_ground;
            case "flying": return isChip ? R.color.type_flying : R.color.card_flying;
            case "psychic": return isChip ? R.color.type_psychic : R.color.card_psychic;
            case "bug": return isChip ? R.color.type_bug : R.color.card_bug;
            case "rock": return isChip ? R.color.type_rock : R.color.card_rock;
            case "ghost": return isChip ? R.color.type_ghost : R.color.card_ghost;
            case "dragon": return isChip ? R.color.type_dragon : R.color.card_dragon;
            case "dark": return isChip ? R.color.type_dark : R.color.card_dark;
            case "steel": return isChip ? R.color.type_steel : R.color.card_steel;
            case "fairy": return isChip ? R.color.type_fairy : R.color.card_fairy;
            default: return isChip ? R.color.type_default : R.color.card_default;
        }
    }

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String lower = input.toLowerCase(Locale.ROOT);
        return lower.substring(0, 1).toUpperCase(Locale.ROOT) + lower.substring(1);
    }
}