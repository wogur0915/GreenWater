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

    private View view;

    EditText edit;
    TextView text;

    String cntntsNo = null;

    XmlPullParser xpp;
    String key = "20221114SOXZKWXXJJSDEZYFMHEBG"; // 할당받은 api키

    String data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        View v = inflater.inflate(R.layout.managementbook, container, false); // fragment안에서 setContentView, findViewById 사용하기 위해 inflater

        //setContentView(R.layout.managementbook);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        edit = (EditText) v.findViewById(R.id.edit);
        text = (TextView) v.findViewById(R.id.result);

        Button button = (Button) v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (v.getId()) {
                    case R.id.button:

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                cntntsNo = getXmlDatalist(); // 식물명 검색하면 컨텐츠 번호 받아오기
                                data = getXmlDataDtl(cntntsNo); // 받아온 컨텐츠 번호로 식물 상세정보 받아오기



                                getActivity().runOnUiThread(new Runnable() { // fragment안에서 runOnUiThread 사용하기위해 getActivity 사용
                                    @Override
                                    public void run() {
                                        text.setText(data);
                                    }
                                });
                            }
                        }).start();
                        break;
                }
            }
        });

        return v; // return inflater
    }

    String getXmlDatalist() {

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
                        tag = xpp.getName();

                        if (tag.equals("item")) ;
                        else if (tag.equals("cntntsNo")) {
                            cntntsNo = xpp.getName();
                            //buffer.append("컨텐츠번호 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            // buffer.append("\n");
                        } /*else if (tag.equals("cntntsSj")) {
                            //buffer.append("식물명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        */
                        break;

                    case XmlPullParser.TEXT:
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
        //buffer.append("파싱 끝\n");
        return buffer.toString();

    }

    String getXmlDataDtl(String cntntsNo) {

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
                        tag = xpp.getName();

                        if (tag.equals("item")) ;
                        else if (tag.equals("distbNm")) {
                            buffer.append("식물명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("fmlCodeNm")) {
                            buffer.append("과명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("orgplceInfo")) {
                            buffer.append("원산지 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("prpgtEraInfo")) {
                            buffer.append("번식 시기 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("grwtveCodeNm")) {
                            buffer.append("생장 속도 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("grwhTpCodeNm")) {
                            buffer.append("생육 온도 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("winterLwetTpCodeNm")) {
                            buffer.append("겨울 최저 온도 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("hdCodeNm")) {
                            buffer.append("적정 습도 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("frtlzrInfo")) {
                            buffer.append("비료 정보 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("soilInfo")) {
                            buffer.append("토양 정보 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("watercycleSprngCodeNm")) {
                            buffer.append("봄 물주기 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("watercycleSummerCodeNm")) {
                            buffer.append("여름 물주기 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("watercycleAutumnCodeNm")) {
                            buffer.append("가을 물주기 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("watercycleWinterCodeNm")) {
                            buffer.append("겨울 물주기 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("dlthtsManageInfo")) {
                            buffer.append("병충해 관리 정보 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("speclmanageInfo")) {
                            buffer.append("특별관리 정보 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("fncltyInfo")) {
                            buffer.append("기능성 정보 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("managedemanddoCodeNm")) {
                            buffer.append("관리요구도 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("clCodeNm")) {
                            buffer.append("분류 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("grwhstleCodeNm")) {
                            buffer.append("생육형태 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("indoorpsncpacompositionCodeNm")) {
                            buffer.append("실내정원구성 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("eclgyCodeNm")) {
                            buffer.append("생태 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("lefmrkCodeNm")) {
                            buffer.append("잎무늬 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("lefcolrCodeNm")) {
                            buffer.append("잎색 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("ignSeasonCodeNm")) {
                            buffer.append("발화 계절 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("flclrCodeNm")) {
                            buffer.append("꽃색 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("prpgtmthCodeNm")) {
                            buffer.append("번식방법 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("lighttdemanddoCodeNm")) {
                            buffer.append("광요구도 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("postngplaceCodeNm")) {
                            buffer.append("배치장소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        else if (tag.equals("dlthtsCodeNm")) {
                            buffer.append("병충해 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item"))
                            buffer.append("\n");
                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {

        }

        //buffer.append("파싱 끝\n");
        return buffer.toString();
    }
}

