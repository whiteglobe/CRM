package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class AddExhibition extends AppCompatActivity {

    AppCompatButton btnChooseExhibitionImage1,btnAddExhibitionDetails;
    AppCompatImageView imageViewExhibitionImageFront,imageViewExhibitionImageBack;
    AppCompatEditText edtProjectExhibitionPartyName,edtProjectExhibitionPhoneNo,edtProjectExhibitionRemarks;

    SharedPreferences sessionExhibition;

    Bitmap imageBitmap1,imageBitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exhibition);

        sessionExhibition = getSharedPreferences("user_details",MODE_PRIVATE);

        btnChooseExhibitionImage1 = findViewById(R.id.btnChooseExhibitionImage1);
        btnAddExhibitionDetails = findViewById(R.id.btnAddExhibitionDetails);
        imageViewExhibitionImageFront = findViewById(R.id.imageViewExhibitionImageFront);
        imageViewExhibitionImageBack = findViewById(R.id.imageViewExhibitionImageBack);
        edtProjectExhibitionPartyName = findViewById(R.id.edtProjectExhibitionPartyName);
        edtProjectExhibitionPhoneNo = findViewById(R.id.edtProjectExhibitionPhoneNo);
        edtProjectExhibitionRemarks = findViewById(R.id.edtProjectExhibitionRemarks);

        btnChooseExhibitionImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 100);
            }
        });

        btnAddExhibitionDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtProjectExhibitionPartyName.getText().toString().equals(""))
                {
                    edtProjectExhibitionPartyName.setError("Please Enter Party Name.");
                }
                else if(edtProjectExhibitionPhoneNo.getText().toString().equals(""))
                {
                    edtProjectExhibitionPhoneNo.setError("Please Enter Phone Number.");
                }
                else if(edtProjectExhibitionRemarks.getText().toString().equals(""))
                {
                    edtProjectExhibitionRemarks.setError("Please Enter Remarks.");
                }
                else if(imageViewExhibitionImageFront.getDrawable() == null)
                {
                    Toast.makeText(getApplicationContext(),"Please Select Front & Back Image of Visiting Card.",Toast.LENGTH_LONG).show();
                }
                else if(imageViewExhibitionImageBack.getDrawable() == null)
                {
                    Toast.makeText(getApplicationContext(),"Please Select Front & Back Image of Visiting Card.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    uploadExhibitionData(imageBitmap1,imageBitmap2,sessionExhibition.getString("uname",null),edtProjectExhibitionPartyName.getText().toString().trim(),edtProjectExhibitionPhoneNo.getText().toString().trim(),edtProjectExhibitionRemarks.getText().toString().trim());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri1 = data.getClipData().getItemAt(0).getUri();
            Uri imageUri2 = data.getClipData().getItemAt(1).getUri();
            try {
                //getting bitmap object from uri
                Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri1);
                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri2);

                imageBitmap1 = bitmap1;
                imageBitmap2 = bitmap2;

                //displaying selected image to imageview
                imageViewExhibitionImageFront.setImageBitmap(bitmap1);
                imageViewExhibitionImageBack.setImageBitmap(bitmap2);

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

    private void uploadExhibitionData(final Bitmap bitmap1, final Bitmap bitmap2, final String username, final String partyName, final String phNo, final String remarks) {

         String url = WebName.weburl+"addexhibition.php";
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
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
                params.put("partyName", partyName);
                params.put("phNo", phNo);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename1 = System.currentTimeMillis();
                long imagename2 = System.currentTimeMillis() + 120;
                params.put("pic1", new DataPart(imagename1 + ".png", getFileDataFromDrawable(bitmap1)));
                params.put("pic2", new DataPart(imagename2 + ".png", getFileDataFromDrawable(bitmap2)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
