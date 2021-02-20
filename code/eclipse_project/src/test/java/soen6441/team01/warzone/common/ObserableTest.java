package soen6441.team01.warzone.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import soen6441.team01.warzone.common.contracts.Observer;

/**
 * Junit 4.0 tests for the Observable class
 *
 */
public class ObserableTest extends Observable implements Observer {

	private int d_observer_call = 0;
	public boolean d_was_updated = false;

	/**
	 * setup the attributes of this instance before each test
	 */
	@Before
	public void test_methodSetup() {
		d_observer_call = 0;
	}

	/**
	 * simple test of our custom observer class.
	 */
	@Test
	public void test_simpleObserPattern_1() {
		this.attach(this);
		this.notifyObservers();
		assertTrue(d_observer_call == 0);
		assertFalse(d_was_updated);
		this.setChanged();
		this.notifyObservers();
		assertTrue(d_observer_call == 1);
		assertTrue(d_was_updated);
	}

	/**
	 * this is not a test case, but is used in testing by the other observer test
	 * case(s). Note that the update function is normally declared in the view and
	 * p_observed is the model. if a View is attached to more than 1 model then the
	 * view will need to keep a reference to all the attached models so that it can
	 * know which model is being updated (as shown in this test method).
	 */
	@Override
	public void update(Observable p_observed) {
		assertSame(this, p_observed);
		d_observer_call = 1;
		if (p_observed == this) {
			((ObserableTest) p_observed).d_was_updated = true; // this would be the model
		}
	}

}
