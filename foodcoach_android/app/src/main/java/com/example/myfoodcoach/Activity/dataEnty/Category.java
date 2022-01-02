package com.example.myfoodcoach.Activity.dataEnty;

public enum Category {
    FruitVegetable, Fat, General, Beverage, Cheese, Water;

    @Override
    public String toString() {
        switch (this) {
            case FruitVegetable:
                return "fruit/vegetable";
            case Fat:
                return "fat";
            case General:
                return "general";
            case Beverage:
                return "beverage";
            case Cheese:
                return "cheese";
            case Water:
                return "water";
            default:
                throw new IllegalArgumentException();
        }
    }
}

