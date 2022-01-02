package com.example.myfoodcoach.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodcoach.R;


// TODO 데이터 입력받을때, 카테고리 이름을 모두 소문자로 처리할 수 있도록 수정(OK)
// TODO 데이터 입력받을때, water 라는 카테고리 추가 (OK)
// TODO 데이터 저장할때, 입력받은 kcal -> cal 로 저장(OK)
// TODO 데이터 저장할때, xml 스키마 수정 (http: -> http://)(OK)
// TODO 데이터 불러올때, VocabularyElement id 값 수정 (idpat -> id) (OK)
// TODO 데이터 불러올때, productName도 같이 Map에 저장하도록 수정 (참고, FoodInfo라는 클래스가 Map을 대체 하는건 어떤지)
// TODO update UI

public class MainActivity extends AppCompatActivity {

    private Button btn_get_nutrition_info;
    private Button btn_input_nutrition_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        btn_get_nutrition_info = findViewById(R.id.btn_get_nutrition_info);
        btn_get_nutrition_info.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GetNutritionInfo.class);
            startActivity(intent);
        });

        btn_input_nutrition_info = findViewById(R.id.btn_input_nutrition_info);
        btn_input_nutrition_info.setOnClickListener(l -> {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);
        });


    }
}