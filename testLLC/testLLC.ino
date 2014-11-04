//przy podłączaniu należy obydwie ziemie wpiąć do ziemi, nie łączyć ich ze sobą

void setup() {
  pinMode(8, OUTPUT);
  pinMode(A0, INPUT);
  Serial.begin(9600);
}

void loop() {
  digitalWrite(channel1out, HIGH);
  Serial.println("----");
  Serial.println("Wysokie");
  Serial.print("Napiecie na A0 (channel1 out) ");
  Serial.println(((double)(analogRead(A0)) / 1024) * 5);
  delay(1000);
  digitalWrite(8, LOW);
  Serial.println("Niskie");
  Serial.print("Napiecie na A0 (channel1 out) ");
  Serial.println(((double)(analogRead(A0)) / 1024) * 5);
  delay(1000);
}
