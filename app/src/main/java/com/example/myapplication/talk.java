package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class talk extends AppCompatActivity {

    Button btn;
    ImageView iv1;
    TextView tv;
    Uri imageUri;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        btn = (Button)findViewById(R.id.btn);
        iv1 = (ImageView)findViewById(R.id.imageView);
        tv = (TextView) findViewById(R.id.textView);

        if (! Python.isStarted())
            Python.start(new AndroidPlatform(this));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
            }
        }
        );
    }

    @Override
    public  void onBackPressed()
    {
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                iv1.setImageBitmap(selectedImage);
                f(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void f(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        String encoded = Base64.encodeToString(b, Base64.DEFAULT);

        final Python py = Python.getInstance();
        PyObject pyo = py.getModule("style");
        PyObject obj = pyo.callAttr("main",encoded);
        String str = obj.toString();
        tv.setText(str);
    }

    public static class welcome {
    }
}