package com.snatik.matches.model;

import java.util.Map;

import android.graphics.Bitmap;

import com.snatik.matches.common.Shared;
import com.snatik.matches.themes.Themes;
import com.snatik.matches.utils.Utils;

/**
 * Before game starts, engine build new board
 * 
 * @author sromku
 */
public class BoardArrangment {//Classe dédiée à l'arrangement de l'ordre de la grille

	// like {0-2, 4-3, 1-5}
	public Map<Integer, Integer> pairs;//Liste des paires
	// like {0-mosters_20, 1-mosters_12, 2-mosters_20, ...}
	public Map<Integer, String> tileUrls;//Liste des URLs des images

	/**
	 * Permet de retourner un bitmap pour une tuile choisie
	 * @param id, un entier correspondant à la position de la tuile dans la liste
	 * @param size, un entier correspondant à la taille du bitmap désirée
	 * @return un bitmap correspondant à celui de la tuile
	 */
	public Bitmap getTileBitmap(int id, int size) {
		String string = tileUrls.get(id);//Chargement de l'url
		if (string.contains(Themes.URI_DRAWABLE)) {
			String drawableResourceName = string.substring(Themes.URI_DRAWABLE.length());
			int drawableResourceId = Shared.context.getResources().getIdentifier(drawableResourceName, "drawable", Shared.context.getPackageName());
			Bitmap bitmap = Utils.scaleDown(drawableResourceId, size, size);//Fixation des réglages de l'image
			return Utils.crop(bitmap, size, size);
		}
		return null;
	}

	/**
	 * Permet de retoruner si deux tuiles sont paires
	 * @return un booleen représentant si deux tuiles font partie de la même paire
	 */
	public boolean isPair(int id1, int id2) {
		Integer integer = pairs.get(id1);
		if (integer == null) {
			// TODO Report this bug!!!
			return false;
		}
		return integer.equals(id2);
	}
}
