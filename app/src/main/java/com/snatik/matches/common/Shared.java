package com.snatik.matches.common;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.snatik.matches.engine.Engine;
import com.snatik.matches.events.EventBus;

public class Shared {//Classe destinée à l'accès aux éléments de l'application

	public static Context context;//Accès au contexte actuel
	public static FragmentActivity activity; //Accès à l'activité actuelle
	public static Engine engine;//Accès au moteur du jeu
	public static EventBus eventBus;//Accès à la liste des évènements
}
