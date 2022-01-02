package com.example.myfoodcoach;


import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.myfoodcoach.Activity.SplashScreen;
import com.example.myfoodcoach.logic.FoodInfo;

public class Transform {

    //Category 와 Name은 extend field 에 들어가야함
    // StandardAmount 는 입력 받을 떄 StandardAmount 로, 저장할때는 ServingSize 로 저장해야함
    static private final String attribute_lastUpdate = "<attribute id=\"lastUpdate\">";
    static private String headerHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE project>\n" +
            "<epcis:EPCISDocument schemaVersion=\"2.0\"\n" +
            "\txmlns:xsd=\"https://www.w3.org/2001/XMLSchema\"\n" +
            "\txmlns:xsi=\"https://www.w3.org/2001/XMLSchema-instance\"\n" +
            "\txmlns:ext1=\"https://ext.com/ext1\" \n" +
            "\tcreationDate=\"2013-06-04T14:59:02.099+02:00\"\n" +
            "\txmlns:epcis=\"urn:epcglobal:epcis:xsd:2\">\n" +
            "    <EPCISHeader>\n" +
            "        <extension>\n" +
            "            <EPCISMasterData>\n" +
            "                <VocabularyList>\n" +
            "                    <Vocabulary\n" +
            "\t\t\t\t\t\ttype=\"urn:epcglobal:epcis:vtype:BusinessLocation\">\n" +
            "                        <VocabularyElementList>";
    static private String headerTail = "</VocabularyElement>\n" +
            "                        </VocabularyElementList>\n" +
            "                    </Vocabulary>\n" +
            "                </VocabularyList>\n" +
            "            </EPCISMasterData>\n" +
            "        </extension>\n" +
            "    </EPCISHeader>\n" +
            "    <EPCISBody>\n" +
            "    </EPCISBody>\n" +
            "</epcis:EPCISDocument>";
    static private String extensionCategoryHead = "<attribute id = \"http:ext.com/ext1#category\">\n" +
            "<ext1:string xsi:type = \"xsd:string\">";
    static private String extensionTail = "</ext1:string>";
    static private String vocabularyElement_id = "<VocabularyElement id=\"";
    static private String attribute_Calories = "<attribute id=\"https://schema.org/NutritionInformation#calories\">";
    static private String attribute_CarbohydrateContent = "<attribute id=\"https://schema.org/NutritionInformation#carbohydrateContent\">";
    static private String attribute_CholesterolContent = "<attribute id=\"https://schema.org/NutritionInformation#cholesterolContent\">";
    static private String attribute_FatContent = "<attribute id=\"https://schema.org/NutritionInformation#fatContent\">";
    static private String attribute_FiberContent = "<attribute id=\"https://schema.org/NutritionInformation#fiberContent\">";
    static private String attribute_ProteinContent = "<attribute id=\"https://schema.org/NutritionInformation#proteinContent\">";
    static private String attribute_SaturatedFatContent = "<attribute id=\"https://schema.org/NutritionInformation#saturatedFatContent\">";
    static private String attribute_ServingSize = "<attribute id=\"https://schema.org/NutritionInformation#servingSize\">";
    static private String attribute_SodiumContent = "<attribute id=\"https://schema.org/NutritionInformation#sodiumContent\">";
    static private String attribute_SugarContent = "<attribute id=\"https://schema.org/NutritionInformation#sugarContent\">";
    static private String attribute_TransFatContent = "<attribute id=\"https://schema.org/NutritionInformation#transFatContent\">";
    static private String attribute_UnsaturatedFatContent = "<attribute id=\"https://schema.org/NutritionInformation#unsaturatedFatContent\">";
    static private String attribute_imageContent = "<attribute id=\"https://schema.org/NutritionInformation#image\">";
    static private String attributeTail = "</attribute>";
    static private String vocabularyElementTail = "\">";
    private String Id = "";
    private String Calories = "";
    private String FiberContent = "";
    private String UnsaturatedFatContent = "";
    private String CarbohydrateContent = "";
    private String CholesterolContent = "";
    private String FatContent = "";
    private String ProteinContent = "";
    private String SaturatedFatContent = "";
    private String ServingSize = "";
    private String SodiumContent = "";
    private String SugarContent = "";
    private String TransFatContent = "";
    private String XML = "";
    private String category = "";
    private String imageContent = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Transform(FoodInfo foodInfo, Integer nutriVersion) {
        foodInfo = this.transformToGram(foodInfo, nutriVersion);
        Id = getURI(foodInfo.id.toString());
        Calories = String.valueOf(foodInfo.energyVal * 1000);
        CarbohydrateContent = foodInfo.carbohydrateVal.toString();
        CholesterolContent = foodInfo.cholesterolVal.toString();
        FiberContent = foodInfo.fiberVal.toString();
        UnsaturatedFatContent = foodInfo.unsaturatedFatVal.toString();
        FatContent = foodInfo.fatVal.toString();
        ProteinContent = foodInfo.proteinVal.toString();
        SaturatedFatContent = foodInfo.saturatedFatVal.toString();
        ServingSize = foodInfo.servingSize.toString();
        SodiumContent = foodInfo.sodiumVal.toString();
        SugarContent = foodInfo.sugarVal.toString();
        TransFatContent = foodInfo.transFatVal.toString();
        imageContent = foodInfo.image;
        category = foodInfo.category;
        XML = headerHead + "\n" + vocabularyElement_id + Id + vocabularyElementTail + "\n"
                + attribute_Calories + Calories + attributeTail + "\n"
                + attribute_CarbohydrateContent + CarbohydrateContent + attributeTail + "\n"
                + attribute_CholesterolContent + CholesterolContent + attributeTail + "\n"
                + attribute_FatContent + FatContent + attributeTail + "\n"
                + attribute_FiberContent + FiberContent + attributeTail + "\n"
                + attribute_ProteinContent + ProteinContent + attributeTail + "\n"
                + attribute_SaturatedFatContent + SaturatedFatContent + attributeTail + "\n"
                + attribute_ServingSize + ServingSize + attributeTail + "\n"
                + attribute_SodiumContent + SodiumContent + attributeTail + "\n"
                + attribute_SugarContent + SugarContent + attributeTail + "\n"
                + attribute_TransFatContent + TransFatContent + attributeTail + "\n"
                + attribute_UnsaturatedFatContent + UnsaturatedFatContent + attributeTail + "\n"
                + attribute_imageContent + imageContent + attributeTail + "\n"
                + extensionCategoryHead + category + extensionTail + attributeTail + "\n"
                + headerTail;
    }

    public String getXML() {
        return XML;
    }

    private String getURI(String barcode) {
        int gcpLength = getGcpLength(barcode);
        if (gcpLength == -1)
            return "No Data";

        String uri = "urn:epc:id:sgtin:";
        String companyPrefix = barcode.substring(0, gcpLength);
        String itemRefAndIndicator = barcode.substring(gcpLength, barcode.length() - 1);
        uri += companyPrefix + ".0" + itemRefAndIndicator + ".*";

        return uri;
    }

    private int getGcpLength(String barcode) {
        String prefix = barcode;
        int gcpLength = -1;
        while (prefix.length() > 0) {
            gcpLength = SplashScreen.sharedPreferences.getInt(prefix, -1);
            if (gcpLength == -1) {
                prefix = prefix.substring(0, prefix.length() - 1);
                continue;
            } else if (gcpLength > 0)
                break;
        }
        return gcpLength;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public FoodInfo transformToGram(FoodInfo inputdata, Integer nutriVersion) {
        //nutrifacts -> 0:조각당/ 1:100gram당 / 2: 총내용량
        if (nutriVersion == 0 || nutriVersion == 2) { // servingsize 를 100으로 환산 한 뒤, 계산해야됨
            inputdata.transform();
            return inputdata;
        } else {
            return inputdata;
        }
    }
}
