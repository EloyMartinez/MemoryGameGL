package com.snatik.matches.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;

import com.snatik.matches.R;
import com.snatik.matches.common.Shared;
import com.snatik.matches.events.ui.FlipCardEvent;
import com.snatik.matches.model.BoardArrangment;
import com.snatik.matches.model.BoardConfiguration;
import com.snatik.matches.model.Game;
import com.snatik.matches.utils.Utils;

public class BoardView extends LinearLayout {//Classe dédiée à la vue de la grille

	private LinearLayout.LayoutParams mRowLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);//Layout pour gérer les lignes
	private LinearLayout.LayoutParams mTileLayoutParams;//Layout pour gérer les tuiles
	private int mScreenWidth;//Largeur de l'écran
	private int mScreenHeight;//Hauteur de l'écran
	private BoardConfiguration mBoardConfiguration;//Instance unique de configuration de la grille
	private BoardArrangment mBoardArrangment;//Instance unique d'arrangement de la grille
	private Map<Integer, TileView> mViewReference;//Liste des vues des tuiles
	private List<Integer> flippedUp = new ArrayList<Integer>();//Liste des cartes retournées
	private boolean mLocked = false;
	private int mSize;

	public BoardView(Context context) {
		this(context, null);
	}

	public BoardView(Context context, AttributeSet attributeSet) {
		//Fixation de la vue de la grille
		super(context, attributeSet);
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		int margin = getResources().getDimensionPixelSize(R.dimen.margine_top);
		int padding = getResources().getDimensionPixelSize(R.dimen.board_padding);
		//Obtention des réglages de l'appareil
		mScreenHeight = getResources().getDisplayMetrics().heightPixels - margin - padding*2;
		mScreenWidth = getResources().getDisplayMetrics().widthPixels - padding*2 - Utils.px(20);
		mViewReference = new HashMap<Integer, TileView>();
		setClipToPadding(false);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	public static BoardView fromXml(Context context, ViewGroup parent) {
		return (BoardView) LayoutInflater.from(context).inflate(R.layout.board_view, parent, false);
	}
	/**
	 * Permet d'initaliser la grille de tuiles en fonction de la partie
	 * @param game, un jeu correspondant à la partie actuelle
	 */
	public void setBoard(Game game) {
		mBoardConfiguration = game.boardConfiguration;
		mBoardArrangment = game.boardArrangment;
		// calc prefered tiles in width and height
		int singleMargin = getResources().getDimensionPixelSize(R.dimen.card_margin);
		float density = getResources().getDisplayMetrics().density;
		singleMargin = Math.max((int) (1 * density), (int) (singleMargin - mBoardConfiguration.difficulty * 2 * density));
		int sumMargin = 0;
		for (int row = 0; row < mBoardConfiguration.numRows; row++) {
			sumMargin += singleMargin * 2;
		}
		int tilesHeight = (mScreenHeight - sumMargin) / mBoardConfiguration.numRows;//Hauteurs des tuiles
		int tilesWidth = (mScreenWidth - sumMargin) / mBoardConfiguration.numTilesInRow;//Largeurs des tuiles
		mSize = Math.min(tilesHeight, tilesWidth);

		mTileLayoutParams = new LinearLayout.LayoutParams(mSize, mSize);
		mTileLayoutParams.setMargins(singleMargin, singleMargin, singleMargin, singleMargin);

		// build the ui
		buildBoard();
	}

	/**
	 * Permet de construire la grille de jeu de la partie
	 */
	private void buildBoard() {

		for (int row = 0; row < mBoardConfiguration.numRows; row++) {
			// add row
			addBoardRow(row);
		}

		setClipChildren(false);
	}
	/**
	 * Permet de fixer le nombre de lignes de la grille
	 */
	private void addBoardRow(int rowNum) {
		LinearLayout linearLayout = new LinearLayout(getContext());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setGravity(Gravity.CENTER);

		for (int tile = 0; tile < mBoardConfiguration.numTilesInRow; tile++) {
			addTile(rowNum * mBoardConfiguration.numTilesInRow + tile, linearLayout);//Ajout des tuiles
		}

		// add to this view
		addView(linearLayout, mRowLayoutParams);
		linearLayout.setClipChildren(false);
	}
	/**
	 * Permet d'ajouter une tuile à la grille
	 * @param id, un entier correspondant à la position de la tuile
	 * @param parent, un groupe de vue représentatif de la grille
	 */
	private void addTile(final int id, ViewGroup parent) {
		final TileView tileView = TileView.fromXml(getContext(), parent);
		tileView.setLayoutParams(mTileLayoutParams);
		parent.addView(tileView);
		parent.setClipChildren(false);
		mViewReference.put(id, tileView);
		asyncTask(id, tileView);
		//Gestion du clic sur une des tuiles
		tileView.setOnClickListener(OnCliclListenerTileView(id, tileView));

		//Animations visuelles
		ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(tileView, "scaleX", 0.8f, 1f);
		scaleXAnimator.setInterpolator(new BounceInterpolator());
		ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(tileView, "scaleY", 0.8f, 1f);
		scaleYAnimator.setInterpolator(new BounceInterpolator());
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
		animatorSet.setDuration(500);
		tileView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		animatorSet.start();
	}
	/**
	 * Permet de rabattre toutes les tuiles de la grille
	 */
	public void flipDownAll() {
		for (Integer id : flippedUp) {
			mViewReference.get(id).flipDown();
		}
		flippedUp.clear();
		mLocked = false;
	}
	/**
	 * Permet de retourner en face cachée deux cartes lorsqu'elles ne sont pas paires
	 * @param id1, un entier correspondant à la position de la première tuile
	 * @param id2, un entier correspondant à la position de la deuxième tuile
	 */
	public void hideCards(int id1, int id2) {
		//animation de masquage de cartes
		animateHide(mViewReference.get(id1));
		animateHide(mViewReference.get(id2));
		flippedUp.clear();
		mLocked = false;
	}
	/**
	 * Permet de générer une animation de rotation de cartes lorsque l'on souhaite la cacher
	 * @param v, une vue de tuile, correspondant à la tuile que l'on veut retourner
	 */
	protected void animateHide(final TileView v) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(v, "alpha", 0f);
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				v.setLayerType(View.LAYER_TYPE_NONE, null);
				v.setVisibility(View.INVISIBLE);
			}
		});//Listener d'animation
		animator.setDuration(100);
		v.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		animator.start();
	}

	private AsyncTask<Void, Void, Bitmap> asyncTask(final int id, final TileView tileView){
		return new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				return mBoardArrangment.getTileBitmap(id, mSize);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				tileView.setTileImage(result);
			}
		}.execute();
	}

	private View.OnClickListener OnCliclListenerTileView(final int id, final TileView tileView){
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mLocked && tileView.isFlippedDown()) {
					tileView.flipUp();
					flippedUp.add(id);
					if (flippedUp.size() == 2) {
						mLocked = true;
					}
					Shared.eventBus.notify(new FlipCardEvent(id));
				}
			}
		};
	}
}
