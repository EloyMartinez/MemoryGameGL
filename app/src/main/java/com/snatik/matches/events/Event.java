package com.snatik.matches.events;


/**
 * The event that is invoked from the low levels of this game (like engine) and
 * not from the ui.
 * 
 * @author sromku
 */
public interface Event {//Interface dédiée au fonctionnement des évènements

	String getType();
}
