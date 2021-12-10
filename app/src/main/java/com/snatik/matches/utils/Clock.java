package com.snatik.matches.utils;

import android.util.Log;

public class Clock {//Classe dédiée au fonctionnement du chronomètre
	private static PauseTimer mPauseTimer = null;//Instance unique de timer
	private static Clock mInstance = null;//Instance unique de l'horloge

	private Clock() {
		Log.i("my_tag", "NEW INSTANCE OF CLOCK");
	}

	public static class PauseTimer extends CountDownClock {//Classe interne dédiée à la gestion d'un timer
		private OnTimerCount mOnTimerCount = null;//Suppression du compteur du timer

		public PauseTimer(long millisOnTimer, long countDownInterval, boolean runAtStart, OnTimerCount onTimerCount) {
			super(millisOnTimer, countDownInterval, runAtStart);
			mOnTimerCount = onTimerCount;
		}

		//En cas de fin du timer
		@Override
		public void onTick(long millisUntilFinished) {
			if (mOnTimerCount != null) {//Verification qu'une instance de compteur de timer existe
				mOnTimerCount.onTick(millisUntilFinished);//L'arrêter
			}
		}

		//En cas de suppression du timer
		@Override
		public void onFinish() {
			if (mOnTimerCount != null) {//Verification qu'une instance de compteur de timer existe
				mOnTimerCount.onFinish();//La supprimer
			}
		}

	}
	/**
	 * Permet d'obtenir l'instance de l'horloge de la partie
	 * @return une horloge, correspondant à celle liée à la partie
	 */
	public static Clock getInstance() {
		if (mInstance == null) {
			mInstance = new Clock();//Création d'une nouvelle horloge s'il n'y en a pas deja : instance unique
		}
		return mInstance;
	}

	/**
	 * Permet de lancer un timer
	 * @param millisOnTimer, un nombre représentant la durée que l'on souhaite mettre sur le timer
	 * @param countDownInterval, un nombre représentant la durée à compter entre deux valeurs du timer
	 * @param onTimerCount, un manager de timer, permettant de définir les actions
	 *                        qu'il faudra réaliser à la fin du timer
	 */
	public void startTimer(long millisOnTimer, long countDownInterval, OnTimerCount onTimerCount) {
		if (mPauseTimer != null) {
			mPauseTimer.cancel();//Supprimer le timer de pause
		}
		mPauseTimer = new PauseTimer(millisOnTimer, countDownInterval, true, onTimerCount);
		mPauseTimer.create();//En créer un nouveau avec le temps et l'intervalle souhaité
	}

	/**
	 * Permet de mettre en pause l'horloge
	 */
	public void pause() {
		if (mPauseTimer != null) {//Vérification de l'existence de l'instance
			mPauseTimer.pause();
		}
	}

	/**
	 * Permet de relancer l'horloge
	 */
	public void resume() {
		if (mPauseTimer != null) {//Vérification de l'existence de l'instance
			mPauseTimer.resume();
		}
	}

	/**
	 * Permet d'annuler et de supprimer l'horloge
	 */
	public void cancel() {
		if (mPauseTimer != null) {//Vérification de l'existence de l'instance
			mPauseTimer.mOnTimerCount = null;
			mPauseTimer.cancel();
		}
	}
	/**
	 * Permet d'obtenir le temps passé depuis le début de la partie
	 */
	public long getPassedTime() {
		return mPauseTimer.timePassed();
	}

	public interface OnTimerCount {
		public void onTick(long millisUntilFinished);

		public void onFinish();
	}

}
