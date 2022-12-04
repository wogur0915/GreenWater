package com.root.greenwater;

import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class Wateringplant extends Fragment {

    private View view;
    private TextView tv_humid;
    private static TextView mtv_humid;
    private static TextView tv_status;
    private static NotificationHelper mNotificationhelper;
    public static int count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        view = inflater.inflate(R.layout.wateringplant, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        tv_humid = view.findViewById(R.id.tv_humid);
        tv_status = view.findViewById(R.id.tv_status);
        mtv_humid = view.findViewById(R.id.tv_humid);

        mNotificationhelper = new NotificationHelper(requireActivity());

        return view;
    }

    public static void displayReceivedData(String message)
    {
        String cuttingMessage = message.replaceAll("[^\\d]", "");
        String calcHumid = calculateHumid(cuttingMessage);
        mtv_humid.setText(calcHumid);
        if (Integer.parseInt(mtv_humid.getText().toString()) >= 0 && Integer.parseInt(mtv_humid.getText().toString()) < 10) {
            tv_status.setText("화분의 습도가 낮습니다.\n물을 주세요.");
            alertDelay();
        }
        else if (Integer.parseInt(mtv_humid.getText().toString()) > 30) {
            count = 0;
            tv_status.setText("화분의 습도가 너무 높습니다.\n물을 줄이세요.");
        }
        else {
            count = 0;
            tv_status.setText("화분의 습도가 적당합니다.");
        }
    }

    public static void alertDelay()
    {
        count = count + 1;
        if (count == 1 || count % 750 == 0) {
            sendOnChannel1("화분 습도 경고 알림", "화분의 습도가 낮습니다.\n물을 주세요.");
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
