package mx.com.dezkser.recipesservice.service;

import mx.com.dezkser.recipesservice.models.Recipe;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipesService {
    private final Set<Recipe> recipes;

    public RecipesService() {
        this.recipes = new HashSet<>();
    }

    // Metodo para agregar una nueva receta
    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    // Metodo para eliminar una receta por id
    public boolean removeRecipeById(String id) {
        return recipes.removeIf(recipe -> recipe.getId().equals(id));
    }

    // Metodo para listar todas las recetas
    public Set<Recipe> getAllRecipes() {
        return new HashSet<>(recipes);
    }

    // Metodo para buscar una receta por id
    public Optional<Recipe> getRecipeById(String id) {
        return recipes.stream()
                .filter(recipe -> recipe.getId().equals(id))
                .findFirst();
    }

    // Metodo para buscar recetas por nombre
    public List<Recipe> getRecipesByName(String name) {
        return recipes.stream()
                .filter(recipe -> recipe.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    // Metodo para buscar recetas por ingrediente
    public List<Recipe> getRecipesByIngredient(String ingredient) {
        return recipes.stream()
                .filter(recipe -> recipe.getIngredients().contains(ingredient))
                .collect(Collectors.toList());
    }
}
