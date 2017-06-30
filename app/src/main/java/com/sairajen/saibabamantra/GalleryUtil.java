package com.sairajen.saibabamantra;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

/**
 * @author Gmonetix
 */

public class GalleryUtil extends Activity {

    private final static int RESULT_SELECTED_IMAGE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = "GalleryUtil";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i,RESULT_SELECTED_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SELECTED_IMAGE:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    try{
                        Uri selectedImage = data.getData();
                        String[]  filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null,null,null);
                        cursor.moveToFirst();
                        int ColumnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath =  cursor.getString(ColumnIndex);
                        cursor.close();

                        Intent returnFromGallery = new Intent();
                        returnFromGallery.putExtra("picturePath",picturePath);
                        setResult(RESULT_OK,returnFromGallery);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent returnFromGallery = new Intent();
                        setResult(RESULT_CANCELED,returnFromGallery);
                        finish();
                    }
                } else {
                    Intent returnFromGallery = new Intent();
                    setResult(RESULT_CANCELED,returnFromGallery);
                    finish();
                }
                break;
        }

    }
}
