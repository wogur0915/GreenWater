#include "BluetoothSerial.h" //블루투스 시리얼 라이브러리 사용
BluetoothSerial SerialBT;

void setup() {
  Serial.begin(115200);
  SerialBT.begin("ESP32"); // 표시될 블루투스 이름

}

void loop() {
  int soilMoisture = analogRead(A0);

  if (Serial.available()) { // 블루투스 값을 읽어들이기
    SerialBT.write(Serial.read());
  }
  if (SerialBT.available()) { //블루투스 값을 읽어들일 때
    char text = SerialBT.read();
    Serial.write(text); // 받은 블루투스 값을 그대로 시리얼 모니터에 출력
    if(text == 'o'){ //만약 받은 신호가 'o'라면
        Serial.print("Soil Moisture : "); // 시리얼 모니터에 'OOO' 표시
        Serial.println(soilMoisture);
        SerialBT.print("Soil Moisture : ");
        SerialBT.println(soilMoisture);
    } else if(text == 'x'){ //만약 받은 신호가 'x'라면
        Serial.println("XXX"); //시리얼 모니터에 'XXX' 표시
    }
  }
  
 /* 
 Serial.print("Soil Moisture : ");
 Serial.print(soilMoisture);
 Serial.print("\n");  
 delay(1000);
 */ // 시리얼 모니터 출력부분 임시 주석처리
}
