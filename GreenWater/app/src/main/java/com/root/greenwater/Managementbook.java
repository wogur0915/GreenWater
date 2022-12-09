package com.root.greenwater;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.URLEncoder;
import java.net.URL;


public class Managementbook extends Fragment {

    EditText edit;
    TextView text;

    String cntntsNo = null;
    boolean incntntsNo = false;

    String key = "20221114SOXZKWXXJJSDEZYFMHEBG"; // 할당받은 api키

    String data;

    boolean indistbNm = false, infmlCodeNm = false, inorgplceInfo = false, inprpgtEraInfo = false, ingrwtveCodeNm = false, ingrwhTpCodeNm = false,
            inwinterLwetTpCodeNm = false, inhdCodeNm = false, infrtlzrInfo = false, insoilInfo = false, inwatercycleSprngCodeNm = false,
            inwatercycleSummerCodeNm = false, inwatercycleAutumnCodeNm = false, inwatercycleWinterCodeNm = false, indlthtsManageInfo = false,
            inspeclmanageInfo = false, infncltyInfo = false, inmanagedemanddoCodeNm = false, inclCodeNm = false,
            ingrwhstleCodeNm = false, inindoorpsncpacompositionCodeNm = false, ineclgyCodeNm = false, inlefmrkCodeNm = false, inlefcolrCodeNm = false, inignSeasonCodeNm = false,
            inflclrCodeNm = false, inprpgtmthCodeNm = false,
            inlightdemanddoCodeNm = false, inpostngplaceCodeNm = false, indlthtsCodeNm = false;

    String distbNm, fmlCodeNm, orgplceInfo, prpgtEraInfo, grwtveCodeNm, grwhTpCodeNm, winterLwetTpCodeNm, hdCodeNm, frtlzrInfo, soilInfo, watercycleSprngCodeNm,
            watercycleSummerCodeNm, watercycleAutumnCodeNm, watercycleWinterCodeNm, dlthtsManageInfo, speclmanageInfo, fncltyInfo, managedemanddoCodeNm, clCodeNm,
            grwhstleCodeNm, indoorpsncpacompositionCodeNm, eclgyCodeNm, lefmrkCodeNm, lefcolrCodeNm, ignSeasonCodeNm, flclrCodeNm, prpgtmthCodeNm,
            lightdemanddoCodeNm, postngplaceCodeNm, dlthtsCodeNm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        View v = inflater.inflate(R.layout.managementbook, container, false); // fragment안에서 setContentView, findViewById 사용하기 위해 inflater

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        edit = (EditText) v.findViewById(R.id.edit);
        text = (TextView) v.findViewById(R.id.result);

        Button button = (Button) v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                text.setText(""); // 이전의 결과 값을 보이지 않게 삭제
                switch (v.getId()) {
                    case R.id.button:

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                getXmlDatalist(); // 식물명 검색하면 컨텐츠 번호 받아오기
                                getXmlDataDtl(cntntsNo); // 받아온 컨텐츠 번호로 식물 상세정보 받아오기
                            }
                        }).start();
                        break;
                }
            }
        });

        return v; // return inflater
    }

    void getXmlDatalist() {

        StringBuffer buffer = new StringBuffer();

        String str = edit.getText().toString();
        String name = URLEncoder.encode(str);


        String queryUrl = "http://api.nongsaro.go.kr/service/garden/gardenList?"
                + "apiKey=" + key
                + "&sType=sCntntsSj"
                + "&sText=" + name;

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;


            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        //buffer.append("파싱 시작\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        if(xpp.getName().equals("cntntsNo")){
                            incntntsNo = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if(incntntsNo){
                            cntntsNo = xpp.getText();
                            incntntsNo = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item"))
                            //buffer.append("\n");
                            break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {

        }
    }

    void getXmlDataDtl(String cntntsNo) {

        StringBuffer buffer = new StringBuffer();

        String str = edit.getText().toString();
        String name = URLEncoder.encode(str);


        String queryUrl = "http://api.nongsaro.go.kr/service/garden/gardenDtl?"
                + "apiKey=" + key
                + "&cntntsNo=" + cntntsNo;

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        //buffer.append("파싱 시작\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        if (xpp.getName().equals("distbNm")) {
                            indistbNm = true;
                        }
                        if (xpp.getName().equals("fmlCodeNm")) {
                            infmlCodeNm = true;
                        }
                        if (xpp.getName().equals("orgplceInfo")) {
                            inorgplceInfo = true;
                        }
                        if (xpp.getName().equals("prpgtEraInfo")) {
                            inprpgtEraInfo = true;
                        }
                        if (xpp.getName().equals("grwtveCodeNm")) {
                            ingrwtveCodeNm = true;
                        }
                        if (xpp.getName().equals("grwhTpCodeNm")) {
                            ingrwhTpCodeNm = true;
                        }
                        if (xpp.getName().equals("winterLwetTpCodeNm")) {
                            inwinterLwetTpCodeNm = true;
                        }
                        if (xpp.getName().equals("hdCodeNm")) {
                            inhdCodeNm = true;
                        }
                        if (xpp.getName().equals("frtlzrInfo")) {
                            infrtlzrInfo = true;
                        }
                        if (xpp.getName().equals("soilInfo")) {
                            insoilInfo = true;
                        }
                        if (xpp.getName().equals("watercycleSprngCodeNm")) {
                            inwatercycleSprngCodeNm = true;
                        }
                        if (xpp.getName().equals("watercycleSummerCodeNm")) {
                            inwatercycleSummerCodeNm = true;
                        }
                        if (xpp.getName().equals("watercycleAutumnCodeNm")) {
                            inwatercycleAutumnCodeNm = true;
                        }
                        if (xpp.getName().equals("watercycleWinterCodeNm")) {
                            inwatercycleWinterCodeNm = true;
                        }
                        if (xpp.getName().equals("dlthtsManageInfo")) {
                            indlthtsManageInfo = true;
                        }
                        if (xpp.getName().equals("speclmanageInfo")) {
                            inspeclmanageInfo = true;
                        }
                        if (xpp.getName().equals("fncltyInfo")) {
                            infncltyInfo = true;
                        }
                        if (xpp.getName().equals("managedemanddoCodeNm")) {
                            inmanagedemanddoCodeNm = true;
                        }
                        if (xpp.getName().equals("clCodeNm")) {
                            inclCodeNm = true;
                        }
                        if (xpp.getName().equals("grwhstleCodeNm")) {
                            ingrwhstleCodeNm = true;
                        }
                        if (xpp.getName().equals("indoorpsncpacompositionCodeNm")) {
                            inindoorpsncpacompositionCodeNm = true;
                        }
                        if (xpp.getName().equals("eclgyCodeNm")) {
                            ineclgyCodeNm = true;
                        }
                        if (xpp.getName().equals("lefmrkCodeNm")) {
                            inlefmrkCodeNm = true;
                        }
                        if (xpp.getName().equals("lefcolrCodeNm")) {
                            inlefcolrCodeNm = true;
                        }
                        if (xpp.getName().equals("ignSeasonCodeNm")) {
                            inignSeasonCodeNm = true;
                        }
                        if (xpp.getName().equals("flclrCodeNm")) {
                            inflclrCodeNm = true;
                        }
                        if (xpp.getName().equals("prpgtmthCodeNm")) {
                            inprpgtmthCodeNm = true;
                        }
                        if (xpp.getName().equals("lightdemanddoCodeNm")) {
                            inlightdemanddoCodeNm = true;
                        }
                        if (xpp.getName().equals("postngplaceCodeNm")) {
                            inpostngplaceCodeNm = true;
                        }
                        if (xpp.getName().equals("dlthtsCodeNm")) {
                            indlthtsCodeNm = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (indistbNm) {
                            distbNm = xpp.getText();
                            indistbNm = false;
                        }
                        if (infmlCodeNm) {
                            fmlCodeNm = xpp.getText();
                            infmlCodeNm = false;
                        }
                        if (inorgplceInfo) {
                            orgplceInfo = xpp.getText();
                            inorgplceInfo = false;
                        }
                        if (inprpgtEraInfo) {
                            prpgtEraInfo = xpp.getText();
                            inprpgtEraInfo = false;
                        }
                        if (ingrwtveCodeNm) {
                            grwtveCodeNm = xpp.getText();
                            ingrwtveCodeNm = false;
                        }
                        if (ingrwhTpCodeNm) {
                            grwhTpCodeNm = xpp.getText();
                            ingrwhTpCodeNm = false;
                        }
                        if (inwinterLwetTpCodeNm) {
                            winterLwetTpCodeNm = xpp.getText();
                            inwinterLwetTpCodeNm = false;
                        }
                        if (inhdCodeNm) {
                            hdCodeNm = xpp.getText();
                            inhdCodeNm = false;
                        }
                        if (infrtlzrInfo) {
                            frtlzrInfo = xpp.getText();
                            infrtlzrInfo = false;
                        }
                        if (insoilInfo) {
                            soilInfo = xpp.getText();
                            insoilInfo = false;
                        }
                        if (inwatercycleSprngCodeNm) {
                            watercycleSprngCodeNm = xpp.getText();
                            inwatercycleSprngCodeNm = false;
                        }
                        if (inwatercycleSummerCodeNm) {
                            watercycleSummerCodeNm = xpp.getText();
                            inwatercycleSummerCodeNm = false;
                        }
                        if (inwatercycleAutumnCodeNm) {
                            watercycleAutumnCodeNm = xpp.getText();
                            inwatercycleAutumnCodeNm = false;
                        }
                        if (inwatercycleWinterCodeNm) {
                            watercycleWinterCodeNm = xpp.getText();
                            inwatercycleWinterCodeNm = false;
                        }
                        if (indlthtsManageInfo) {
                            dlthtsManageInfo = xpp.getText();
                            indlthtsManageInfo = false;
                        }
                        if (inspeclmanageInfo) {
                            speclmanageInfo = xpp.getText();
                            inspeclmanageInfo = false;
                        }
                        if (infncltyInfo) {
                            fncltyInfo = xpp.getText();
                            infncltyInfo = false;
                        }
                        if (inmanagedemanddoCodeNm) {
                            managedemanddoCodeNm = xpp.getText();
                            inmanagedemanddoCodeNm = false;
                        }
                        if (inclCodeNm) {
                            clCodeNm = xpp.getText();
                            inclCodeNm = false;
                        }
                        if (ingrwhstleCodeNm) {
                            grwhstleCodeNm = xpp.getText();
                            ingrwhstleCodeNm = false;
                        }
                        if (inindoorpsncpacompositionCodeNm) {
                            indoorpsncpacompositionCodeNm = xpp.getText();
                            inindoorpsncpacompositionCodeNm = false;
                        }
                        if (ineclgyCodeNm) {
                            eclgyCodeNm = xpp.getText();
                            ineclgyCodeNm = false;
                        }
                        if (inlefmrkCodeNm) {
                            lefmrkCodeNm = xpp.getText();
                            inlefmrkCodeNm = false;
                        }
                        if (inlefcolrCodeNm) {
                            lefcolrCodeNm = xpp.getText();
                            inlefcolrCodeNm = false;
                        }
                        if (inignSeasonCodeNm) {
                            ignSeasonCodeNm = xpp.getText();
                            inignSeasonCodeNm = false;
                        }
                        if (inflclrCodeNm) {
                            flclrCodeNm = xpp.getText();
                            inflclrCodeNm = false;
                        }
                        if (inprpgtmthCodeNm) {
                            prpgtmthCodeNm = xpp.getText();
                            inprpgtmthCodeNm = false;
                        }
                        if (inlightdemanddoCodeNm) {
                            lightdemanddoCodeNm = xpp.getText();
                            inlightdemanddoCodeNm = false;
                        }
                        if (inpostngplaceCodeNm) {
                            postngplaceCodeNm = xpp.getText();
                            inpostngplaceCodeNm = false;
                        }
                        if (indlthtsCodeNm) {
                            dlthtsCodeNm = xpp.getText();
                            indlthtsCodeNm = false;
                        }


                        break;

                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equals("item")) {
                            text.setText(text.getText() + "식물명 : " + distbNm + "\n\n 과명 : " + fmlCodeNm + "\n\n 원산지 : "
                                    + orgplceInfo + "\n\n 번식 시기 : " + prpgtmthCodeNm + "\n\n 생장 속도 : " + grwtveCodeNm
                                    + "\n\n 생육 온도 : " + grwhTpCodeNm + "\n\n 겨울 최저 온도 : " + winterLwetTpCodeNm
                                    + "\n\n 적정 습도 : " + hdCodeNm + "\n\n 비료 정보 :" + frtlzrInfo + "\n\n 토양 정보 : " + soilInfo
                                    + "\n\n 봄 물주기 : " + watercycleSprngCodeNm + "\n\n 여름 물주기 : " + watercycleSummerCodeNm
                                    + "\n\n 가을 물주기 : " + watercycleAutumnCodeNm + "\n\n 겨울 물주기 : " + watercycleWinterCodeNm
                                    + "\n\n 병충해 관리 정보 : " + dlthtsManageInfo + "\n\n 특별관리 정보 :" + speclmanageInfo
                                    + "\n\n 기능성 정보 : " + fncltyInfo + "\n\n 관리요구도 : " + managedemanddoCodeNm + "\n\n 분류 : " + clCodeNm
                                    + "\n\n 생육형태 : " + grwhstleCodeNm + "\n\n 실내정원구성 : " + indoorpsncpacompositionCodeNm + "\n\n 생태 : " + eclgyCodeNm
                                    + "\n\n 잎무늬 : " + lefmrkCodeNm + "\n\n 잎색 : " + lefcolrCodeNm + "\n\n 발화 계절 : " + ignSeasonCodeNm
                                    + "\n\n 꽃색 : " + flclrCodeNm + "\n\n 번식방법 : " + prpgtmthCodeNm + "\n\n 광요구도 : " + lightdemanddoCodeNm
                                    + "\n\n 배치장소 : " + postngplaceCodeNm + "\n\n 병충해 : " + dlthtsCodeNm + "\n\n");
                        }
                        break;
                }
                eventType = xpp.next();
            }

        } catch (Exception e) {

        }
    }
}

