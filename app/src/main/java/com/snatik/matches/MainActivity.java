package com.snatik.matches;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.snatik.matches.common.Shared;
import com.snatik.matches.engine.Engine;
import com.snatik.matches.engine.ScreenController;
import com.snatik.matches.engine.ScreenController.Screen;
import com.snatik.matches.events.EventBus;
import com.snatik.matches.events.ui.BackGameEvent;
import com.snatik.matches.ui.PopupManager;
import com.snatik.matches.utils.Utils;

public class MainActivity extends FragmentActivity {//Classe principale de l'application, celle à exécuter

	private ImageView mBackgroundImage;//Image de fond de l'application

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Récupération des éléments de l'application (contexte, activité, bus d'évènements)
		Shared.context = getApplicationContext();
		Shared.engine = Engine.getInstance();
		Shared.eventBus = EventBus.getInstance();

		setContentView(R.layout.activity_main);
		mBackgroundImage = (ImageView) findViewById(R.id.background_image);

		Shared.activity = this;
		Shared.engine.start();//Démarrage du moteur de l'application
		Shared.engine.setBackgroundImageView(mBackgroundImage);//Fixation de l'image de fond dans le moteur

		//Fixation du fond d'écran
		setBackgroundImage();

		// Fixation du menu
		ScreenController.getInstance().openScreen(Screen.MENU);
	}
	//En cas de fin de l'application
	@Override
	protected void onDestroy() {
		Shared.engine.stop();
		super.onDestroy();
	}
	//En cas de fond d'écran touché
	@Override
	public void onBackPressed() {
		if (PopupManager.isShown()) {//Si un pop up est affiché, alors on le referme
			PopupManager.closePopup();
			if (ScreenController.getLastScreen() == Screen.GAME) {//Retour au jeu
				Shared.eventBus.notify(new BackGameEvent());
			}
		} else if (ScreenController.getInstance().onBack()) {//Si l'utilisateur choisi de revenir en arrière, alors le faire
			super.onBackPressed();
		}
	}
	/**
	 * Permet de définir l'image de fond
	 */
	private void setBackgroundImage() {
		Bitmap bitmap = Utils.scaleDown(R.drawable.background, Utils.screenWidth(), Utils.screenHeight());
		bitmap = Utils.crop(bitmap, Utils.screenHeight(), Utils.screenWidth());//Redimension de l'image
		bitmap = Utils.downscaleBitmap(bitmap, 2);
		mBackgroundImage.setImageBitmap(bitmap);//Fixation de l'image
	}
}
