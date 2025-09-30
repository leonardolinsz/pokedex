package com.example.pokedex;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PokemonFilterBottomSheet extends BottomSheetDialogFragment {

    public interface FilterListener {
        void onFilterApplied(Set<String> selectedTypes, boolean showFavorites, String order);
    }

    private FilterListener listener;
    private ChipGroup chipGroupTypes;
    private ChipGroup chipGroupFavorites;
    private Spinner spinnerOrder;
    private Button btnApplyFilter;

    private static final List<String> ALL_POKEMON_TYPES = Arrays.asList(
            "normal", "fire", "water", "electric", "grass", "ice", "fighting",
            "poison", "ground", "flying", "psychic", "bug", "rock", "ghost",
            "dragon", "dark", "steel", "fairy"
    );

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (FilterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " deve implementar FilterListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_filter, container, false);

        chipGroupTypes = view.findViewById(R.id.chipGroupTypes);
        chipGroupFavorites = view.findViewById(R.id.chipGroupFavorites);
        spinnerOrder = view.findViewById(R.id.spinnerOrder);
        btnApplyFilter = view.findViewById(R.id.btnApplyFilter);

        populateTypeChips(inflater);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.order_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrder.setAdapter(adapter);

        // 3. Ação do Botão Aplicar
        btnApplyFilter.setOnClickListener(v -> applyFilters());

        return view;
    }

    private void populateTypeChips(LayoutInflater inflater) {
        for (String type : ALL_POKEMON_TYPES) {
            Chip chip = (Chip) inflater.inflate(R.layout.chip_type_template, chipGroupTypes, false);
            chip.setText(type.substring(0, 1).toUpperCase() + type.substring(1));
            chip.setCheckable(true);
            chipGroupTypes.addView(chip);
        }
    }

    private void applyFilters() {
        Set<String> selectedTypes = new HashSet<>();
        for (int id : chipGroupTypes.getCheckedChipIds()) {
            Chip chip = chipGroupTypes.findViewById(id);
            selectedTypes.add(chip.getText().toString().toLowerCase(Locale.ROOT));
        }

        boolean showFavorites = chipGroupFavorites.getCheckedChipId() == R.id.chipShowFavorites;
        String selectedOrder = spinnerOrder.getSelectedItem().toString();

        if (listener != null) {
            listener.onFilterApplied(selectedTypes, showFavorites, selectedOrder);
        }

        dismiss();
    }
}
