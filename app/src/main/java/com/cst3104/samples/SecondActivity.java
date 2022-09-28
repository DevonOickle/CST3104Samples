package com.cst3104.samples;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.ImageView;




public class SecondActivity extends AppCompatActivity {

    public final static String TAG ="SecondActivity";

    ImageView imgv;

    ActivityResultLauncher<Intent> myPictureTakerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            ,new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {

                        try {
                            Intent data = result.getData();

                            Bitmap imgbitmap = (Bitmap) data.getExtras().get("data");
                            imgv.setImageBitmap(imgbitmap);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                            Log.w(TAG, "Can't output PNG");
                        }
                    }
                    else if(result.getResultCode() == Activity.RESULT_CANCELED)
                        Log.i(TAG, "User refused to capture a picture.");
                }
            } );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        //if "USERINPUT" is not found, return null
        String input = fromPrevious.getStringExtra(FirstActivity.USER_INPUT_KEY);

        Log.i(TAG, " Reading " + input );

        //save data from first page:
        SharedPreferences prefs = getSharedPreferences(FirstActivity.PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor writer = prefs.edit();
        Log.i(TAG, " Saving " + input );
        writer.putString(FirstActivity.USER_INPUT_KEY, input);

        writer.apply(); //save to disk

        Button cam = findViewById( R.id.start_camera);
        cam.setOnClickListener(view -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                myPictureTakerLauncher.launch(takePictureIntent);
            }
        });

        //Picture container
        imgv = findViewById( R.id.picture);

        Button term = findViewById( R.id.intent_return_button);
        term.setOnClickListener(  click ->  { finish(); } );
    }

}
