#include <LiquidCrystal_I2C.h>
#include <Wire.h>

LiquidCrystal_I2C lcd(0x27, 16, 2);

void setup() {
  Serial.begin(9600);
  lcd.init();
  lcd.backlight();
  
}

void loop() {
  int soilMoisture = analogRead(A0);
  Serial.print("Soil Moisture: ");
  Serial.print(soilMoisture);
  Serial.print("\n");
  delay(1000);

  lcd.setCursor(0,0);
  lcd.print("Moisture : ");
  lcd.print(soilMoisture);
}
