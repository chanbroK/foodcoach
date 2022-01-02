package com.example.myfoodcoach.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myfoodcoach.BarcodeInfo;
import com.example.myfoodcoach.BarcodeListViewAdapter;
import com.example.myfoodcoach.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class GetNutritionInfo extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;

    private String[] storagePermission;
    private ImageView cropImage;
    private ListView barcodeListview;
    private Button btn_cal_nutrition, btn_inter_gallery;
    private Uri crop_image_Uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_nutrition_info);
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        // SplashScreen.sharedPreferences = getSharedPreferences(SplashScreen.sharedPreferences, MODE_PRIVATE);
        barcodeListview = findViewById(R.id.lv_barcode_info);
        cropImage = findViewById(R.id.imageview_croped_image);

        if (checkStoragePermission()) {
            requestStoragePermission();
        } else {
            pickGallery();
        }

        btn_inter_gallery = findViewById(R.id.btn_inter_gallery);
        btn_inter_gallery.setOnClickListener(e -> {
            if (checkStoragePermission()) {
                requestStoragePermission();
            } else {
                pickGallery();
            }
        });

        btn_cal_nutrition = findViewById(R.id.btn_calc_nutrition);
        btn_cal_nutrition.setOnClickListener(v -> {
            Intent intent = new Intent(GetNutritionInfo.this, ShowNutriScore.class);
            BarcodeListViewAdapter tempAdapter = (BarcodeListViewAdapter) barcodeListview.getAdapter();
            intent.putExtra("barcodeList", convertBarcodeToUri(tempAdapter.getBarcodeArray()));
            intent.putExtra("quantityList", tempAdapter.getQuantityArray());
            intent.putExtra("cropImageUri", crop_image_Uri.toString());
            startActivity(intent);
        });

        getSupportActionBar().hide();
    }

    private String[] convertBarcodeToUri(String[] barcodeArray) {
        if (SplashScreen.sharedPreferences.getString("date", null) == null) {
            Toast.makeText(this, "App 실행 시 GCP 데이터가 없는 경우", Toast.LENGTH_LONG).show();
        }
        for (int i = 0; i < barcodeArray.length; i++) {
            barcodeArray[i] = getURI(barcodeArray[i]);
        }
        return barcodeArray;
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

    // handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (writeStorageAccepted) {
                    pickGallery();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        executeCropImage(requestCode, resultCode, data);
        setCropImage(requestCode, resultCode, data);

    }

    private void executeCropImage(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
    }

    private void setCropImage(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                crop_image_Uri = resultUri;
                cropImage.setImageURI(resultUri);
                LinearLayout layoutForBarcodeInfo = findViewById(R.id.lin_lay_barcode_info);
                layoutForBarcodeInfo.setVisibility(View.VISIBLE);

                // get drawable bitmap for text recognition
                BitmapDrawable bitmapDrawable = (BitmapDrawable) cropImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (recognizer.isOperational()) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);

                    // ArrayAdapter<itemListView>
                    BarcodeListViewAdapter adapter = new BarcodeListViewAdapter();
                    ArrayList<BarcodeInfo> itemList = adapter.getData();
                    setBarcodeList(itemList, items);
                    barcodeListview.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(barcodeListview);

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // if there is any error show it
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setBarcodeList(ArrayList<BarcodeInfo> itemList, SparseArray<TextBlock> items) {
        for (int i = 0; i < items.size(); i++) {
            // Convert TextBlock to String
            TextBlock myItem = items.valueAt(i);
            String strItem = myItem.getValue();

            String[] line = strItem.split("\n");

            for (int lineNum = 0; lineNum < line.length; lineNum++) {
                String[] splitLineOfInfo = line[lineNum].split(" |\n");

                // 바코드가 아닌 경우, ex). 01) 펩시콜라캔250ml
                // 바코드인 경우, [바코드] [가격] [개수] [가격]

                if (isBarcode(splitLineOfInfo[0])) {
                    itemList.add(getBarcodeInfoObj(splitLineOfInfo));
                }
            }
        }
    }

    private BarcodeInfo getBarcodeInfoObj(String[] splitLineOfInfo) {
        BarcodeInfo objBarcode = new BarcodeInfo("", 0);
        String cnt = null;

        // Set Barcode
        objBarcode.setBarcode(splitLineOfInfo[0]);

        // Set Quantity
        // [barcode] [price][cnt][total_price]
        if (splitLineOfInfo.length == 2) {
            // code...
        }
        // [barcode] [price] [cnt] [total_price]
        else if (splitLineOfInfo.length == 4) {
            cnt = splitLineOfInfo[2];
        } else if (splitLineOfInfo.length == 3) {
            int wordLength = splitLineOfInfo[1].length();
            // [barcode] [price] [cnt][total_price]
            // [price] == "..x,yze"
            // OR [price] == "900"
            if ((wordLength >= 4 && splitLineOfInfo[1].charAt(wordLength - 4) == ',')
                    || wordLength < 4) {
                Log.d("Barcode No.286", "price : " + splitLineOfInfo[1]);
                String strPrice = splitLineOfInfo[1].replaceAll(",", "");
                int price = Integer.parseInt(strPrice);
                int x, total;
                for (int size = 1; size <= 3; size++) {
                    String strCnt = splitLineOfInfo[2].substring(0, size);
                    String strTotal = splitLineOfInfo[2].substring(size + 1);
                    strTotal = strTotal.replaceAll(",", "");
                    x = Integer.parseInt(strCnt);
                    total = Integer.parseInt(strTotal);
                    Log.d("Barcode No.296", "cnt : " + strCnt + "total : " + strTotal);
                    if (price * x == total) {
                        cnt = strCnt;
                        break;
                    }
                }
            }
            // [barcode] [price][cnt] [total_price]
            // [price][cnt] == '...x,yzx[cnt]'
            // [price][cnt] == 'xyz[cnt]'
            else {
                String strPriceAndCnt = splitLineOfInfo[1];
                Log.d("Barcode No.309", "strPrice And Cnt : " + strPriceAndCnt);
                if (strPriceAndCnt.contains(",")) {
                    int pos = strPriceAndCnt.lastIndexOf(",");
                    Log.d("Barcode No.312", "strPrice And Cnt's position " + pos);
                    cnt = strPriceAndCnt.substring(pos + 4);
                } else {
                    cnt = strPriceAndCnt.substring(4);

                }

            }
        }

        if (cnt != null)
            objBarcode.setQuantity(Integer.parseInt(cnt));

        return objBarcode;
    }

    private boolean isNumeric(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isBarcode(String input) {
        if (input.length() == 13 && isNumeric(input))
            return true;
        return false;
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMinimumHeight();

            Log.d("LIST_HEIGHT", String.valueOf(listItem.getHeight()));
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight;
        listView.setLayoutParams(params);

        listView.requestLayout();
    }


}