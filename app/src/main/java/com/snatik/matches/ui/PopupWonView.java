package com.snatik.matches.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snatik.matches.R;
import com.snatik.matches.common.Music;
import com.snatik.matches.common.Shared;
import com.snatik.matches.events.ui.BackGameEvent;
import com.snatik.matches.events.ui.NextGameEvent;
import com.snatik.matches.model.GameState;
import com.snatik.matches.utils.Clock;
import com.snatik.matches.utils.Clock.OnTimerCount;
import com.snatik.matches.utils.FontLoader;
import com.snatik.matches.utils.FontLoader.Font;

public class PopupWonView extends RelativeLayout {//Classe dédiée à la gestion de la popup de victoire

	private TextView mTime;//temps
	private TextView mScore;//score
	private ImageView mStar1;//première étoile (difficulté facile)
	private ImageView mStar2;//première étoile (difficulté moyenne)
	private ImageView mStar3;//première étoile (difficulté difficile)
	private ImageView mNextButton;//bouton de nouvelle partie
	private ImageView mBackButton;//bouton de retour au jeu
	private Handler mHandler;//handler de l'activité

	public PopupWonView(Context context) {
		this(context, null);
	}

	public PopupWonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//Réglage de la vue de la popup
		LayoutInflater.from(context).inflate(R.layout.popup_won_view, this, true);
		mTime = (TextView) findViewById(R.id.time_bar_text);
		mScore = (TextView) findViewById(R.id.score_bar_text);
		mStar1 = (ImageView) findViewById(R.id.star_1);
		mStar2 = (ImageView) findViewById(R.id.star_2);
		mStar3 = (ImageView) findViewById(R.id.star_3);
		mBackButton = (ImageView) findViewById(R.id.button_back);
		mNextButton = (ImageView) findViewById(R.id.button_next);
		FontLoader.setTypeface(context, new TextView[] { mTime, mScore }, Font.GROBOLD);//Police
		setBackgroundResource(R.drawable.level_complete);
		mHandler = new Handler();
		
		mBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Shared.eventBus.notify(new BackGameEvent());
			}
		});
		
		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Shared.eventBus.notify(new NextGameEvent());
			}
		});
	}

	/**
	 * Permet de mettre à jour l'affichage des informations sur la partie actuelle (score, temps, ...)
	 * @param  gameState, état du jeu représentant l'avancement actuel de la partie
	 */
	public void setGameState(final GameState gameState) {
		int min = gameState.remainedSeconds / 60;
		int sec = gameState.remainedSeconds - min * 60;
		mTime.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec));
		mScore.setText("" + 0);
		
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {//animation de victoire lancée après un certain délai
				animateScoreAndTime(gameState.remainedSeconds, gameState.achievedScore);
				animateStars(gameState.achievedStars);
			}
		}, 500);
	}

	/**
	 * Permet de représenter l'affichage du score obtenu, ceci en affichant de 0 à 3 étoiles
	 * @param  start, un entier représentant le nombre d'étoiles obtenu lors de la partie
	 */
	private void animateStars(int start) {
		//En fonction du nombre d'étoiles débloquées
		switch (start) {
			case 0:
				choice0();
				break;
			case 1:
				choice1();
				break;
			case 2:
				choice2();
				break;
			case 3:
				choice3();
				break;
			default:
				break;
		}
	}


	private void choice0(){
		mStar1.setVisibility(View.GONE);
		mStar2.setVisibility(View.GONE);
		mStar3.setVisibility(View.GONE);
	}

	private void choice1(){
		mStar2.setVisibility(View.GONE);
		mStar3.setVisibility(View.GONE);
		mStar1.setAlpha(0f);
		animateStar(mStar1, 0);
	}

	private void choice2(){
		mStar3.setVisibility(View.GONE);
		mStar1.setVisibility(View.VISIBLE);
		mStar1.setAlpha(0f);
		animateStar(mStar1, 0);
		mStar2.setVisibility(View.VISIBLE);
		mStar2.setAlpha(0f);
		//Animation avec un peu de délai pour donner un style
		animateStar(mStar2, 600);
	}

	private void choice3(){
		mStar1.setVisibility(View.VISIBLE);
		mStar1.setAlpha(0f);
		animateStar(mStar1, 0);
		mStar2.setVisibility(View.VISIBLE);
		mStar2.setAlpha(0f);
		animateStar(mStar2, 600);
		mStar3.setVisibility(View.VISIBLE);
		mStar3.setAlpha(0f);
		animateStar(mStar3, 1200);
	}

	/**
	 * Permet d'animer une étoile représentative du score du joueur
	 * @param view, une vue représentative de l'étoile à animer
	 * @param delay, un entier correspondant au délai à attendre avant de lancer l'animation d'affichage
	 */
	private void animateStar(final View view, int delay) {
		ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);
		alpha.setDuration(100);
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0, 1f);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0, 1f);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(alpha, scaleX, scaleY);
		animatorSet.setInterpolator(new BounceInterpolator());
		animatorSet.setStartDelay(delay);
		animatorSet.setDuration(600);
		view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		animatorSet.start();
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Music.showStar();
			}
		}, delay);
	}

	/**
	 * Permet de gérer l'animation du score et du temps restant d'une partie
	 * @param achievedScore, un entier correspondant au score actuel du joueur
	 * @param remainedSeconds, un entier correspondant au temps restant au joueur pour associer les paires
	 */
	private void animateScoreAndTime(final int remainedSeconds, final int achievedScore) {
		final int totalAnimation = 1200;

		Clock.getInstance().startTimer(totalAnimation, 35, new OnTimerCount() {
			//à la fin de l'horloge
			@Override
			public void onTick(long millisUntilFinished) {
				float factor = millisUntilFinished / (totalAnimation * 1f); // 0.1
				int scoreToShow = achievedScore - (int) (achievedScore * factor);
				int timeToShow = (int) (remainedSeconds * factor);
				int min = timeToShow / 60;
				int sec = timeToShow - min * 60;
				mTime.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec));
				mScore.setText("" + scoreToShow);
			}
			//à la suppression de l'horloge
			@Override
			public void onFinish() {
				mTime.setText(" " + String.format("%02d", 0) + ":" + String.format("%02d", 0));
				mScore.setText("" + achievedScore);
			}
		});
	}
}
