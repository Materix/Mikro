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
  while (waitingForConnect() == false) {
    ;
  }
  while (waitingForOrders() == false) {
    ;
  }
}

// wiadomości:
// c -> connect
// m -> move
// t -> transfer
// s -> stop
// d -> disconnect
boolean waitingForConnect() {
  Serial.println("Czekam na polaczenie");
  while (!bluetoothSerial.available()) {
    delay(100);
  }
  if (bluetoothSerial.available()) {
    data = bluetoothSerial.read();
    if (data == 'c') {
      Serial.println("Nawiazano polaczenie");
      bluetoothSerial.println("OK");
      while (bluetoothSerial.available()) {
        data = bluetoothSerial.read();
      }
      return true;
    } else {
      Serial.println("Zla wiadomosc");
      bluetoothSerial.println("NIE");
      while (bluetoothSerial.available()) {
        data = bluetoothSerial.read();
      }
      return false;
    }
  }
}

boolean waitingForOrders() {
  Serial.println("Czekam na rozkazy");
  while (!bluetoothSerial.available()) {
    delay(100);
  }
  if (bluetoothSerial.available()) {
    data = bluetoothSerial.read();
    if (data == 'm') {
      receiveAndMakeMoveOrder();
    } else if (data = 't') {
      receiveAndMakeTransferOrder();
    } else if (data = 'd') {
      Serial.print("Koniec polaczenia");
      return true;
    } else {
      Serial.print("Bledny rozkaz");
      while (bluetoothSerial.available()) {
        data = bluetoothSerial.read();
      }
    }
  }
  return false;
}

boolean receiveAndMakeMoveOrder() {
  int toX = 0, toY = 0;
  data = bluetoothSerial.read();
  if (data == 'c') {
    data = bluetoothSerial.read();
    if (data == 'x') {
      data = bluetoothSerial.read();
      while (data != 'y') {
        if (data >= 48 && data <= 57) {
          toX *= 10;
          toX += data - 48; 
        } else {
          return false;
        }
        data = bluetoothSerial.read();
      }
      data = bluetoothSerial.read();
      while (data != '$') {
        if (data >= 48 && data <= 57) {
          toY *= 10;
          toY += data - 48; 
        } else {
          return false;
        }
        data = bluetoothSerial.read();
      }
    }
  } else {
    return false;
  }
  while (bluetoothSerial.available()) {
    data = bluetoothSerial.read();
  }
  bluetoothSerial("OK");
  move(toX, toY);
  bluetoothSerial("OK");
  return true;
}

boolean receiveAndMakeTransferOrder() {
  int toX = 0, toY = 0, fromX = 0, fromY = 0;
  data = bluetoothSerial.read();
  if (data == 'f') {
    data = bluetoothSerial.read();
    if (data == 'c') {
      data = bluetoothSerial.read();
      if (data == 'x') {
        data = bluetoothSerial.read();
        while (data != 'y') {
          if (data >= 48 && data <= 57) {
            fromX *= 10;
            fromX += data - 48; 
          } else {
            return false;
          }
          data = bluetoothSerial.read();
        }
        data = bluetoothSerial.read();
        while (data != 't') {
          if (data >= 48 && data <= 57) {
            fromY *= 10;
            fromY += data - 48; 
          } else {
            return false;
          }
          data = bluetoothSerial.read();
        }
        data = bluetoothSerial.read();
        if (data == 'c') {
          data = bluetoothSerial.read();
          if (data == 'x') {
            data = bluetoothSerial.read();
            while (data != 'y') {
              if (data >= 48 && data <= 57) {
                toX *= 10;
                toX += data - 48; 
              } else {
                return false;
              }
              data = bluetoothSerial.read();
            }
            data = bluetoothSerial.read();
            while (data != '$') {
              if (data >= 48 && data <= 57) {
                toY *= 10;
                toY += data - 48; 
              } else {
                return false;
              }
              data = bluetoothSerial.read();
            }
          }
        }
      }
    }
  } else {
    return false;
  }
  while (bluetoothSerial.available()) {
    data = bluetoothSerial.read();
  }
  bluetoothSerial("OK");
  transfer(fromX, fromY, toX, toY);
  bluetoothSerial("OK");
  
}

void move(int toX, int toY) {
  //tu trzeba rejestrować przerwanie na rx, w związku ze stop
  Serial.println("---Rozkaz obrotu---");
  Serial.print("Przesuwam hak na pozycje x=");
  Serial.print(toX);
  Serial.print(", y=");
  Serial.println(toY);
}

void transfer(int fromX, int fromY, int toX, int toY) {
  Serial.println("---Rozkaz przeniesienia---");
  Serial.print("Przesuwam hak na pozycje x=");
  Serial.print(fromX);
  Serial.print(", y=");
  Serial.println(fromY);
  Serial.println("Chwytam przedmiot");
  Serial.print("Przesuwam hak na pozycje x=");
  Serial.print(toX);
  Serial.print(", y=");
  Serial.println(toY);
  Serial.println("Opuszczam przedmiot");
}
