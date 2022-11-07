package com.root.greenwater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd; // 회원가입 입력필드
    private Button mBtnRegister; // 회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mBtnRegister = findViewById(R.id.btn_register);



    }
}