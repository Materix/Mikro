/*
 * Bipolar.cpp
 *
 *  Created on: 21 lis 2014
 *      Author: Michal
 */

#include "Bipolar.h"



Bipolar::Bipolar(int A, int nA, int B, int nB, int delayVal) :
	pinA(A),
	pinB(B),
	pinNA(nA),
	pinNB(nB),
	delayTime(delayVal)
{
	pinMode(this->pinA, OUTPUT);
	pinMode(this->pinB, OUTPUT);
	pinMode(this->pinNA, OUTPUT);
	pinMode(this->pinNB, OUTPUT);
	this->phase = 0;
}

Bipolar::~Bipolar() {
	// TODO Auto-generated destructor stub
}

// ujemne steps => ujemny kat
void Bipolar::makeSteps(int steps) {
	int numberOfSteps = abs(steps);
	if (steps < 0) {
		stepRight(numberOfSteps);
	} else {
		stepLeft(numberOfSteps);
	}
}

void Bipolar::hold() {
  this->stepLeft(1);
  this->phase--;
}

void Bipolar::release() {

}

//przeciwnie do wskazowek zegara. Dodatni kat
void Bipolar::stepLeft(int steps) {
	for (int i = 0; i < steps; i++) {
		switch (phase) {
                    case 0:
			digitalWrite(this->pinA, HIGH);
			digitalWrite(this->pinB, HIGH);
			digitalWrite(this->pinNA, LOW);
			digitalWrite(this->pinNB, LOW);
			delay(this->delayTime);
			this->phase = 1;
                        break;
                    case 1:
			digitalWrite(this->pinA, LOW);
			digitalWrite(this->pinB, HIGH);
			digitalWrite(this->pinNA, LOW);
			digitalWrite(this->pinNB, LOW);
			delay(this->delayTime);
			this->phase = 2;
                        break;
                    case 2:
			digitalWrite(this->pinA, LOW);
			digitalWrite(this->pinB, HIGH);
			digitalWrite(this->pinNA, HIGH);
			digitalWrite(this->pinNB, LOW);
			delay(this->delayTime);
			this->phase = 3;
                        break;
                    case 3:
			digitalWrite(this->pinA, LOW);
			digitalWrite(this->pinB, LOW);
			digitalWrite(this->pinNA, HIGH);
			digitalWrite(this->pinNB, LOW);
			delay(this->delayTime);
			this->phase = 4;
                        break;
                    case 4:
			digitalWrite(this->pinA, LOW);
			digitalWrite(this->pinB, LOW);
			digitalWrite(this->pinNA, HIGH);
			digitalWrite(this->pinNB, HIGH);
			delay(this->delayTime);
			this->phase = 5;
                        break;
                    case 5:
			digitalWrite(this->pinA, LOW);
			digitalWrite(this->pinB, LOW);
			digitalWrite(this->pinNA, LOW);
			digitalWrite(this->pinNB, HIGH);
			delay(this->delayTime);
			this->phase = 6;
                        break;
                    case 6:
			digitalWrite(this->pinA, HIGH);
			digitalWrite(this->pinB, LOW);
			digitalWrite(this->pinNA, LOW);
			digitalWrite(this->pinNB, HIGH);
			delay(this->delayTime);
			this->phase = 7;
                        break;
                    case 7:
			digitalWrite(this->pinA, HIGH);
			digitalWrite(this->pinB, LOW);
			digitalWrite(this->pinNA, LOW);
			digitalWrite(this->pinNB, LOW);
			delay(this->delayTime);
			this->phase = 0;
                        break;
		}
	}
}

//zgodnie z wskazowkami zegara. Ujemny kat
void Bipolar::stepRight(int steps) {
	for (int i = 0; i < steps; i++) {
		switch (phase) {
                    case 0:
			digitalWrite(this->pinA, HIGH);
			digitalWrite(this->pinB, HIGH);
			digitalWrite(this->pinNA, LOW);
			digitalWrite(this->pinNB, LOW);
			delay(this->delayTime);
			this->phase = 7;
                        break;
                    case 1:
			digitalWrite(this->pinA, LOW);
			digitalWrite(this->pinB, HIGH);
			digitalWrite(this->pinNA, LOW);
			digitalWrite(this->pinNB, LOW);
			delay(this->delayTime);
			this->phase = 0;
                        break;
                    case 2:
			digitalWrite(this->pinA, LOW);
			digitalWrite(this->pinB, HIGH);
			digitalWrite(this->pinNA, HIGH);
			digitalWrite(this->pinNB, LOW);
			delay(this->delayTime);
			this->phase = 1;
                        break;
                    case 3:
			digitalWrite(this->pinA, LOW);
			digitalWrite(this->pinB, LOW);
			digitalWrite(this->pinNA, HIGH);
			digitalWrite(this->pinNB, LOW);
			delay(this->delayTime);
			this->phase = 2;
                        break;
                    case 4:
			digitalWrite(this->pinA, LOW);
			digitalWrite(this->pinB, LOW);
			digitalWrite(this->pinNA, HIGH);
			digitalWrite(this->pinNB, HIGH);
			delay(this->delayTime);
			this->phase = 3;
                        break;
                    case 5:
			digitalWrite(this->pinA, LOW);
			digitalWrite(this->pinB, LOW);
			digitalWrite(this->pinNA, LOW);
			digitalWrite(this->pinNB, HIGH);
			delay(this->delayTime);
			this->phase = 4;
                        break;
                    case 6:
			digitalWrite(this->pinA, HIGH);
			digitalWrite(this->pinB, LOW);
			digitalWrite(this->pinNA, LOW);
			digitalWrite(this->pinNB, HIGH);
			delay(this->delayTime);
			this->phase = 5;
                        break;
                    case 7:
			digitalWrite(this->pinA, HIGH);
			digitalWrite(this->pinB, LOW);
			digitalWrite(this->pinNA, LOW);
			digitalWrite(this->pinNB, LOW);
			delay(this->delayTime);
			this->phase = 6;
                        break;
                }
	}
}
