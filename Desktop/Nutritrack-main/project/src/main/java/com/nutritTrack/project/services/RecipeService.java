package com.nutritTrack.project.services;

import com.nutritTrack.project.entities.Recipe;

import java.util.List;

public interface RecipeService {
    public Recipe createRecipe(Recipe recipe);
    public Recipe findRecipeById(Long id) throws Exception ;
    public void deleteRecipe(Long id) throws Exception ;
    public Recipe updateRecipe(Recipe recipe,Long Id) throws Exception ;

    public List<Recipe> findAllRecipe();
}
