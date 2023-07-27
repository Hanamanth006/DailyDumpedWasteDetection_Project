package com.example.takepic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {

    public static final int camera_perm_code = 101;
    public static final int cam_req_code = 101;
    public static final int gallery_req_code = 103;
    ImageView imageView;
    private Button takepic, uploadimage,clickhere;
    String currentPhotoPath;
    StorageReference storageReference;
    EditText Location;
    private DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private String location;
    private String users;
    private Uri locdata;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


       clickhere=(Button) findViewById(R.id.button3);
        imageView= findViewById(R.id.imageView);
        takepic=(Button) findViewById(R.id.button);
        uploadimage=findViewById(R.id.uploadimage);
        Location=findViewById(R.id.location);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference=FirebaseDatabase.getInstance().getReference("users");

        storageReference= FirebaseStorage.getInstance().getReference();
        //  storageReference.child(Location.getText().toString());
        // Location.setText(Location.getText().toString());
         clickhere.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i=new Intent(MainActivity2.this,second.class);
                 startActivity(i);
             }
         });
        takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                camera();


            }
        });

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, gallery_req_code);
            }
        });


    }

    private void camera() {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, camera_perm_code);
        }
        else {
            dispatchTakePictureIntent();

        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==camera_perm_code){
            if(grantResults.length<0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                dispatchTakePictureIntent();

            } else{
                Toast.makeText(this, "camera permission is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void opencamera() {
//
//        Intent cam=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cam, cam_req_code);
//    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==cam_req_code){
//            Bitmap image= (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(image);

            if(resultCode== Activity.RESULT_OK){
                File f= new File(currentPhotoPath);
                imageView.setImageURI(Uri.fromFile(f));
                Log.d("tag","Absolute url of image is "+ Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                //  File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                uploadimagetofirebase(f.getName(),contentUri);
            }
        }

        if (requestCode==gallery_req_code){
//            Bitmap image= (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(image);

            if(resultCode== Activity.RESULT_OK){
//                File f= new File(currentPhotoPath);
//                imageView.setImageURI(Uri.fromFile(f));
//                Log.d("tag","Absolute url of image is "+ Uri.fromFile(f));

                Uri contentUri =data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "."+getFileExt(contentUri);
                Log.d("tag","OnActivityResult: Gallery Image Uri "+ imageFileName);
                imageView.setImageURI(contentUri);
                uploadimagetofirebase(imageFileName,contentUri);



//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//              //  File f = new File(currentPhotoPath);
//                Uri contentUri = Uri.fromFile(f);
//                mediaScanIntent.setData(contentUri);
//                this.sendBroadcast(mediaScanIntent);
//
//                uploadimagetofirebase(f.getName(),contentUri);
            }
        }
    }

    private void uploadimagetofirebase(String imageFileName, Uri contentUri) {

        StorageReference reference= storageReference.child("location/"+location+"-"+System.currentTimeMillis()+".jpg");

        reference.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());

                Uri uri=uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity2.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String downloadurl) {
        String uniqukey=databaseReference.child("pdf").push().getKey();
        location=Location.getText().toString();

        HashMap data=new HashMap();
        data.put("location",location);
        data.put("imageURL",downloadurl);

        databaseReference.child("location").child(uniqukey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //  pd.dismiss();
                Toast.makeText(MainActivity2.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                Location.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // pd.dismiss();
                Toast.makeText(MainActivity2.this, "Failed to upload ", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private String getFileExt(Uri contentUri) {
        ContentResolver c=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

//    private void uploadimagetofirebase(String name, Uri contentUri) {
//        StorageReference image= storageReference.child("BBMP/"+name);
//        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Log.d("tag","onsuccess: Uploaded image url is"+uri.toString());
//
//
//                    }
//
//                });
//
//
//
//                Toast.makeText(MainActivity.this, "Image Uploaded successfully", Toast.LENGTH_SHORT).show();
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MainActivity.this, "upload failed", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        String uniqukey=databaseReference.child("BBMP").push().getKey();
//        location=Location.getText().toString();
//
//        HashMap data=new HashMap();
//        data.put("location",location);
//        databaseReference.child("BBMP").child(uniqukey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                Toast.makeText(MainActivity.this, "location updated successfully", Toast.LENGTH_SHORT).show();
//                Location.setText("");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Toast.makeText(MainActivity.this, "Failed to update location", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//
//
//
//    }

    // String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //     File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;


    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, cam_req_code);
            }
        }
    }


}