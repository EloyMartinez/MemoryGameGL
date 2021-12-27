package com.snatik.matches.common;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.snatik.matches.R;

public class Music {//Classe destinée à gérer la musique du jeu

	public static boolean OFF = false;//Etat du lecteur de musique (activé ou éteint)

	/**
	 * Lance la musique actuelle dans le jeu
	 */
	public static void playCurrent() {
		if (!OFF) {
			MediaPlayer mp = MediaPlayer.create(Shared.context, R.raw.correct_answer);//Accès au lecteur musical
			// + initialisation de la musique pour une réponse correcte
			mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.reset();
					mp.release();
					mp = null;
				}

			});//Vidage du cache du lecteur
			mp.start();//Lancement du lecteur
		}
	}

	/**
	 * Lance la musique d'animation pour le débloquage d'une nouvelle étoile
	 */
	public static void showStar() {
		if (!OFF) {//Vérification que le lecteur est activé
			MediaPlayer mp = MediaPlayer.create(Shared.context, R.raw.star);//Accès au lecteur de musique
			// + initialisation de la musique pour l'animation des étoiles
			mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.reset();
					mp.release();
					mp = null;
				}

			});//Vidage du cache du lecteur
			mp.start();//Lancement du lecteur
		}
	}
}
