package com.snatik.matches.engine;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ImageView;

import com.snatik.matches.R;
import com.snatik.matches.common.Memory;
import com.snatik.matches.common.Music;
import com.snatik.matches.common.Shared;
import com.snatik.matches.engine.ScreenController.Screen;
import com.snatik.matches.events.EventObserverAdapter;
import com.snatik.matches.events.engine.FlipDownCardsEvent;
import com.snatik.matches.events.engine.GameWonEvent;
import com.snatik.matches.events.engine.HidePairCardsEvent;
import com.snatik.matches.events.ui.BackGameEvent;
import com.snatik.matches.events.ui.DifficultySelectedEvent;
import com.snatik.matches.events.ui.FlipCardEvent;
import com.snatik.matches.events.ui.NextGameEvent;
import com.snatik.matches.events.ui.ResetBackgroundEvent;
import com.snatik.matches.events.ui.StartEvent;
import com.snatik.matches.events.ui.ThemeSelectedEvent;
import com.snatik.matches.model.BoardArrangment;
import com.snatik.matches.model.BoardConfiguration;
import com.snatik.matches.model.Game;
import com.snatik.matches.model.GameState;
import com.snatik.matches.themes.Theme;
import com.snatik.matches.themes.Themes;
import com.snatik.matches.ui.PopupManager;
import com.snatik.matches.utils.Clock;
import com.snatik.matches.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Engine extends EventObserverAdapter {//Classe destinée à gérer le moteur du jeu

	private static Engine mInstance = null;//Instance unique du moteur
	private Game mPlayingGame = null;//Instance unique du jeu
	private int mFlippedId = -1;//Carte retournée
	private int mToFlip = -1;//Carte à retourner
	private ScreenController mScreenController;//Instance unique du controleur de l'ecran
	private Theme mSelectedTheme;//Theme selectionné
	private ImageView mBackgroundImage;//Image de fond
	private Handler mHandler;//Manager d'évènements

	private Engine() {
		mScreenController = ScreenController.getInstance();//Récupération de l'instance du controleur de l'écran
		mHandler = new Handler();
	}

	//Retourne l'instance unique du moteur de jeu
	public static Engine getInstance() {
		if (mInstance == null) {//S'il n'y a pas d'instances, alors la créer
			mInstance = new Engine();
		}
		return mInstance;
	}

	/**
	 * Permet de lancer le moteur qui va gérer le déroulement d'une partie
	 */
	public void start() {
		//Mise en place des listener, permettant de surveiller l'apparition d'évènements à réaliser
		Shared.eventBus.listen(DifficultySelectedEvent.TYPE, this);//Evènement lié à la sélection de la difficulté
		Shared.eventBus.listen(FlipCardEvent.TYPE, this);//Evènement lié au retournement d'une carte
		Shared.eventBus.listen(StartEvent.TYPE, this);//Evènement lié à un lancement de partie
		Shared.eventBus.listen(ThemeSelectedEvent.TYPE, this);//Evènement lié à une sélection d'un nouveau thème
		Shared.eventBus.listen(BackGameEvent.TYPE, this);//Evènement lié à un retour en arrière de l'application
		Shared.eventBus.listen(NextGameEvent.TYPE, this);//Evènement lié au lancement d'une nouvelle partie
		Shared.eventBus.listen(ResetBackgroundEvent.TYPE, this);//Evènement lié à un reset du fond d'ecran de l'application
	}
	/**
	 * Permet d'arrêter le moteur qui va gérer le déroulement d'une partie
	 */
	public void stop() {
		//Fixer l'ensemble des instances nécéssaires au fonctionnement de l'application à null
		mPlayingGame = null;
		mBackgroundImage.setImageDrawable(null);//Suppression du cache de l'image de fond
		mBackgroundImage = null;
		mHandler.removeCallbacksAndMessages(null);//Suppression du cache des evènements
		mHandler = null;

		//Arret des listeners, fin de la surveillance des évènements à réaliser
		Shared.eventBus.unlisten(DifficultySelectedEvent.TYPE, this);
		Shared.eventBus.unlisten(FlipCardEvent.TYPE, this);
		Shared.eventBus.unlisten(StartEvent.TYPE, this);
		Shared.eventBus.unlisten(ThemeSelectedEvent.TYPE, this);
		Shared.eventBus.unlisten(BackGameEvent.TYPE, this);
		Shared.eventBus.unlisten(NextGameEvent.TYPE, this);
		Shared.eventBus.unlisten(ResetBackgroundEvent.TYPE, this);

		//Suppresion de l'instance du moteur, une fois toutes les autres tâches réalisées
		mInstance = null;
	}

	//En cas d'évènement de changement du fond d'écran
	@Override
	public void onEvent(ResetBackgroundEvent event) {
		Drawable drawable = mBackgroundImage.getDrawable();
		if (drawable != null) {//S'il n'y a aucun fond d'écran en mémoire, en mettre un de base
			((TransitionDrawable) drawable).reverseTransition(2000);
		} else {
			new AsyncTask<Void, Void, Bitmap>() {

				@Override
				protected Bitmap doInBackground(Void... params) {//Réglage de l'image en tâche asynchrone
					Bitmap bitmap = Utils.scaleDown(R.drawable.background, Utils.screenWidth(), Utils.screenHeight());
					return bitmap;
				}
				//Fixation de l'image une fois que la tâche de fond a été exécuté
				protected void onPostExecute(Bitmap bitmap) {
					mBackgroundImage.setImageBitmap(bitmap);
				};

			}.execute();
		}
	}
	//En cas d'évènement de démarrage de jeu
	@Override
	public void onEvent(StartEvent event) {
		mScreenController.openScreen(Screen.THEME_SELECT);
	}
	//En cas d'évènement de lancement d'une nouvelle partie
	@Override
	public void onEvent(NextGameEvent event) {
		PopupManager.closePopup();//Fermer le pop up
		int difficulty = mPlayingGame.boardConfiguration.difficulty;
		if (mPlayingGame.gameState.achievedStars == 3 && difficulty < 6) {
			difficulty++;//Augmentation du niveau si le niveau précédent a été parfaitement réalisé
		}
		Shared.eventBus.notify(new DifficultySelectedEvent(difficulty));//Notification de changement de niveau
	}
	//En cas d'évènement de retour au jeu
	@Override
	public void onEvent(BackGameEvent event) {
		PopupManager.closePopup();//Fermer le popup
		mScreenController.openScreen(Screen.DIFFICULTY);//Revenir au jeu
	}
	//En cas d'évènement de changement de thème
	@Override
	public void onEvent(ThemeSelectedEvent event) {
		mSelectedTheme = event.theme;
		mScreenController.openScreen(Screen.DIFFICULTY);
		AsyncTask<Void, Void, TransitionDrawable> task = new AsyncTask<Void, Void, TransitionDrawable>() {

			//Réglage du thème en arrière plan
			@Override
			protected TransitionDrawable doInBackground(Void... params) {
				Bitmap bitmap = Utils.scaleDown(R.drawable.background, Utils.screenWidth(), Utils.screenHeight());
				Bitmap backgroundImage = Themes.getBackgroundImage(mSelectedTheme);
				backgroundImage = Utils.crop(backgroundImage, Utils.screenHeight(), Utils.screenWidth());//Fixation de l'image de fond
				Drawable backgrounds[] = new Drawable[2];
				backgrounds[0] = new BitmapDrawable(Shared.context.getResources(), bitmap);
				backgrounds[1] = new BitmapDrawable(Shared.context.getResources(), backgroundImage);
				TransitionDrawable crossfader = new TransitionDrawable(backgrounds);
				return crossfader;
			}

			//Execution de la tâche une fois que celle ci est chargée
			@Override
			protected void onPostExecute(TransitionDrawable result) {
				super.onPostExecute(result);
				mBackgroundImage.setImageDrawable(result);
				result.startTransition(2000);
			}
		};
		task.execute();
	}
	//En cas d'évènement de changement de difficulté
	@Override
	public void onEvent(DifficultySelectedEvent event) {
		mFlippedId = -1;//initialisation des cartes à retourner en version initiale
		mPlayingGame = new Game();//Création d'un nouveau jeu
		mPlayingGame.boardConfiguration = new BoardConfiguration(event.difficulty);//Nouvelle grille de jeu
		mPlayingGame.theme = mSelectedTheme;//Thème de la partie
		mToFlip = mPlayingGame.boardConfiguration.numTiles;//Nombre de tuiles

		//Réarrangement de la grille
		arrangeBoard();

		//Lancement de l'écran
		mScreenController.openScreen(Screen.GAME);
	}
	/**
	 * Permet d'arranger la grille de tuiles
	 */
	private void arrangeBoard() {
		BoardConfiguration boardConfiguration = mPlayingGame.boardConfiguration;
		BoardArrangment boardArrangment = new BoardArrangment();

		// build pairs
		// result {0,1,2,...n} // n-number of tiles
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < boardConfiguration.numTiles; i++) {
			ids.add(i);
		}
		// shuffle
		// result {4,10,2,39,...}
		Collections.shuffle(ids);

		// place the board
		List<String> tileImageUrls = mPlayingGame.theme.tileImageUrls;
		Collections.shuffle(tileImageUrls);
		boardArrangment.pairs = new HashMap<Integer, Integer>();
		boardArrangment.tileUrls = new HashMap<Integer, String>();
		int j = 0;
		for (int i = 0; i < ids.size(); i++) {
			if (i + 1 < ids.size()) {
				// {4,10}, {2,39}, ...
				boardArrangment.pairs.put(ids.get(i), ids.get(i + 1));
				// {10,4}, {39,2}, ...
				boardArrangment.pairs.put(ids.get(i + 1), ids.get(i));
				// {4,
				boardArrangment.tileUrls.put(ids.get(i), tileImageUrls.get(j));
				boardArrangment.tileUrls.put(ids.get(i + 1), tileImageUrls.get(j));
				i++;
				j++;
			}
		}

		mPlayingGame.boardArrangment = boardArrangment;
	}
	//En cas d'évènement de retournement de carte
	@Override
	public void onEvent(FlipCardEvent event) {
		// Log.i("my_tag", "Flip: " + event.id);
		int id = event.id;
		if (mFlippedId == -1) {
			mFlippedId = id;
			// Log.i("my_tag", "Flip: mFlippedId: " + event.id);
		} else {
			if (mPlayingGame.boardArrangment.isPair(mFlippedId, id)) {
				// Log.i("my_tag", "Flip: is pair: " + mFlippedId + ", " + id);
				// send event - hide id1, id2
				Shared.eventBus.notify(new HidePairCardsEvent(mFlippedId, id), 1000);
				// play music
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						Music.playCurrent();
					}
				}, 1000);
				mToFlip -= 2;
				if (mToFlip == 0) {
					int passedSeconds = (int) (Clock.getInstance().getPassedTime() / 1000);
					Clock.getInstance().pause();
					int totalTime = mPlayingGame.boardConfiguration.time;
					GameState gameState = new GameState();
					mPlayingGame.gameState = gameState;
					// remained seconds
					gameState.remainedSeconds = totalTime - passedSeconds;
					gameState.passedSeconds = passedSeconds;

					// calc stars
					if (passedSeconds <= totalTime / 2) {
						gameState.achievedStars = 3;
					} else if (passedSeconds <= totalTime - totalTime / 5) {
						gameState.achievedStars = 2;
					} else if (passedSeconds < totalTime) {
						gameState.achievedStars = 1;
					} else {
						gameState.achievedStars = 0;
					}

					// calc score
					gameState.achievedScore = mPlayingGame.boardConfiguration.difficulty * gameState.remainedSeconds * mPlayingGame.theme.id;

					// save to memory
					Memory.save(mPlayingGame.theme.id, mPlayingGame.boardConfiguration.difficulty, gameState.achievedStars);
					Memory.saveTime(mPlayingGame.theme.id, mPlayingGame.boardConfiguration.difficulty ,gameState.passedSeconds);



					Shared.eventBus.notify(new GameWonEvent(gameState), 1200);
				}
			} else {
				// Log.i("my_tag", "Flip: all down");
				// send event - flip all down
				Shared.eventBus.notify(new FlipDownCardsEvent(), 1000);
			}
			mFlippedId = -1;
			// Log.i("my_tag", "Flip: mFlippedId: " + mFlippedId);
		}
	}
	/**
	 * Permet de retourner la partie actuelle
	 * @return un jeu représentant la partie actuelle
	 */
	public Game getActiveGame() {
		return mPlayingGame;
	}

	/**
	 * Permet de retourner le thème sélectionné
	 * @return un thème correspondant au thème sélectionné
	 */
	public Theme getSelectedTheme() {
		return mSelectedTheme;
	}

	/**
	 * Permet d'afficher une image de fond
	 * @param backgroundImage, une vue d'image correspondant à l'image de fond que
	 *                            l'on souhaite afficher
	 */
	public void setBackgroundImageView(ImageView backgroundImage) {
		mBackgroundImage = backgroundImage;
	}
}
