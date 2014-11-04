package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TransferTest {
	
	Transfer tested;

	@Before
	public void setUp() throws Exception {
		tested = new Transfer(new Coord(100, 100), new Coord(200, 200));
	}

	@Test
	public void testToSend() {
		System.out.println(tested.toSend());
	}

}
