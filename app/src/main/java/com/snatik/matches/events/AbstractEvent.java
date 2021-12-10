package com.snatik.matches.events;

public abstract class AbstractEvent implements Event {//Classe dédiée à l'apparition d'évènements abstraits

	protected abstract void fire(EventObserver eventObserver);

}
