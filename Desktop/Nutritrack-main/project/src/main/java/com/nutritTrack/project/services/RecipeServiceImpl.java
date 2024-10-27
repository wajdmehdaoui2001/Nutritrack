package com.nutritTrack.project.services;

import com.nutritTrack.project.entities.Recipe;
import com.nutritTrack.project.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Override
    public Recipe createRecipe(Recipe recipe) {
        Recipe createdRecipe = new Recipe();
        createdRecipe.setTitle(recipe.getTitle());
        createdRecipe.setImage(recipe.getImage());
        createdRecipe.setDescription(recipe.getDescription());
        createdRecipe.setFoodType(recipe.getFoodType());
        createdRecipe.setTauxCalories(recipe.getTauxCalories());
        createdRecipe.setTauxCarbohydrates(recipe.getTauxCarbohydrates());
        createdRecipe.setTauxfats(recipe.getTauxfats());
        createdRecipe.setCreatedAt(LocalDateTime.now());
        return recipeRepository.save(createdRecipe);
    }
    @Override
    public Recipe findRecipeById(Long id) throws Exception {
        Optional<Recipe> opt = recipeRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new Exception("recipe not found with id"+id);
    }

    @Override
    public void deleteRecipe(Long id) throws Exception {
        findRecipeById(id);
        recipeRepository.deleteById(id);
    }

    @Override
    public Recipe updateRecipe(Recipe recipe, Long Id) throws Exception {
        Recipe oldRecipe=findRecipeById(Id);
        if(recipe.getTitle() !=null){
            oldRecipe.setTitle(recipe.getTitle());

        }
        if(recipe.getImage()!=null) {
            oldRecipe.setImage(recipe.getImage());

        }
        if(recipe.getDescription()!=null) {
            oldRecipe.setDescription(recipe.getDescription());

        }
        if(recipe.getTauxCalories()!=null){
            oldRecipe.setTauxCalories(recipe.getTauxCalories());
        }
        if(recipe.getTauxCarbohydrates()!=0){
            oldRecipe.setTauxCarbohydrates(recipe.getTauxCarbohydrates());
        }
        if(recipe.getTauxfats()!=0){
            oldRecipe.setTauxfats(recipe.getTauxfats());
        }
        if(recipe.getFoodType()!=null){
            oldRecipe.setFoodType(recipe.getFoodType());
        }
        return recipeRepository.save(oldRecipe);
    }

    @Override
    public List<Recipe> findAllRecipe() {
        return recipeRepository.findAll();
    }
}
