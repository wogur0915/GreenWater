package com.root.greenwater;

import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class wateringplant extends Fragment {

    private View view;
//    private Button btn_adjust;
//    private EditText et_humid;
//    private TextView tv_humid;
//    private TextView tv_status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        view = inflater.inflate(R.layout.wateringplant, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        Button btn_adjust = view.findViewById(R.id.btn_adjust);
        EditText et_humid = view.findViewById(R.id.et_humid);
        TextView tv_humid = view.findViewById(R.id.tv_humid);
        TextView tv_status = view.findViewById(R.id.tv_status);

        btn_adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_humid.setText((et_humid.getText()));
                if (Integer.parseInt(tv_humid.getText().toString()) < 40)
                    tv_status.setText("화분의 습도가 낮습니다.\n물을 주세요.");
                else if (Integer.parseInt(tv_humid.getText().toString()) > 90)
                    tv_status.setText("화분의 습도가 너무 높습니다.\n물을 줄이세요.");
                else tv_status.setText("화분의 습도가 적당합니다.");
            }
        });

        return view;
    }
}
