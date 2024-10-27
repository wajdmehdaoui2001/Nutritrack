package com.nutritTrack.project.entities;

import com.nutritTrack.project.enums.FoodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private FoodType foodType;

    private Long  tauxCalories;

    private  Long tauxProteine;

    private String image;

    private Long tauxCarbohydrates;

    private Long tauxfats;

    private LocalDateTime createdAt;

}
