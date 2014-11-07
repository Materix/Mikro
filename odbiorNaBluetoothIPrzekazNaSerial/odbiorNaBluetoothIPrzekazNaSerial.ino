#include <SoftwareSerial.h>
SoftwareSerial bluetoothSerial(10, 11);
char data;

void setup() {
  delay(5000);
  Serial.begin(9600);
  bluetoothSerial.begin(9600);
  //Serial.println("Czekam na wiadomosci z bluetooth");
  bluetoothSerial.print("AT");
  while(!bluetoothSerial.available()) {
    ;
  }
  data = bluetoothSerial.read();
  data = bluetoothSerial.read();
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
      initialize();
      bluetoothSerial.println("OK");
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

void initialize() {
  Serial.println("Inicjuje sie");
  delay(2000);
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
    } else if (data == 't') {
      receiveAndMakeTransferOrder();
    } else if (data == 'd') {
      Serial.println("Koniec polaczenia");
      return true;
    } else {
      Serial.println("Bledny rozkaz");
    }
  }
  while (bluetoothSerial.available()) {
    data = bluetoothSerial.read();
  }
  return false;
}

boolean receiveAndMakeMoveOrder() {
  boolean minus = false;
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
        } else if (data == '-') {
          minus = true;
        } else {
          return false;
        }
        data = bluetoothSerial.read();
      }
      if (minus) {
        toX = -toX;
      }
      minus = false;
      data = bluetoothSerial.read();
      while (data != '$') {
        if (data >= 48 && data <= 57) {
          toY *= 10;
          toY += data - 48; 
        } else if (data == '-') {
          minus = true;
        } else {
          return false;
        }
        data = bluetoothSerial.read();
      }
      if (minus) {
        toY = -toY;
      }
      minus = false;;
    }
  } else {
    return false;
  }
  while (bluetoothSerial.available()) {
    data = bluetoothSerial.read();
  }
  bluetoothSerial.println("OK");
  move(toX, toY);
  bluetoothSerial.println("OK");
  return true;
}

boolean receiveAndMakeTransferOrder() {
  boolean minus = false;
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
          } else if (data == '-') {
            minus = true;
          } else {
            return false;
          }
          data = bluetoothSerial.read();
        }
        if (minus) {
          fromX = -fromX;
        }
        minus = false;
        data = bluetoothSerial.read();
        while (data != 't') {
          if (data >= 48 && data <= 57) {
            fromY *= 10;
            fromY += data - 48; 
          } else if (data == '-') {
            minus = true;
          } else {
            return false;
          }
          data = bluetoothSerial.read();
        }
        if (minus) {
          fromY = -fromY;
        }
        minus = false;
        data = bluetoothSerial.read();
        if (data == 'c') {
          data = bluetoothSerial.read();
          if (data == 'x') {
            data = bluetoothSerial.read();
            while (data != 'y') {
              if (data >= 48 && data <= 57) {
                toX *= 10;
                toX += data - 48; 
              } else if (data == '-') {
                minus = true;
              } else {
                return false;
              }
              data = bluetoothSerial.read();
            }
            if (minus) {
              toX = -toX;
            }
            minus = false;
            data = bluetoothSerial.read();
            while (data != '$') {
              if (data >= 48 && data <= 57) {
                toY *= 10;
                toY += data - 48; 
              } else if (data == '-') {
                minus = true;
              } else {
                return false;
              }
              data = bluetoothSerial.read();
            }
            if (minus) {
              toY = -toY;
            }
            minus = false;
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
  bluetoothSerial.println("OK");
  transfer(fromX, fromY, toX, toY);
  bluetoothSerial.println("OK");
  
}

void move(int toX, int toY) {
  //tu trzeba rejestrować przerwanie na rx, w związku ze stop
  Serial.println("---Rozkaz obrotu---");
  Serial.print("Przesuwam hak na pozycje x=");
  Serial.print(toX);
  Serial.print(", y=");
  Serial.println(toY);
  delay(2000);
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
  delay(2000);
}
