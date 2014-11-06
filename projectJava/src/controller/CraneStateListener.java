package controller;

import model.State;

public interface CraneStateListener {
	public void changed(State newState);
}
