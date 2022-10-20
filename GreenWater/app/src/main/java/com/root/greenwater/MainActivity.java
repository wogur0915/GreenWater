package com.root.greenwater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    
    private BottomNavigationView bottomNavigationView;  // 하단바 선언
    private FragmentManager fm;  // 프래그먼트 매니저 선언
    private FragmentTransaction ft;  // 프래그먼트 트랜잭션 선언
    private plantlist plantlist;  // 식물 목록 프래그먼트 선언
    private wateringplant wateringplant;  // 식물 습도 상태 프래그먼트 선언
    private managementbook managementbook;  // 식물 관리법 프래그먼트 선언
    private settings settings;  // 설정 프래그먼트 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);  // 하단바 연결
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {  // 하단바 아이템 선택 리스너
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {  // 하단바 아이템 아이디로 분기
                    case R.id.action_plantlist:
                            setFrag(0);
                        break;
                    case R.id.action_watertingplant:
                        setFrag(1);
                        break;
                    case R.id.action_managementbook:
                        setFrag(2);
                        break;
                    case R.id.action_setting:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });  // 하단바 아이템 선택 리스너 연결

        plantlist = new plantlist();  // 식물 목록 프래그먼트 생성
        wateringplant = new wateringplant();  // 식물 습도 상태 프래그먼트 생성
        managementbook = new managementbook();  // 식물 관리법 프래그먼트 생성
        settings = new settings();  // 설정 프래그먼트 생성
        setFrag(0);  // 첫 프래그먼트 화면 지정

    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.mainFrame, plantlist);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.mainFrame, wateringplant);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.mainFrame, managementbook);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.mainFrame, settings);
                ft.commit();
                break;
        }
    }
}