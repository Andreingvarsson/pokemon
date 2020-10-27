package com.example.pokemon.entities;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class PokeApiHarvest implements Serializable {
    private static final long serialVersionUID = 4163605045098458405L;

    @Id
    private String id;
    private String name;

    public PokeApiHarvest() {
    }

    public PokeApiHarvest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
