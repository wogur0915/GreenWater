void setup() {
  Serial.begin(9600);
  
}

void loop() {
  int soilMoisture = analogRead(A0);
  Serial.print("Soil Moisture: ");
  Serial.print(soilMoisture);
  Serial.print("\n");
  delay(1000);

}
