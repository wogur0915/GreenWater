package com.root.greenwater;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WritePost extends BasicActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    public static int postNum;
    public String postN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        //컴포넌트 변수에 담기
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editInner = findViewById(R.id.editInner);
        Button confirmBtn = findViewById(R.id.submit_post);


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
                finish();
            } //onClick
        });//setOnClickListener
    }//onCreate

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

}
