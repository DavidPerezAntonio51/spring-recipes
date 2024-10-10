package mx.com.dezkser.recipesservice.controller;

import mx.com.dezkser.recipesservice.models.Recipe;
import mx.com.dezkser.recipesservice.service.RecipesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/recipes")
public class RecipesController {
    private final RecipesService recipesService;

    public RecipesController(RecipesService recipesService) {
        this.recipesService = recipesService;
    }

    // Endpoint para agregar una nueva receta
    @PostMapping
    public ResponseEntity<Void> addRecipe(@RequestBody Recipe recipe) {
        recipesService.addRecipe(recipe);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Endpoint para eliminar una receta por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeRecipeById(@PathVariable String id) {
        boolean removed = recipesService.removeRecipeById(id);
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para listar todas las recetas
    @GetMapping
    public ResponseEntity<Set<Recipe>> getAllRecipes() {
        Set<Recipe> recipes = recipesService.getAllRecipes();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    // Endpoint para buscar una receta por id
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        Optional<Recipe> recipe = recipesService.getRecipeById(id);
        return recipe.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para buscar recetas por nombre
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> getRecipesByName(@RequestParam String name) {
        List<Recipe> recipes = recipesService.getRecipesByName(name);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    // Endpoint para buscar recetas por ingrediente
    @GetMapping("/ingredient")
    public ResponseEntity<List<Recipe>> getRecipesByIngredient(@RequestParam String ingredient) {
        List<Recipe> recipes = recipesService.getRecipesByIngredient(ingredient);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }
}
