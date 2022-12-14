package com.root.greenwater;

import static java.lang.Integer.parseInt;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class Wateringplant extends Fragment {

    private View view;
    private static TextView mtv_humid;
    private static TextView tv_status;
    private RadioGroup rg_plantgroup;
    private RadioButton rdo_dryplant, rdo_wetplant, rdo_normalplant;
    private static NotificationHelper mNotificationhelper;
    private static ImageView iv_plant;
    public static int count = 0;
    public static int plantFlag = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        view = inflater.inflate(R.layout.wateringplant, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        tv_status = view.findViewById(R.id.tv_status);
        mtv_humid = view.findViewById(R.id.tv_humid);
        iv_plant = view.findViewById(R.id.plant_image);
        rg_plantgroup = view.findViewById(R.id.rg_plantgroup);
        rdo_dryplant = view.findViewById(R.id.rdo_dryplant);
        rdo_wetplant = view.findViewById(R.id.rdo_wetplant);
        rdo_normalplant = view.findViewById(R.id.rdo_normalplant);


        mNotificationhelper = new NotificationHelper(requireActivity());

        if (mtv_humid.getText().toString() == "-1%") {
            tv_status.setText("모니터링 장치가 연결되지 않았습니다.");
            mtv_humid.setText("");
            iv_plant.setImageResource(R.drawable.connection_error);
        }

        rg_plantgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdo_normalplant:
                        Toast.makeText(getActivity(), "보통 식물을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        plantFlag = 0;
                        break;
                    case R.id.rdo_dryplant:
                        Toast.makeText(getActivity(), "건조한 식물을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        plantFlag = 1;
                        break;
                    case R.id.rdo_wetplant:
                        Toast.makeText(getActivity(), "습식 식물을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        plantFlag = 2;
                        break;
                }
            }
        });

        return view;
    }

    public static void displayReceivedData(String message)
    {
        String cuttingMessage = message.replaceAll("[^\\d]", "");
        String calcHumid = calculateHumid(cuttingMessage);
        mtv_humid.setText(calcHumid + "%");
        // 일반 식물
        if (plantFlag == 0) {
            if (Integer.parseInt(mtv_humid.getText().toString().replaceAll("[^\\d]", "")) >= 0
                    && Integer.parseInt(mtv_humid.getText().toString().replaceAll("[^\\d]", "")) < 10) {
                tv_status.setText("화분의 습도가 낮습니다.\n물을 주세요.");
                tv_status.setTextColor(Color.parseColor("#E0144C"));
                iv_plant.setImageResource(R.drawable.crying_plant);
                alertDelay();
            }
            else if (Integer.parseInt(mtv_humid.getText().toString().replaceAll("[^\\d]", "")) > 30) {
                count = 0;
                tv_status.setText("화분의 습도가 너무 높습니다.\n물을 줄이세요.");
                tv_status.setTextColor(Color.parseColor("#C147E9"));
                iv_plant.setImageResource(R.drawable.toomuch_water);
            }
            else {
                count = 0;
                tv_status.setText("화분의 습도가 적당합니다.");
                tv_status.setTextColor(Color.parseColor("#1dde7d"));
                iv_plant.setImageResource(R.drawable.smile_plant);
            }
        }
        // 건조 식물
        else if (plantFlag == 1) {
            if (Integer.parseInt(mtv_humid.getText().toString().replaceAll("[^\\d]", "")) >= 0
                    && Integer.parseInt(mtv_humid.getText().toString().replaceAll("[^\\d]", "")) < 5) {
                tv_status.setText("화분의 습도가 낮습니다.\n물을 주세요.");
                tv_status.setTextColor(Color.parseColor("#E0144C"));
                iv_plant.setImageResource(R.drawable.crying_plant);
                alertDelay();
            }
            else if (Integer.parseInt(mtv_humid.getText().toString().replaceAll("[^\\d]", "")) > 25) {
                count = 0;
                tv_status.setText("화분의 습도가 너무 높습니다.\n물을 줄이세요.");
                tv_status.setTextColor(Color.parseColor("#C147E9"));
                iv_plant.setImageResource(R.drawable.toomuch_water);
            }
            else {
                count = 0;
                tv_status.setText("화분의 습도가 적당합니다.");
                tv_status.setTextColor(Color.parseColor("#1dde7d"));
                iv_plant.setImageResource(R.drawable.smile_plant);
            }
        }

        // 습식 식물
        else if (plantFlag == 2) {
            if (Integer.parseInt(mtv_humid.getText().toString().replaceAll("[^\\d]", "")) >= 0
                    && Integer.parseInt(mtv_humid.getText().toString().replaceAll("[^\\d]", "")) < 15) {
                tv_status.setText("화분의 습도가 낮습니다.\n물을 주세요.");
                tv_status.setTextColor(Color.parseColor("#E0144C"));
                iv_plant.setImageResource(R.drawable.crying_plant);
                alertDelay();
            }
            else if (Integer.parseInt(mtv_humid.getText().toString().replaceAll("[^\\d]", "")) > 35) {
                count = 0;
                tv_status.setText("화분의 습도가 너무 높습니다.\n물을 줄이세요.");
                tv_status.setTextColor(Color.parseColor("#C147E9"));
                iv_plant.setImageResource(R.drawable.toomuch_water);
            }
            else {
                count = 0;
                tv_status.setText("화분의 습도가 적당합니다.");
                tv_status.setTextColor(Color.parseColor("#1dde7d"));
                iv_plant.setImageResource(R.drawable.smile_plant);
            }
        }
    }

    public static void alertDelay()
    {
        if (Settings.alerFlag == 1) {
            count = count + 1;
            if (count == 1 || count % 800 == 0) {
                sendOnChannel1("GreenWater - 화분 습도 경고 알림", "화분의 습도가 낮습니다.\n물을 주세요.");
                Toast.makeText(mtv_humid.getContext(), "화분의 습도가 낮습니다.\n물을 주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String calculateHumid(String humid)
    {
        int tmpHumidInt = parseInt(humid);
        int humidInt = (4095 - tmpHumidInt) / 80;
        String humidString = Integer.toString(humidInt);
        return humidString;
    }


    public static void sendOnChannel1(String title, String message){
        NotificationCompat.Builder nb = mNotificationhelper.getChannel1Notification(title, message);
        mNotificationhelper.getManager().notify(1, nb.build());
    }
}
