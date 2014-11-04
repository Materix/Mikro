#include <SoftwareSerial.h>
SoftwareSerial bluetoothSerial(10, 11);
char data;

void setup() {
  delay(5000);
  Serial.begin(9600);
  bluetoothSerial.begin(9600);
  Serial.println("Czekam na wiadomosci z bluetooth");
  bluetoothSerial.print("AT");
  while(!bluetoothSerial.available()) {
    ;
  }
  data = bluetoothSerial.read();
  Serial.print(data);
  data = bluetoothSerial.read();
  Serial.println(data);

}

void loop() {
  if (Serial.available()) {
    data = Serial.read();
    Serial.println(data);
  }
  if (bluetoothSerial.available()) {
    Serial.println("Dostalem wiadomosc");
    while (bluetoothSerial.available()) {
      data = bluetoothSerial.read();
      Serial.print(data);
   }
    Serial.println();
    Serial.println("Koniec wiadomosci");
    bluetoothSerial.println("OK");
  } else {
    //bluetoothSerial.println("Test dzialania");
  }
  delay(200);
}
