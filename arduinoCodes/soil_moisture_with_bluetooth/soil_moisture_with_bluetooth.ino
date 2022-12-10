#include "BluetoothSerial.h" //블루투스 시리얼 라이브러리 사용
BluetoothSerial SerialBT;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  SerialBT.begin("ESP32"); // 표시될 블루투스 이름

}

void loop() {
  // put your main code here, to run repeatedly:  
  int soilMoisture = analogRead(36);
    Serial.print("Soil Moisture : "); // 시리얼 모니터에 'OOO' 표시
    Serial.println(soilMoisture);
    SerialBT.print("Soil Moisture : ");
    SerialBT.println(soilMoisture);
    delay(1000);

  }
  
//  Serial.print("Soil Moisture : ");
//  Serial.print(soilMoisture);
//  Serial.print("\n");  
//  delay(1000);
