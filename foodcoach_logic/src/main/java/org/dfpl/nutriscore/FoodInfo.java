package org.dfpl.nutriscore;

public class FoodInfo {
    public Double energyVal;
    public Double sugarVal;
    public Double fatVal;
    public Double saturatedFatVal;
    public Double sodiumVal;
    public Double fiberVal;
    public Double proteinVal;
    public String category;
    public Integer quantity;
    public String productName;
    public FoodInfo(Double energyVal, Double sugarVal, Double fatVal, Double saturatedFatVal, Double sodiumVal, Double fiberVal, Double proteinVal, String category, Integer quantity, String produdctName) {
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
    }
}
