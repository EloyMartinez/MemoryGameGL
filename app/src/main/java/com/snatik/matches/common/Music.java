package com.snatik.matches.common;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.snatik.matches.R;

public class Music {

	public static boolean OFF = false;

	/**
	 * Lance la musique actuelle dans le jeu
	 */
	public static void playCurrent() {
		if (!OFF) {
			MediaPlayer mp = MediaPlayer.create(Shared.context, R.raw.correct_answer);
			mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.reset();
					mp.release();
					mp = null;
				}

			});
			mp.start();
		}
	}

	/**
	 * Lance la musique d'animation pour le débloquage d'une nouvelle étoile
	 */
	public static void showStar() {
		if (!OFF) {
			MediaPlayer mp = MediaPlayer.create(Shared.context, R.raw.star);
			mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.reset();
					mp.release();
					mp = null;
				}

			});
			mp.start();
		}
	}
}
