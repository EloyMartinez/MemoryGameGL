package com.snatik.matches.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;

/**
 * The gateway of all events running in the game from ui to engine components
 * and back.
 * 
 * @author sromku
 */
public class EventBus {// Classe dédiée à la gestion du bus d'évènements

	private Handler mHandler;// Handler pour gérer les activités
	private static EventBus mInstance = null;//Instance unique de bus d'évènements
	//Map des évènements
	private final Map<String, List<EventObserver>> events = Collections.synchronizedMap(new HashMap<String, List<EventObserver>>());
	private Object obj = new Object();//Objet à synchroniser

	private EventBus() {
		mHandler = new Handler();
	}

	/**
	 * Permet de récuperer le bus correspondant à l'ensemble des évenements en cours
	 * @return un eventBus représenant l'ensemble des évènements
	 */
	public static EventBus getInstance() {
		if (mInstance == null) {//Création d'une instance unique
			mInstance = new EventBus();
		}
		return mInstance;
	}
	/**
	 * Permet d'assigner un observateur à un certain types d'évènements
	 * @param eventObserver, un observateur d'evenement à assigner aux évènements
	 * @param eventType, un string représentant le type d'évènement que l'on veut attendre
	 */
	synchronized public void listen(String eventType, EventObserver eventObserver) {
		List<EventObserver> observers = events.get(eventType);//Observateur d'évènements
		if (observers == null) {
			observers = Collections.synchronizedList(new ArrayList<EventObserver>());
		}
		observers.add(eventObserver);//Ajout d'un listener
		events.put(eventType, observers);
	}
	/**
	 * Permet d'enlever l'assignationd'un observateur à un certain types d'évènements
	 * @param eventObserver, un observateur d'evenement à désassigner aux évènements
	 * @param eventType, un string représentant le type d'évènement que l'on veut attendre
	 */
	synchronized public void unlisten(String eventType, EventObserver eventObserver) {
		List<EventObserver> observers = events.get(eventType);//Observateurs d'évènements
		if (observers != null) {
			observers.remove(eventObserver);//Suppression du listener
		}
	}
	/**
	 * Permet de notifier les observateurs d'un évènements
	 * @param event, un evenement que l'on veut suivre
	 */
	public void notify(Event event) {
		synchronized (obj) {//Synchronisation de l'objet
			List<EventObserver> observers = events.get(event.getType());//Liste des observateurs
			if (observers != null) {
				for (EventObserver observer : observers) {//Notifier tous les observateurs
					AbstractEvent abstractEvent = (AbstractEvent) event;
					abstractEvent.fire(observer);
				}
			}
		}
	}
	/**
	 * Permet de notifier les observateurs d'un evenement avec un certain délai
	 * @param event, un evenement que l'on veut suivre
	 * @param delay, un nombre représentant la durée à attendre avant d'envoyer la notification
	 */
	public void notify(final Event event, long delay) {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				EventBus.this.notify(event);
			}
		}, delay);
	}

}
