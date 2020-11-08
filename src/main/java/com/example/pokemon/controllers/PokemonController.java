package com.example.pokemon.controllers;

import com.example.pokemon.entities.Pokemon;
import com.example.pokemon.entities.User;
import com.example.pokemon.services.PokemonConsumerService;
import com.example.pokemon.services.PokemonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v1/pokemon")
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;
    @Autowired
    private PokemonConsumerService pokemonConsumerService;

// Collect all pokemonnames and url do DB
    /*
    @GetMapping("/test")
    public void findAllPokemon(){ pokemonConsumerService.getAllPokemonsFromApi();
    }
     */

    @Operation(summary = "Find a pokemon or a list of pokemon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the pokemon",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pokemon.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @GetMapping
    public ResponseEntity<List<Pokemon>> findPokemon(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minHeight,
            @RequestParam(required = false) Integer maxHeight,
            @RequestParam(required = false) Integer minWeight,
            @RequestParam(required = false) Integer maxWeight) {
        var pokemon = pokemonService.searchPokemon(name, minHeight, maxHeight, minWeight, maxWeight);
        return ResponseEntity.ok(pokemon);
    }

    @Operation(summary = "Find a pokemon with a specific id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the pokemon",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pokemon.class))}),
            @ApiResponse(responseCode = "404", description = "Pokemon not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> findPokemonById(@PathVariable String id) {
        return ResponseEntity.ok(pokemonService.findById(id));
    }

    @Operation(summary = "Create/save a pokemon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Created the pokemon",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pokemon.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request, require: name, height, weight",
                    content = @Content)})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Pokemon> savePokemon(@RequestBody Pokemon pokemon) {
        var savedPokemon = pokemonService.save(pokemon);
        var uri = URI.create("/api/v1/pokemon/" + savedPokemon.getId());
        return ResponseEntity.created(uri).body(savedPokemon);
    }

    @Operation(summary = "Update a pokemon with a specific id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Updated the pokemon",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Pokemon not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authorized",
                    content = @Content)})
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePokemon(@PathVariable String id, @RequestBody Pokemon pokemon) {
        pokemonService.update(id, pokemon);
    }

    @Operation(summary = "Delete a pokemon with a specific id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted the pokemon",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Pokemon not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authorized",
                    content = @Content)})
    @DeleteMapping("{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePokemon(@PathVariable String id) {
        pokemonService.delete(id);
    }


}
