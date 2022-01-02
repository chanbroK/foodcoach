package com.example.myfoodcoach.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myfoodcoach.Activity.dataEnty.Category;
import com.example.myfoodcoach.R;
import com.example.myfoodcoach.Transform;
import com.example.myfoodcoach.logic.FoodInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NutritionDataProducerActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static String url;
    private Button trans_button, encodeButton, btn_inter_gallery;
    private TextView categoryTextView;
    private Integer nutriFactsVersion;
    private TextView servingTextView;
    private Category category; // 카테고리
    private EditText editText_id; //바코드
    private EditText editText_calories; // 칼로리
    private EditText editText_carbohydrateContent; // 탄수화물함량    private EditText editText_cholesterolContent;
    private EditText editText_cholesterolContent; // 콜레스테롤 함량
    private EditText editText_fatContent; // 지방함량
    private EditText editText_fiberContent; // 섬유함량
    private EditText editText_proteinContent; // 단백질 함량
    private EditText editText_saturatedFatContent; // 포화 지방 함량
    private EditText editText_standardAmount; // 서빙 사이즈 ->standardAmount
    private EditText editText_sodiumContent; // 나트륨 함량
    private EditText editText_sugarContent; // 설탕 함량
    private EditText editText_transFatContent; // 트랜스 지방 함량
    private EditText editText_unsaturatedFatContent; // 불포화 지방 함량
    private String XML;
    private String[] storagePermission;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_data_producer);
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        getSupportActionBar().hide();
        categoryTextView = findViewById(R.id.tv_categoryName);
        category = (Category) getIntent().getSerializableExtra("Category");
        nutriFactsVersion = (Integer) getIntent().getSerializableExtra("nutriFactsVersion"); //nutrifacts -> 0:조각당/ 1:100gram당 / 2: 총내용량
        //nutrifacts : 1:조각당/ 2:100gram당 / 3: 총내용량
        Toast.makeText(NutritionDataProducerActivity.this, category.toString() + " " + nutriFactsVersion, Toast.LENGTH_SHORT).show();
        servingTextView = findViewById(R.id.tv_servingsize);
        if (category.toString().equals("beverage"))
            servingTextView.setText(servingTextView.getText() + " (ml)");
        else
            servingTextView.setText(servingTextView.getText() + " (g)");
        categoryTextView.setText(category.toString());
        editText_id = findViewById(R.id.et_id);
        editText_calories = findViewById(R.id.et_calories);
        editText_carbohydrateContent = findViewById(R.id.et_carbohydrateContent);
        editText_cholesterolContent = findViewById(R.id.et_cholesterolContent);
        editText_fatContent = findViewById(R.id.et_fatContent);
        editText_fiberContent = findViewById(R.id.et_fiberContent);
        editText_proteinContent = findViewById(R.id.et_proteinContent);
        editText_saturatedFatContent = findViewById(R.id.et_saturatedfatContent);
        editText_standardAmount = findViewById(R.id.et_servingsize);
        editText_sodiumContent = findViewById(R.id.et_sodiumContent);
        editText_sugarContent = findViewById(R.id.et_sugarContent);
        editText_transFatContent = findViewById(R.id.et_transfatContent);
        editText_unsaturatedFatContent = findViewById(R.id.et_unsaturatedfatContent);
        trans_button = findViewById(R.id.bt_transform);
        encodeButton = findViewById(R.id.bt_uploadImage);

        if (nutriFactsVersion == 1)
            editText_standardAmount.setText("100");

        trans_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // 버튼을 클릭했을 때 edittext 에 빈곳이 없는것을 확인해야함
                // integer 인지 확인도 해야됨
                // xml 형식으로 바꿔줘야함 id,ingredient
                HashMap<String, String> Nutrients = new HashMap<>();
                Nutrients.put("category", category.toString());
                Nutrients.put("id", editText_id.getText().toString());
                Nutrients.put("calories", editText_calories.getText().toString());
                Nutrients.put("carbohydrateContent", editText_carbohydrateContent.getText().toString());
                Nutrients.put("cholesterolContent", editText_cholesterolContent.getText().toString());
                Nutrients.put("fatContent", editText_fatContent.getText().toString());
                Nutrients.put("fiberContent", editText_fiberContent.getText().toString());
                Nutrients.put("proteinContent", editText_proteinContent.getText().toString());
                Nutrients.put("saturatedFatContent", editText_saturatedFatContent.getText().toString());
                Nutrients.put("standardAmount", editText_standardAmount.getText().toString());
                Nutrients.put("sodiumContent", editText_sodiumContent.getText().toString());
                Nutrients.put("sugarContent", editText_sugarContent.getText().toString());
                Nutrients.put("transFatContent", editText_transFatContent.getText().toString());
                Nutrients.put("unsaturatedFatContent", editText_unsaturatedFatContent.getText().toString());
                FoodInfo foodInfo = new FoodInfo(Nutrients);
                foodInfo.setImage(image);
                // crwad sourcing 할 때는 카테고리와 영양성분 표를 사용자에게 직접 입력받아 그에 따른 계산법을 구현해야함
                // barcode 로 nutriscore 를 계산할 때는 영샹성분 표 필요 x , Category 만 사용해야함
                // 이것들이 숫자이거나 널이 아니면 실행

                XML = new Transform(foodInfo, nutriFactsVersion).getXML();
                if (true)
                    System.out.println(XML);
                POST_request(XML);
                //
            }
        });
        encodeButton.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                requestStoragePermission();
            } else {
                pickGallery();
            }
        });
    }


    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, CAMERA_REQUEST_CODE);
    }

    private void pickGallery() {
        // intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        // set intent type to image
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                image = Base64.encodeToString(bytes, Base64.DEFAULT);
                System.out.println("image = " + image);

            } catch (IOException e) {

            }
        }
    }

    private void POST_request(String XML) {
        RequestQueue queue = Volley.newRequestQueue(this);
        url = "http://dfpl.sejong.ac.kr:8080/epcis/capture";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "response" + response);
                Toast.makeText(NutritionDataProducerActivity.this, "200OK", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                    startActivity(new Intent(NutritionDataProducerActivity.this, MainActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "response" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/xml");
                headers.put("Content-Type", "application/xml");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String httpPostBody = XML;
                return httpPostBody.getBytes();
            }
        };
        queue.add(stringRequest);

    }

}