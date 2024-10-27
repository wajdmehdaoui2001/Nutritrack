package com.nutritTrack.project.controllers;

import com.nutritTrack.project.entities.Recipe;
import com.nutritTrack.project.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @PostMapping()
    public Recipe createRecipe(@RequestBody Recipe recipe) throws Exception {
        Recipe createdRecipe=recipeService.createRecipe(recipe);
        return createdRecipe;

    }

    @PutMapping("/{id}")
    public Recipe updateRecipe(@RequestBody Recipe recipe , @PathVariable Long id) throws Exception {


        Recipe updatedRecipe=recipeService.updateRecipe(recipe, id);
        return updatedRecipe;

    }
    @GetMapping()
    public List<Recipe> getAllRecipe() throws Exception{
        List<Recipe> recipes=recipeService.findAllRecipe();
        return recipes;
    }
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Map<String, String>> deleteRecipe(@PathVariable Long recipeId) throws Exception {
        recipeService.deleteRecipe(recipeId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Recipe deleted successfully");
        return ResponseEntity.ok(response);
    }

}
