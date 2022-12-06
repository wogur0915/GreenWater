package com.root.greenwater;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    
    private BottomNavigationView bottomNavigationView;  // 하단바 선언
    private FragmentManager fm;  // 프래그먼트 매니저 선언
    private FragmentTransaction ft;  // 프래그먼트 트랜잭션 선언
    private Plantlist plantlist;  // 식물 목록 프래그먼트 선언
    private Wateringplant wateringplant;  // 식물 습도 상태 프래그먼트 선언
    private Managementbook managementbook;  // 식물 관리법 프래그먼트 선언
    private Settings settings;  // 설정 프래그먼트 선언
    private static final String NOTIFICATION = "notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 1001);
        }

        bottomNavigationView = findViewById(R.id.bottomNavi);  // 하단바 연결
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {  // 하단바 아이템 선택 리스너

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {  // 하단바 아이템 아이디로 분기
                    case R.id.action_setting:
                        changeFrag(0);
                        break;
                    case R.id.action_watertingplant:
                        changeFrag(1);
                        break;
                    case R.id.action_plantlist:
                        changeFrag(2);
                        break;
                    case R.id.action_managementbook:
                        changeFrag(3);
                        break;
                }
                return true;
            }
        });  // 하단바 아이템 선택 리스너 연결

        plantlist = new Plantlist();  // 식물 목록 프래그먼트 생성
        wateringplant = new Wateringplant();  // 식물 습도 상태 프래그먼트 생성
        managementbook = new Managementbook();  // 식물 관리법 프래그먼트 생성
        settings = new Settings();  // 설정 프래그먼트 생성
        setFrag();
        changeFrag(0);  // 첫 프래그먼트 화면 지정

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final boolean fromNotification = extras.getBoolean(NOTIFICATION);
            if (fromNotification) {
                changeFrag(1);
            }
        }
    }

    private void setFrag() {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.mainFrame, settings);
        ft.add(R.id.mainFrame, wateringplant);
        ft.add(R.id.mainFrame, plantlist);
        ft.add(R.id.mainFrame, managementbook);
        ft.hide(settings);
        ft.hide(wateringplant);
        ft.hide(plantlist);
        ft.hide(managementbook);
        ft.commit();
    }

    private void changeFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.show(settings);
                ft.hide(wateringplant);
                ft.hide(plantlist);
                ft.hide(managementbook);
                ft.commit();
                break;
            case 1:
                ft.hide(settings);
                ft.show(wateringplant);
                ft.hide(plantlist);
                ft.hide(managementbook);
                ft.commit();
                break;
            case 2:
                ft.hide(settings);
                ft.hide(wateringplant);
                ft.show(plantlist);
                ft.hide(managementbook);
                ft.commit();
                break;
            case 3:
                ft.hide(settings);
                ft.hide(wateringplant);
                ft.hide(plantlist);
                ft.show(managementbook);
                ft.commit();
                break;
        }
    }
}