package soen6441.team01.warzone.common;

import java.util.ArrayList;

import soen6441.team01.warzone.common.contracts.Observer;

/**
 * Implements the functionality as defined by the Observer design pattern. Used
 * primarily by the Model to communicate data changes to the views.
 *
 */
public class Observable {
	private ArrayList<Observer> d_observers = new ArrayList<Observer>();
	private boolean d_dirty = false;

	/**
	 * Observable constructor
	 */
	public Observable() {
	}

	/**
	 * Register the specified object as an observer, i.e. who wants to be notified
	 * when changes are made to the underlying data.
	 * 
	 * @param observer the observer object that wants to receive notification
	 *                 messages.
	 */
	public void attach(Observer observer) {
		try {
			d_observers.add(observer);
		} catch (Exception ex) {
		}
	}

	/**
	 * De-register an attached object for notification.
	 * 
	 * @param observer the observer object that wants to end notifications.
	 */
	public void detach(Observer observer) {
		try {
			d_observers.remove(observer);
		} catch (Exception ex) {
		}
	}

	/**
	 * if a change in the data is indicated then notify all attached objects that a
	 * change was made to the underlying data.
	 */
	public void notifyObservers() {
		if (d_dirty) {
			for (Observer observer : d_observers) {
				try {
					observer.update(this);
				} catch (Exception ex) {
					detach(observer);
				}

			}
		}
		d_dirty = false;
	}

	/**
	 * indicate that the underlying data has changed in some way
	 */
	public void setChanged() {
		d_dirty = true;
	}
}
