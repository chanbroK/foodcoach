package org.dfpl.nutriscore;

import java.util.ArrayList;
import java.util.List;
public class App {

    public static void main (String[] args){
//        https://world.openfoodfacts.org/product/8714100261088/knorr-cup-a-soup-soupe-veloute-de-tomates-54g-3-sachets
        FoodInfo vegeSoup = new FoodInfo(39000., 3.6, 0.6, 0.3, 0.332, 0., 1.2,"fruit/vegetable",3,"VegeSoup");
        System.out.println("[Normal] nutriscore.NutriScore is " + NutriScore.getNutriScore(vegeSoup));
//        https://world.openfoodfacts.org/product/5010477348357/country-crisp-jordans
        FoodInfo cereal = new FoodInfo(469000., 17.5, 20.3, 2.6, 0.012, 6.1, 10.2,"general",2,"Cereal");
        System.out.println("[Fats] nutriscore.NutriScore is " + NutriScore.getNutriScore(cereal));
//        https://world.openfoodfacts.org/product/7622210449283/prince-chocolat-lu
        FoodInfo snack = new FoodInfo(465000., 32., 17., 5.6, 0.232,  4., 6.4,"general",1,"snack");
        System.out.println("[Beverage] nutriscore.NutriScore is " + NutriScore.getNutriScore(snack));
//        https://world.openfoodfacts.org/product/8000430138719/mozzarella-galbani
        FoodInfo cheese = new FoodInfo(236400., 1., 18., 12.5, 0.28, 0., 17.,"cheese",4,"cheese");
        System.out.println("[Cheese] nutriscore.NutriScore is " + NutriScore.getNutriScore(cheese));
//        https://world.openfoodfacts.org/product/5449000000996/coca-cola
        FoodInfo cola = new FoodInfo(42000., 10.6, 0., 0., 0., 0., 0.,"beverage",5,"cola");
        System.out.println("[FruitsAndVegetable] nutriscore.NutriScore is " + NutriScore.getNutriScore(cola));

        FoodInfo beefCan = new FoodInfo(102000.,2.36,3.15,1.38,236.,1.6,6.3,"general",3,"beefcan");
        FoodInfo fanta = new FoodInfo(145000.,8.5,0.,0.,0.,0.,0.,"beverage",2,"fanta");

        List<FoodInfo> foodInfoList = new ArrayList<>();
        foodInfoList.add(beefCan);
        foodInfoList.add(fanta);
        System.out.println("[BasketScore]" + BasketScore.getBasketScore(foodInfoList));
    }
}

