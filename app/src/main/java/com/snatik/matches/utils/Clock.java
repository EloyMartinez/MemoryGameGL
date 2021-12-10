package com.snatik.matches.utils;

import android.util.Log;

public class Clock {
	private static PauseTimer mPauseTimer = null;
	private static Clock mInstance = null;

	private Clock() {
		Log.i("my_tag", "NEW INSTANCE OF CLOCK");
	}

	public static class PauseTimer extends CountDownClock {
		private OnTimerCount mOnTimerCount = null;

		public PauseTimer(long millisOnTimer, long countDownInterval, boolean runAtStart, OnTimerCount onTimerCount) {
			super(millisOnTimer, countDownInterval, runAtStart);
			mOnTimerCount = onTimerCount;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			if (mOnTimerCount != null) {
				mOnTimerCount.onTick(millisUntilFinished);
			}
		}

		@Override
		public void onFinish() {
			if (mOnTimerCount != null) {
				mOnTimerCount.onFinish();
			}
		}

	}
	/**
	 * Permet d'obtenir l'instance de l'horloge de la partie
	 * @return une horloge, correspondant à celle liée à la partie
	 */
	public static Clock getInstance() {
		if (mInstance == null) {
			mInstance = new Clock();
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
			mPauseTimer.cancel();
		}
		mPauseTimer = new PauseTimer(millisOnTimer, countDownInterval, true, onTimerCount);
		mPauseTimer.create();
	}

	/**
	 * Permet de mettre en pause l'horloge
	 */
	public void pause() {
		if (mPauseTimer != null) {
			mPauseTimer.pause();
		}
	}

	/**
	 * Permet de relancer l'horloge
	 */
	public void resume() {
		if (mPauseTimer != null) {
			mPauseTimer.resume();
		}
	}

	/**
	 * Permet d'annuler et de supprimer l'horloge
	 */
	public void cancel() {
		if (mPauseTimer != null) {
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
