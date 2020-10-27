package com.example.pokemon.repositories;

import com.example.pokemon.entities.PokeApiHarvest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokeApiHarvestRepository extends MongoRepository<PokeApiHarvest, String> {

}
