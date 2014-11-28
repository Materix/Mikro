#include <SoftwareSerial.h>
#include <math.h>
#include <Stepper.h>
#include "Bipolar.h"

#define MIN_RADIUS (10) // minimalny promień w krokach
#define MAX_RADIUS (10) // maksymalny promień w krokach
#define ARM_LENGHT (41) // Maksymalny zasięg ramienia w centymetrach. Mierzona od środka.
#define MAX_HEIGHT (1000) // maksymalna ilość kroków na jaką możemy opuścić hak
#define STEP_PER_ROTATE_BIPOLAR (400)  // ilość kroków na obrót


SoftwareSerial bluetoothSerial(10, 11); //RX, TX
Bipolar armEngine = Bipolar(2, 3, 4, 5, 200); //A, nA, B, nB, delay
Bipolar hookEngine = Bipolar(6, 7, 8, 9, 10); //A, nA, B, nB, delay

//pełen obrót ramienia to 400 kroków <- działa, ale lepiej nie ruszać. NIezbyt dobrze zablokowana podstawa
//pełen obrót podnośnika to około 7,5cm

char data;
int currentHeight; // ilość kroków
int currentAngle; // ilość kroków
int currentRadius; // ilość kroków

int targetHeight; // póki co bezużyteczne
int targetAngle;
int targetRadius;

void setup() {
  delay(5000);
  Serial.begin(9600);
  bluetoothSerial.begin(9600);
  bluetoothSerial.print("AT");
  while(!bluetoothSerial.available()) {
    ;
  }
  initialize(); // inicjalizacja dźwiga
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

// inicjalizacja czyli 
// ustawienie haka na maksymalną wysokość, ustawienie wysokości na 0 kroków
// ustawienie wózka na minimalną odległość, ustawienie odległości na MIN_RADIUS
void initialize() {
  Serial.println("Inicjuje sie");
  currentAngle = 0;
  // TODO tu powinno znaleść się podnoszenie haka na maksymalną wysokość
  currentHeight = 0;
  // TODO tu powinno znaleść się przesuwanie wózka na minimalną odległość
  currentRadius = MIN_RADIUS;
  armEngine.hold();
  hookEngine.hold();
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
  Serial.print(toX);
  Serial.print(" ");
  Serial.print(toY);
  Serial.println(" ");
  cartesianToStep(toX, toY); // mamy docelowe wartości w targetRadius i targetAngle
  Serial.print(targetRadius);
  Serial.print(" ");
  Serial.println(targetAngle);
  // tymczasowe ograniczenie
  if (abs(targetAngle) > 25) {
    if (targetAngle > 0) {
      targetAngle = 25;
    } else {
      targetAngle = -25;
    }
  }
  int steps = targetAngle - currentAngle;
  Serial.println(steps);
  if (abs(steps) > (STEP_PER_ROTATE_BIPOLAR / 2)) {
    if (steps > 0) {
      steps = steps - STEP_PER_ROTATE_BIPOLAR;
    } else {
      steps = steps + STEP_PER_ROTATE_BIPOLAR;
    }
  }
  armEngine.makeSteps(steps); // te silnik działa na odwwrót. + jest zgodnie z zegarem. obór zrobiony, teraz ruch wózka
  steps = targetRadius - currentRadius; // ujemne kroki -> wózek do tyłu
  if (steps < MIN_RADIUS || steps > MAX_RADIUS) {
    targetRadius = currentRadius;
    steps = 0;
  }
  //cartEngine.makeSteps(steps)
  currentAngle = targetAngle;
  currentRadius = targetRadius;
}

void transfer(int fromX, int fromY, int toX, int toY) {
  move(fromX, fromY);
  // tu musi być dodana funkcja która obsługuje akceleromenrt. Jakiś while czy cuś
  Serial.println("Chwytam przedmiot");
  hookEngine.makeSteps(800);
  hookEngine.makeSteps(-800);
  move(toX, toY);
  Serial.println("Opuszczam przedmiot");
  hookEngine.makeSteps(800);
  hookEngine.makeSteps(-800);
  delay(2000);
}

// przelicza podane współrzędne na wartości kroków. Wynik zapisuje w targetAngle i targetRadius
void cartesianToStep(int x, int y) {
  double angle = 0, radius = 0;
  radius = sqrt((double)x * (double)x + (double)y * (double)y); // promień w cm
  angle = atan2((double)y, (double)x); // kąt w radianach
  targetRadius = (int)((radius * MAX_RADIUS) / ARM_LENGHT);
  targetAngle = (int)((angle * STEP_PER_ROTATE_BIPOLAR) / (2 * M_PI));
}
