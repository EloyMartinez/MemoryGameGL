package com.snatik.matches.events.ui;

import com.snatik.matches.events.AbstractEvent;
import com.snatik.matches.events.EventObserver;

/**
 * When the 'back to menu' was pressed.
 */
public class BackGameEvent extends AbstractEvent {//Classe dédiée à l'évènement de retour au jeu

	public static final String TYPE = BackGameEvent.class.getName();

	@Override
	protected void fire(EventObserver eventObserver) {
		eventObserver.onEvent(this);
	}

	@Override
	public String getType() {
		return TYPE;
	}
}
