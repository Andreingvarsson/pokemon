package com.example.pokemon.services;

import com.example.pokemon.entities.PokeApiHarvest;
import com.example.pokemon.entities.Pokemon;
import com.example.pokemon.repositories.PokeApiHarvestRepository;
import com.example.pokemon.repositories.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class PokemonService {

    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    private PokeApiHarvestRepository pokeApiHarvestRepository;
    @Autowired
    private PokemonConsumerService pokemonConsumerService;
    @Autowired
    private MongoTemplate mongoTemplate;


    public List<Pokemon> searchPokemon(String name, Integer minHeight, Integer maxHeight, Integer minWeight, Integer maxWeight){
        Query query = new Query();

        if(name != null && !name.isEmpty()){
            this.collectPokemonFromApi(name);
            query.addCriteria(Criteria.where("name").regex(name.toLowerCase()));
        }
        if(minHeight != null || maxHeight != null){
            query.addCriteria(criteriaQueryWithParameters("height", minHeight, maxHeight));
        }
        if(minWeight != null || maxWeight != null){
            query.addCriteria(criteriaQueryWithParameters("weight", minWeight, maxWeight));
        }
        List<Pokemon> pokemon = mongoTemplate.find(query, Pokemon.class);
        return pokemon;
    }


    private Criteria criteriaQueryWithParameters(String params, Integer minValue, Integer maxValue){
        if(minValue != null && maxValue != null){
            return Criteria.where(params).gte(minValue).lte(maxValue);
        }else if(maxValue != null){
            return Criteria.where(params).lte(maxValue);
        }else{
            return Criteria.where(params).gte(minValue);
        }
    }

    private void collectPokemonFromApi(String name) {
        if(name.toCharArray().length < 3){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Input %s is to short, require atleast 3 letters", name));
        }
        var pokemons = this.controlPokemonExistanceInDb(name);
        var nameOfPokemon = this.searchPokemonNameInDb(name);

        if(pokemons.size() < nameOfPokemon.size()){
            nameOfPokemon.forEach(namedPokemon -> {
                var existingPokemon = pokemonRepository.findByName(namedPokemon.getName());
                if(existingPokemon.isEmpty()){
                    var pokemonDto = pokemonConsumerService.search(namedPokemon.getName());
                    var pokemon = new Pokemon(pokemonDto.getName(), pokemonDto.getHeight(), pokemonDto.getWeight());
                    this.save(pokemon);
                }
            });
        }
    }

    public List<Pokemon> controlPokemonExistanceInDb(String name) {
        var pokemons = pokemonRepository.findAll();
        pokemons = pokemons.stream().filter(pokemon -> pokemon.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        return pokemons;
    }

    public List<PokeApiHarvest> searchPokemonNameInDb(String name) {
        var nameOfPokemons = pokeApiHarvestRepository.findAll();
        nameOfPokemons = nameOfPokemons.stream().filter(nameOfPokemon -> nameOfPokemon.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        return nameOfPokemons;
    }


    public Pokemon findById(String id) {
        return pokemonRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find the pokemon.."));
    }

    public Pokemon save(Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }

    public void update(String id, Pokemon pokemon) {
        if (!pokemonRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find the pokemon..");
        }
        pokemon.setId(id);
        pokemonRepository.save(pokemon);
    }

    public void delete(String id) {
        if (!pokemonRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find the pokemon..");
        }
        pokemonRepository.deleteById(id);
    }

}
