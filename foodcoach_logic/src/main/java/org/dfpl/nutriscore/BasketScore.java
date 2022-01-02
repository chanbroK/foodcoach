package org.dfpl.nutriscore;

import java.util.List;

public class BasketScore {
    public static Double getBasketScore(List<FoodInfo> foodInfoList){
        Double denominator = 0. ; // 분모
        Double numerator = 0.; // 분자
        for(FoodInfo foodInfo : foodInfoList){
            Integer nutriScore = NutriScore.getNutriScore(foodInfo);
            numerator += (nutriScore * foodInfo.energyVal * foodInfo.quantity);
            denominator += (foodInfo.energyVal * foodInfo.quantity);
        }
        if(denominator.equals(0)) return 0.;
        return numerator / denominator;
    }
}
