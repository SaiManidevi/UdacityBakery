package com.example.android.udacitybakery.model;

import java.io.Serializable;
import java.util.List;

public class RecipeStepsContainer implements Serializable {
    private List<Bakery.StepsList> clickedRecipeStepsList;
    private Bakery.StepsList clickedStep;

    public RecipeStepsContainer(List<Bakery.StepsList> clickedRecipeStepsList, Bakery.StepsList clickedStep) {
        this.clickedRecipeStepsList = clickedRecipeStepsList;
        this.clickedStep = clickedStep;
    }

    public List<Bakery.StepsList> getClickedRecipeStepsList() {
        return clickedRecipeStepsList;
    }

    public Bakery.StepsList getClickedStep() {
        return clickedStep;
    }
}
