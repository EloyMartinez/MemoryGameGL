package com.snatik.matches.events.engine;

import com.snatik.matches.events.AbstractEvent;
import com.snatik.matches.events.EventObserver;

/**
 * When the 'back to menu' was pressed.
 */
public class HidePairCardsEvent extends AbstractEvent {//Classe dédiée au masquage d'une paire de cartes

	public static final String TYPE = HidePairCardsEvent.class.getName();
	public int id1;//Première carte de la paire
	public int id2;//Deuxième carte de la paire

	public HidePairCardsEvent(int id1, int id2) {
		this.id1 = id1;
		this.id2 = id2;
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
