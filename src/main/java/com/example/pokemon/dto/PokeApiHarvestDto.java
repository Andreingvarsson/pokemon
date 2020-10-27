package com.example.pokemon.dto;

import java.util.List;

public class PokeApiHarvestDto {

    private List<PokemonToDBDto> results;

    public PokeApiHarvestDto() {
    }

    public PokeApiHarvestDto(List<PokemonToDBDto> results) {
        this.results = results;
    }

    public List<PokemonToDBDto> getResults() {
        return results;
    }

    public void setResults(List<PokemonToDBDto> results) {
        this.results = results;
    }
}
