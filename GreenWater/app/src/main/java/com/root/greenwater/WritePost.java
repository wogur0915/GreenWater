package com.root.greenwater;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

public class WritePost extends BasicActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    public static int postNum;
    public String postN;
    private static final String ADDPOST = "addpost";
    private final int GET_GALLERY_IMAGE = 200;

    ImageView addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkSelfPermission();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        //컴포넌트 변수에 담기
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editInner = findViewById(R.id.editInner);
        Button confirmBtn = findViewById(R.id.submit_post);

        addImage = findViewById(R.id.addImage);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("greenwater");
        getPostNum();

        //데이터 등록
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //입력값 변수에 담기
                String sTitle = editTitle.getText().toString();
                String sInner = editInner.getText().toString();

                //키 생성
                String sKey = mDatabaseRef.getKey();

                //sKey가 null이 아니면 sKey값으로 데이터를 저장한다.
                if(sKey != null){
                    postNum++;
                    postN = String.format("post_%d", postNum);
                    mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).child("PostingList").child(postN).child("Inner").setValue(sInner);
                    mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).child("PostingList").child(postN).child("Title").setValue(sTitle);
                    mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).child("PostingList").child("SavePostNum").setValue(postNum);
                }
                Toast.makeText(WritePost.this, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WritePost.this, MainActivity.class);
                intent.putExtra(ADDPOST, true);
                finish();
                startActivity(intent);
            } //onClick
        });//setOnClickListener

        // 갤러리 열기
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

            Uri selectedImageUri = data.getData();
            addImage.setImageURI(selectedImageUri);

        }

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
        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += android.Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }else {
            // 모두 허용 상태
            Toast.makeText(this, "권한이 허용 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

}
