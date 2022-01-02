package com.example.myfoodcoach.logic;

import java.util.Map;

public class FoodInfo {
    public String id;
    public Double energyVal;
    public Double sugarVal;
    public Double fatVal;
    public Double saturatedFatVal;
    public Double sodiumVal;
    public Double fiberVal;
    public Double proteinVal;
    public Double transFatVal;
    public Double unsaturatedFatVal;
    public Double carbohydrateVal;
    public Double cholesterolVal;
    public String category;
    public Integer quantity;
    public String productName;
    public String image;
    public Double servingSize;


    public FoodInfo(Map<String, String> foodmap) {
        this.id = foodmap.get("id");
        System.out.println("this.id = " + this.id);
        this.energyVal = Double.parseDouble(foodmap.get("calories"));
        this.sugarVal = Double.parseDouble(foodmap.get("sugarContent"));
        this.fatVal = Double.parseDouble(foodmap.get("fatContent"));
        this.saturatedFatVal = Double.parseDouble(foodmap.get("saturatedFatContent"));
        this.sodiumVal = Double.parseDouble(foodmap.get("sodiumContent"));
        this.fiberVal = Double.parseDouble(foodmap.get("fiberContent"));
        this.proteinVal = Double.parseDouble(foodmap.get("proteinContent"));
        this.transFatVal = Double.parseDouble(foodmap.get("transFatContent"));
        this.unsaturatedFatVal = Double.parseDouble(foodmap.get("unsaturatedFatContent"));
        this.carbohydrateVal = Double.parseDouble(foodmap.get("carbohydrateContent"));
        this.cholesterolVal = Double.parseDouble(foodmap.get("cholesterolContent"));
        this.category = foodmap.get("category");
        this.servingSize = Double.parseDouble(foodmap.get("standardAmount"));

    }

    public FoodInfo(Double energyVal, Double sugarVal, Double fatVal, Double saturatedFatVal, Double sodiumVal, Double fiberVal, Double proteinVal, String category, Integer quantity, String produdctName, String image) {
        this.energyVal = energyVal;
        this.sugarVal = sugarVal;
        this.fatVal = fatVal;
        this.saturatedFatVal = saturatedFatVal;
        this.sodiumVal = sodiumVal;
        this.fiberVal = fiberVal;
        this.proteinVal = proteinVal;
        this.category = category;
        this.quantity = quantity;
        this.productName = produdctName;
        this.image = image;
    }

    public void setImage(String base64Data) {
        this.image = base64Data;
    }

    public void transform() {
        this.energyVal = this.energyVal / servingSize * 100;
        this.sugarVal = this.sugarVal / servingSize * 100;
        this.fatVal = this.fatVal / servingSize * 100;
        this.saturatedFatVal = this.saturatedFatVal / servingSize * 100;
        this.sodiumVal = this.sodiumVal / servingSize * 100;
        this.fiberVal = this.fiberVal / servingSize * 100;
        this.proteinVal = this.proteinVal / servingSize * 100;
        this.transFatVal = this.transFatVal / servingSize * 100;
        this.unsaturatedFatVal = this.unsaturatedFatVal / servingSize * 100;
        this.carbohydrateVal = this.carbohydrateVal / servingSize * 100;
        this.cholesterolVal = this.cholesterolVal / servingSize * 100;
        this.servingSize = 100.0;
    }
}

