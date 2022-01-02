package com.example.myfoodcoach.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodcoach.Activity.dataEnty.Category;
import com.example.myfoodcoach.R;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton generalButton, fruitVegetableButton, cheeseButton, beverageButton, fatButton, waterButton, nutriFactsButton1, nutriFactsButton2, nutriFactsButton3;
    private ArrayList<RadioButton> buttons = new ArrayList<>();
    private Button nextButton;
    private Category category;
    private Integer nutriFactsVersion;

    //nutrifacts : 1:조각당/ 2:100gram당 / 3: 총내용량
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().hide();
        category = null;
        nutriFactsVersion = null;
        radioGroup = findViewById(R.id.radioGroup1);
        generalButton = findViewById(R.id.radioGroup1_GButton);
        fruitVegetableButton = findViewById(R.id.radioGroup1_FVButton);
        cheeseButton = findViewById(R.id.radioGroup1_CButton);
        beverageButton = findViewById(R.id.radioGroup1_BButton);
        fatButton = findViewById(R.id.radioGroup1_FButton);
        waterButton = findViewById(R.id.radioGroup1_WButton);
        nextButton = findViewById(R.id.next_button);
        nutriFactsButton1 = findViewById(R.id.nutriFactsVersionRadioButton1);
        nutriFactsButton2 = findViewById(R.id.nutriFactsVersionRadioButton2);
        nutriFactsButton3 = findViewById(R.id.nutriFactsVersionRadioButton3);
        nextButton.setOnClickListener(l -> {
            Intent intent = new Intent(CategoryActivity.this, NutritionDataProducerActivity.class);
            if (generalButton.isChecked())
                category = Category.General;
            else if (fruitVegetableButton.isChecked())
                category = Category.FruitVegetable;
            else if (cheeseButton.isChecked())
                category = Category.Cheese;
            else if (fatButton.isChecked())
                category = Category.Fat;
            else if (beverageButton.isChecked())
                category = Category.Beverage;
            else if (waterButton.isChecked()) {
                category = Category.Water;
            }
            // 아무것도 체크 안되었을 때 예외처리 해줘야 함

            if (nutriFactsButton1.isChecked())
                nutriFactsVersion = 0;
            else if (nutriFactsButton2.isChecked())
                nutriFactsVersion = 1;
            else if (nutriFactsButton3.isChecked())
                nutriFactsVersion = 2;

            if (category == null || nutriFactsVersion == null) {
                System.out.println(category);
                //toast 메시지 뜨지 않음
                Toast.makeText(CategoryActivity.this, "please check the button", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("category.toString() = " + category.toString());
                intent.putExtra("Category", category);
                intent.putExtra("nutriFactsVersion", nutriFactsVersion);
                startActivity(intent);
            }
        });


    }
}