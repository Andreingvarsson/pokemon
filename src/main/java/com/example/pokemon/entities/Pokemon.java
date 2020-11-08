package com.example.pokemon.entities;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class Pokemon implements Serializable {
    private static final long serialVersionUID = -865214149412787221L;

    @Id
    private String id;
    @NotBlank
    private String name;
    @Min(1)
    private int height;
    @Min(1)
    private int weight;

    public Pokemon() {
    }

    public Pokemon(String name, int height, int weight ) {
        this.name = name.toLowerCase();
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
        this.name = name.toLowerCase();
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
