/*
 * Bipolar.h
 *
 *  Created on: 21 lis 2014
 *      Author: Michal
 */

#ifndef BIPOLAR_H_
#define BIPOLAR_H_

#include "Arduino.h"

class Bipolar {
public:
	Bipolar(int A, int nA, int B, int nB, int delay);
	virtual ~Bipolar();
	void makeSteps(int steps);
	void hold();
	void release();
private:
	void stepLeft(int steps);
	void stepRight(int steps);
	int pinA;
	int pinB;
	int pinNA;
	int pinNB;
	int delayTime;
	volatile int phase;
};

#endif /* BIPOLAR_H_ */
