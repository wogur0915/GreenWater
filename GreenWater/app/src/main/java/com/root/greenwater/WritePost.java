package com.root.greenwater;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;

public class WritePost extends BasicActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference("greenwater");
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    public static int postNum;
    public String postN;
    private EditText editTitle;
    private EditText editInner;
    private ImageView addImage;
    private Button confirmBtn;
    private ProgressBar progressBar;
    private Uri imageUri;
    private static final String ADDPOST = "addpost";
    private final int GET_GALLERY_IMAGE = 200;
    public static int pictureNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkSelfPermission();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // ???????????? ?????? ????????????

        //???????????? ????????? ??????
        editTitle = findViewById(R.id.editTitle);
        editInner = findViewById(R.id.editInner);
        confirmBtn = findViewById(R.id.submit_post);
        addImage = findViewById(R.id.addImage);
        progressBar = findViewById(R.id.progress_View);

        //?????????????????? ?????????
        progressBar.setVisibility(View.INVISIBLE);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("greenwater");
        getPostNum();

        //????????? ??????
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //????????? ????????? ??????
                String sTitle = editTitle.getText().toString();
                String sInner = editInner.getText().toString();

                if ((sTitle.equals("") || sInner.equals(""))) {
                    Toast.makeText(WritePost.this, "????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                } else {
                    //??? ??????
                    String sKey = mDatabaseRef.getKey();

                    //sKey??? null??? ????????? sKey????????? ???????????? ????????????.
                    if (sKey != null) {
                        postNum++;
                        postN = String.format("post_%d", postNum);
                        mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).child("PostingList").child(postN).child("Inner").setValue(sInner);
                        mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).child("PostingList").child(postN).child("Title").setValue(sTitle);
                        mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).child("PostingList").child("SavePostNum").setValue(postNum);
                    }
                    //????????? ???????????? ?????????
                    if (imageUri != null) {
                        pictureNum++;
                        uploadToFirebase(imageUri);
                    } else {
                        Toast.makeText(WritePost.this, "????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(WritePost.this, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WritePost.this, MainActivity.class);
                    intent.putExtra(ADDPOST, true);
                    finish();
                    startActivity(intent);
                }
            } //onClick
        });//setOnClickListener

        // ????????? ??????
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            addImage.setImageURI(imageUri);
        }

    }

    //?????????????????? ????????? ?????????
    private void uploadToFirebase(Uri uri) {
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "Post_Images/" + mFirebaseAuth.getUid() + "/"); //???????????? ????????? ??? ?????? ????????????
        //????????? ??? ?????? /toolbar_images????????? ????????????.
        //??? ???????????? ??? ??????????????? ????????? ??????
        if (!file.isDirectory()) { //??????????????? ?????????,
            file.mkdir(); //??????????????? ?????????.
        }

        String filename = "post_" + pictureNum;
        StorageReference fileRef = storageReference.child("Post_Images/" + mFirebaseAuth.getUid() + "/" + filename + ".jpg");

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //????????? ????????? ??????
                        Model model = new Model(uri.toString());

//                        //?????? ????????? ??????
//                        String modelId = root.push().getKey();

                        //????????? ??????
                        root.child("Post_Images/" + postNum).setValue(model);

                        //?????????????????? ??????
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(WritePost.this, "????????? ??????", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                //?????????????????? ????????????
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //?????????????????? ??????
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(WritePost.this, "????????? ??????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //???????????? ????????????
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void getPostNum() {
        mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).child("PostingList").child("SavePostNum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int getPNum = (int)dataSnapshot.getValue(Integer.class);
                    postNum = getPNum;
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkSelfPermission() {
        String temp = "";
        //?????? ?????? ?????? ??????
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += android.Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        //?????? ?????? ?????? ??????
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (TextUtils.isEmpty(temp) == false) {
            // ?????? ??????
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }else {
            // ?????? ?????? ??????
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

}
