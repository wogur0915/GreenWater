package com.root.greenwater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends BasicActivity {

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText mEtEmail, mEtPwd, mEtNickname; // 회원가입 입력필드
    private TextView tv_warning;
    private Button mBtnRegister; // 회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("greenwater");

        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtNickname = findViewById(R.id.et_nickname);
        mBtnRegister = findViewById(R.id.btn_register);
        tv_warning = findViewById(R.id.tv_warning);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strNickname = mEtNickname.getText().toString();

                if (strPwd.length() < 6) {
                    tv_warning.setText("비밀번호는 6자리 이상이어야 합니다.");
                    Toast.makeText(RegisterActivity.this, "비밀번호를 6자리 이상 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Firebase Auth 진행
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                UserAccount account = new UserAccount();
                                account.setIdToken(firebaseUser.getUid());
                                account.setEmailId(firebaseUser.getEmail());
                                account.setPassword(strPwd);
                                account.setNickname(strNickname);

                                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                                mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).child("PostingList").child("SavePostNum").setValue(0);

                                Toast.makeText(RegisterActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();   // 현재 액티비티 파괴
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}