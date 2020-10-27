package com.example.pokemon.services;


import com.example.pokemon.entities.Pokemon;
import com.example.pokemon.repositories.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class PokemonService {

    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    private PokemonConsumerService pokemonConsumerService;


    @Cacheable(value = "pokemonCache", key = "#name")
    public List<Pokemon> findAll(String name){
        var pokemons = pokemonRepository.findByName(name);
        if(pokemons.isEmpty()){
            var PokemonDto = pokemonConsumerService.search(name);
            if(PokemonDto != null){
                var pokemon = new Pokemon(PokemonDto.getName(), PokemonDto.getHeight(), PokemonDto.getWeight());
                pokemons.add(this.save(pokemon));
            }
        }
        return pokemons;
    }


    public Pokemon findById(String id){
        return pokemonRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Kunde inte hitta pokemonen.."));
    }

    @CachePut(value = "pokemonCache", key = "#result.id")
    public Pokemon save(Pokemon pokemon){
        return pokemonRepository.save(pokemon);
    }

    @CachePut(value = "pokemonCache", key = "id")
    public void update(String id, Pokemon pokemon){
        if(!pokemonRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kunde inte hitta pokemonen..");
        }
        pokemon.setId(id);
        pokemonRepository.save(pokemon);
    }

    @CacheEvict(value = "pokemonCache", allEntries = true)
    public void delete(String id){
        if(!pokemonRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kunde inte hitta pokemonen..");
        }
        pokemonRepository.deleteById(id);
    }

}
