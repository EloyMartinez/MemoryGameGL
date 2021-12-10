package com.snatik.matches.themes;

import android.graphics.Bitmap;

import com.snatik.matches.common.Shared;
import com.snatik.matches.utils.Utils;

import java.util.ArrayList;

public class Themes {//Classe dédiée à la gestion des thèmes

	public static String URI_DRAWABLE = "drawable://";//Url du package drawable où l'on retrouve les thèmes et images

	/**
	 * Permet de mettre en place le thème de jeu lié aux animaux
	 * @return un thème, correspondant à celui des animaux
	 */
	public static Theme createAnimalsTheme() {
		//Chargement du thème
		Theme theme = new Theme();
		theme.id = 1;
		theme.name = "Animals";
		theme.backgroundImageUrl = URI_DRAWABLE + "back_animals";
		theme.tileImageUrls = new ArrayList<String>();
		// 40 drawables
		for (int i = 1; i <= 28; i++) {
			theme.tileImageUrls.add(URI_DRAWABLE + String.format("animals_%d", i));//liste des vignettes
		}
		return theme;
	}
	/**
	 * Permet de mettre en place le thème de jeu lié aux monstres
	 * @return un thème, correspondant à celui des monstres
	 */
	public static Theme createMonsterTheme() {
		//Chargement du thème
		Theme theme = new Theme();
		theme.id = 2;
		theme.name = "Mosters";
		theme.backgroundImageUrl = URI_DRAWABLE + "back_horror";
		theme.tileImageUrls = new ArrayList<String>();
		// 40 drawables
		for (int i = 1; i <= 40; i++) {
			theme.tileImageUrls.add(URI_DRAWABLE + String.format("mosters_%d", i));//Liste des vignettes
		}
		return theme;
	}
	/**
	 * Permet de mettre en place le thème de jeu lié aux emojis
	 * @return un thème, correspondant à celui des emojis
	 */
	public static Theme createEmojiTheme() {
		//Chargement du thème
		Theme theme = new Theme();
		theme.id = 3;
		theme.name = "Emoji";
		theme.backgroundImageUrl = URI_DRAWABLE + "background";
		theme.tileImageUrls = new ArrayList<String>();
		// 40 drawables
		for (int i = 1; i <= 40; i++) {
			theme.tileImageUrls.add(URI_DRAWABLE + String.format("emoji_%d", i));//Liste des vignettes
		}
		return theme;
	}

	/**
	 * Permet de retourner le bitmap correspondant à l'image de fond d'un theme
	 * @param theme, correspondant au thème que l'on veut analyser
	 * @return un bitmap représentant l'image de fond du thème donné
	 */
	public static Bitmap getBackgroundImage(Theme theme) {
		String drawableResourceName = theme.backgroundImageUrl.substring(Themes.URI_DRAWABLE.length());
		int drawableResourceId = Shared.context.getResources().getIdentifier(drawableResourceName,
				"drawable", Shared.context.getPackageName());
		Bitmap bitmap = Utils.scaleDown(drawableResourceId, Utils.screenWidth(), Utils.screenHeight());//Fixation des réglages de l'image
		return bitmap;
	}
}
