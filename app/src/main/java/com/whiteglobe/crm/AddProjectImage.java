package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProjectImage extends AppCompatActivity {

    SharedPreferences sessionAddProjectImages;

    AppCompatEditText edtProjectImageRemarks;
    AppCompatImageView imageViewProjectImage;
    AppCompatButton btnChooseProjectImage,btnAddProjectImage;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_image);
        getSupportActionBar().hide();

        edtProjectImageRemarks = findViewById(R.id.edtProjectImageRemarks);
        imageViewProjectImage = findViewById(R.id.imageViewProjectImage);
        btnChooseProjectImage = findViewById(R.id.btnChooseProjectImage);
        btnAddProjectImage = findViewById(R.id.btnAddProjectImage);

        sessionAddProjectImages = getSharedPreferences("user_details",MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        btnChooseProjectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, 100);
            }
        });

        btnAddProjectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtProjectImageRemarks.getText().toString().equals(""))
                {
                    edtProjectImageRemarks.setError("Please Enter Remarks.");
                }
                else if(imageViewProjectImage.getDrawable() == null)
                {
                    Toast.makeText(getApplicationContext(),"Please Select Image.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    uploadProjectImage(imageBitmap,getIntent().getStringExtra("projectunique"),sessionAddProjectImages.getString("uname",null));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                imageBitmap = bitmap;

                //displaying selected image to imageview
                imageViewProjectImage.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                //uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadProjectImage(final Bitmap bitmap, final String projectUnique, final String username) {

        //getting the tag from the edittext
        final String remarks = edtProjectImageRemarks.getText().toString().trim();
        String url = WebName.weburl+"addprojectimage.php";
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("Response From Server",new String(response.data));
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if(obj.getInt("success") == 1)
                            {
                                Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else if(obj.getInt("success") == 0)
                            {
                                Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("remarks", remarks);
                params.put("username", username);
                params.put("projectUnique", projectUnique);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
