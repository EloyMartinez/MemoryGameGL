package com.snatik.matches.events.ui;

import com.snatik.matches.events.AbstractEvent;
import com.snatik.matches.events.EventObserver;

/**
 * When the 'back to menu' was pressed.
 */
public class FlipCardEvent extends AbstractEvent {//Classe dédiée à l'évènement de retournement d'une carte

	public static final String TYPE = FlipCardEvent.class.getName();

	public final int id;//Carte à retourner
	
	public FlipCardEvent(int id) {
		this.id = id;
	}
	
	@Override
	protected void fire(EventObserver eventObserver) {
		eventObserver.onEvent(this);
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
