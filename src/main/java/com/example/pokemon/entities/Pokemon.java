package com.example.pokemon.entities;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

public class Pokemon implements Serializable {
    private static final long serialVersionUID = -865214149412787221L;

    @Id
    private String id;
    private String name;
    private int height;
    private int weight;



    public Pokemon() {
    }

    public Pokemon(String name, int height, int weight ) {
        this.name = name;
        this.height = height;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
