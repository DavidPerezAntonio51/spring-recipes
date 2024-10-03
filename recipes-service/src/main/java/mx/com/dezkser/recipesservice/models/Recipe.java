package mx.com.dezkser.recipesservice.models;

import lombok.Data;

import java.util.List;

@Data
public class Recipe {
    private String id;
    private String name;
    private String description;
    private List<String> steps;
    private List<String> ingredients;
}
