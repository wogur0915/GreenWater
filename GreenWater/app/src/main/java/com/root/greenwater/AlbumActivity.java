package com.root.greenwater;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class AlbumActivity extends AppCompatActivity {

    private ImageView ivImage1;
    private ImageView ivImage2;
    private ImageView ivImage3;
    private ImageView ivImage4;
    private ImageView ivImage5;
    private ImageView ivImage6;
    private String photoUrl1;
    private String photoUrl2;
    private FirebaseAuth mFirebaseAuth;
    private Button btn_closealbum;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        ivImage1 = findViewById(R.id.albumImage1);
        ivImage2 = findViewById(R.id.albumImage2);
        ivImage3 = findViewById(R.id.albumImage3);
        ivImage4 = findViewById(R.id.albumImage4);
        ivImage5 = findViewById(R.id.albumImage5);
        ivImage6 = findViewById(R.id.albumImage6);
        btn_closealbum = findViewById(R.id.btn_closealbum);

        mFirebaseAuth = FirebaseAuth.getInstance();
        albumClear();
        btn_closealbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void loading() {
        // 이미지 url 구성
        photoUrl1 = "https://firebasestorage.googleapis.com/v0/b/greenwater-5c393.appspot.com/o/Post_Images%2F";
        String userId = mFirebaseAuth.getUid();
        photoUrl2 = "?alt=media&token";

        // Glide로 이미지 표시하기
        String imageUrl = photoUrl1 + userId + "%2Fpost_1.jpg" + photoUrl2;
        Glide.with(this).load(imageUrl).into(ivImage1);

        String imageUrl2 = photoUrl1 + userId + "%2Fpost_2.jpg" + photoUrl2;;
        Glide.with(this).load(imageUrl2).into(ivImage2);

        String imageUrl3 = photoUrl1 + userId + "%2Fpost_3.jpg" + photoUrl2;;
        Glide.with(this).load(imageUrl3).into(ivImage3);

        String imageUrl4 = photoUrl1 + userId + "%2Fpost_4.jpg" + photoUrl2;;
        Glide.with(this).load(imageUrl4).into(ivImage4);

        String imageUrl5 = photoUrl1 + userId + "%2Fpost_5.jpg" + photoUrl2;;
        Glide.with(this).load(imageUrl5).into(ivImage5);

        String imageUrl6 = photoUrl1 + userId + "%2Fpost_6.jpg" + photoUrl2;;
        Glide.with(this).load(imageUrl6).into(ivImage6);

    }

    private void albumClear() {
        String imageUrl = "";
        Glide.with(this).load(imageUrl).into(ivImage1);
        Glide.with(this).load(imageUrl).into(ivImage2);
        Glide.with(this).load(imageUrl).into(ivImage3);
        Glide.with(this).load(imageUrl).into(ivImage4);
        Glide.with(this).load(imageUrl).into(ivImage5);
        Glide.with(this).load(imageUrl).into(ivImage6);
        loading();
    }

}
