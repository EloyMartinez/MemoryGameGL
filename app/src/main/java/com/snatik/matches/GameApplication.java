package com.snatik.matches;

import android.app.Application;

import com.snatik.matches.utils.FontLoader;

public class GameApplication extends Application {//Classe destinée à gérer le fonctionnement du jeu

	@Override
	public void onCreate() {
		super.onCreate();
		FontLoader.loadFonts(this);//Chargement des polices
	}
}
