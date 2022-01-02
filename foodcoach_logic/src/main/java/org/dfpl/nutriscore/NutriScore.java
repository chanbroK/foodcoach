package org.dfpl.nutriscore;

public class NutriScore {
    private static final Integer[] energyScoreInfo = {3350, 3015, 2680, 2345, 2010, 1675, 1340, 1005, 670, 335};
    private static final Double[] sugarScoreInfo = {45., 40., 36., 31., 27., 22.5, 18., 13.5, 9., 4.5};
    private static final Integer[] saturatedFatScoreInfo = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    private static final Integer[] sodiumScoreInfo = {900, 810, 720, 630, 540, 450, 360, 270, 180, 90};

    public static Integer getNutriScore(FoodInfo foodInfo) {
//        Double energyVal, Double sugarVal, Double fatVal,Double saturatedFatVal, Double sodiumVal, Double fibreVal, Double proteinVal, String category
        //입력되는 모든 값이 100g 당 영양성분을 기준으로, g 혹은 ml 단위로 입력되었다고 가정
        Double energyVal = foodInfo.energyVal;
        Double sugarVal = foodInfo.sugarVal;
        Double fatVal = foodInfo.fatVal;
        Double saturatedFatVal = foodInfo.saturatedFatVal;
        Double sodiumVal= foodInfo.sodiumVal;
        Double fiberVal= foodInfo.fiberVal;
        Double proteinVal= foodInfo.proteinVal;
        String category= foodInfo.category;
        boolean isBeverage = category.equals("beverage");
        boolean isCheese = category.equals("cheese");
        boolean isFruitAndVegetable = category.equals("fruit/vegetable");
        boolean isFat = category.equals("fat");
        boolean isWater = category.equals("water");
        if(isWater){
            // 무조건 A score
            return 0;
        }

        int pointsA = getPointA(energyVal, sugarVal, fatVal,saturatedFatVal, sodiumVal, isBeverage,isFat);
        System.out.println("PointA:" + pointsA);
        int pointsC = getPointC( fiberVal, proteinVal, isBeverage, isFruitAndVegetable);
        System.out.println("PointC:" + pointsC);

        if (pointsA < 11 || isCheese) {
            return pointsA - pointsC;
        } else {
            if (getFruitAndVegetablesScore( isBeverage, isFruitAndVegetable).equals(5)) {
                return pointsA - pointsC;
            } else if (getFruitAndVegetablesScore( isBeverage, isFruitAndVegetable) < 5) {
                int pointC_1 = getFiberScore(fiberVal);
                int pointC_2 = getFruitAndVegetablesScore( isBeverage, isFruitAndVegetable);
                return pointsA - (pointC_1 + pointC_2);
            }
        }
        // return -1 -> calculation error
        return -1;
    }

    private static Integer getPointA(Double energyVal, Double sugarVal, Double fatVal, Double saturatedFatVal, Double sodiumVal, boolean isBeverage, boolean isFat) {
        Integer a = getEnergyScore(energyVal, isBeverage);
        Integer b = getSugarScore(sugarVal, isBeverage);
        Integer c = getSaturatedFatScore(fatVal,saturatedFatVal,isFat);
        Integer d = getSodiumScore(sodiumVal);
        System.out.println("EnergeScore:" + a);
        System.out.println("SugarScore:" + b);
        System.out.println("SaturatedFatScore:" + c);
        System.out.println("SodiumScore:" + d);

        return a + b + c + d;
    }

    private static Integer getPointC(Double fiberVal, Double proteinVal, boolean isBeverage, boolean isFruitAndVegetable) {
        Integer a = getFruitAndVegetablesScore( isBeverage, isFruitAndVegetable);
        Integer b = getFiberScore(fiberVal);
        Integer c = getProteinScore(proteinVal);
        System.out.println("FruitAndVegetableScore:" + a);
        System.out.println("ProteinScore:" + b);
        System.out.println("ProteinScore:" + c);
        return a + b + c;
    }

    private static Integer getSodiumScore(Double val) {
        Integer len = sodiumScoreInfo.length;
        Integer score = 10;
        val = val * 1000;
        for (int i = 0; i < len; i++) {
            if (val > sodiumScoreInfo[i]) {
                break;
            }
            score--;
        }
        return score;
    }

    private static Integer getSaturatedFatScore(Double fatVal, Double saturatedFatVal, boolean isFat) {
        Integer len = saturatedFatScoreInfo.length;
        Integer score = 10;
        Double percentage = saturatedFatVal / fatVal * 100;
        if (isFat) {
            //is Fats
            if(percentage >= 64){
                return 10;
            }else if(percentage >= 58){
                return 9;
            }else if(percentage >= 52){
                return 8;
            }else if(percentage >= 46){
                return 7;
            }else if(percentage >= 40){
                return 6;
            }else if(percentage >= 34){
                return 5;
            }else if(percentage >= 28){
                return 4;
            }else if(percentage >= 22){
                return 3;
            }else if(percentage >= 16){
                return 2;
            }else if(percentage >= 10){
                return 1;
            }else {
                return 0;
            }
        } else {
            //not Fat
            for (int i = 0; i < len; i++) {
                if (saturatedFatVal > saturatedFatScoreInfo[i]) {
                    break;
                }
                score--;
            }
            return score;
        }
    }

    private static Integer getSugarScore(Double val, boolean isBeverage) {
        Integer len = sugarScoreInfo.length;
        Integer score = 10;
        if (isBeverage) {
            //is beverage
            if (val > 13.5) {
                return 10;
            } else if (val > 12) {
                return 9;
            } else if (val > 10.5) {
                return 8;
            } else if (val > 9) {
                return 7;
            } else if (val > 7.5) {
                return 6;
            } else if (val > 6) {
                return 5;
            } else if (val > 4.5) {
                return 4;
            } else if (val > 3) {
                return 3;
            } else if (val > 1.5) {
                return 2;
            } else if (val > 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            // not beverage
            for (int i = 0; i < len; i++) {
                if (val > sugarScoreInfo[i]) {
                    break;
                }
                score--;
            }
            return score;
        }
    }

    private static Integer getEnergyScore(Double val, boolean isBeverage) {
        Integer len = energyScoreInfo.length;
        Integer score = 10;
        //convert cal to kJ
        Double valKj = val / 1000 * 4.184;
        System.out.println(valKj);
        if (isBeverage) {
            // is beverage
            if (valKj > 270) {
                return 10;
            } else if (valKj > 240) {
                return 9;
            } else if (valKj > 210) {
                return 8;
            } else if (valKj > 180) {
                return 7;
            } else if (valKj > 150) {
                return 6;
            } else if (valKj > 120) {
                return 5;
            } else if (valKj > 90) {
                return 4;
            } else if (valKj > 60) {
                return 3;
            } else if (valKj > 30) {
                return 2;
            } else if (valKj > 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            //not beverage
            for (int i = 0; i < len; i++) {
                if (valKj > energyScoreInfo[i]) {
                    break;
                }
                score--;
            }
            return score;
        }
    }

    private static Integer getFruitAndVegetablesScore(boolean isBeverage, boolean isFruitAndVegetable) {
        if (isFruitAndVegetable) {
            // is fruit and vegetable
            if (isBeverage) {
                return 10;
            } else {
                return 5;
            }
        } else {
            //not fruit and vegetable
//            if (val > 80) {
//                return 5;
//            } else if (val > 60) {
//                return 2;
//            } else if (val > 40) {
//                return 1;
//            } else {
//                return 0;
//            }
            return 0;
        }
    }

    private static Integer getFiberScore(Double val) {
        if (val > 3.5) {
            return 5;
        } else if (val > 2.8) {
            return 4;
        } else if (val > 2.1) {
            return 3;
        } else if (val > 1.4) {
            return 2;
        } else if (val > 0.7) {
            return 1;
        } else {
            return 0;
        }
    }

    private static Integer getProteinScore(Double val) {
        if (val > 8.0) {
            return 5;
        } else if (val > 6.4) {
            return 4;
        } else if (val > 4.8) {
            return 3;
        } else if (val > 3.2) {
            return 2;
        } else if (val > 1.6) {
            return 1;
        } else {
            return 0;
        }
    }
}
