package com.snatik.matches.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;
import android.widget.TextView;

public class FontLoader {

	public static final int GROBOLD = 0;

	private static SparseArray<Typeface> fonts = new SparseArray<Typeface>();
	private static boolean fontsLoaded = false;

	public static enum Font {
		GROBOLD(FontLoader.GROBOLD, "fonts/grobold.ttf");

		private int val;
		private String path;

		private Font(int val, String path) {
			this.val = val;
			this.path = path;
		}

		public static String getByVal(int val) {
			for (Font font : values()) {
				if (font.val == val) {
					return font.path;
				}
			}
			return null;
		}
	}
	/**
	 * Permet de charger les polices
	 * @param context, représentant le contexte actuel
	 */
	public static void loadFonts(Context context) {
		for (int i = 0; i < Font.values().length; i++) {
			fonts.put(i, Typeface.createFromAsset(context.getAssets(), Font.getByVal(i)));
		}
		fontsLoaded = true;
	}

	/**
	 * Returns a loaded custom font based on it's identifier.
	 *
	 * @param context
	 *            - the current context
	 * @param fontIdentifier
	 *            = the identifier of the requested font
	 *
	 * @return Typeface object of the requested font.
	 */
	public static Typeface getTypeface(Context context, Font font) {
		if (!fontsLoaded) {
			loadFonts(context);
		}
		return fonts.get(font.val);
	}

	/**
	 * Set the given font into the array of text views
	 *
	 * @param context
	 *            - the current context
	 * @param textViews
	 *            - array of text views to set
	 * @param fontIdentifier
	 *            = the identifier of the requested font
	 */
	public static void setTypeface(Context context, TextView[] textViews, Font font) {
		setTypeFaceToTextViews(context, textViews, font, Typeface.NORMAL);
	}

	/**
	 * Set the given bold font into the array of text views
	 *
	 * @param context
	 *            - the current context
	 * @param textViews
	 *            - array of text views to set
	 * @param fontIdentifier
	 *            = the identifier of the requested font
	 */
	public static void setBoldTypeface(Context context, TextView[] textViews, Font font) {
		setTypeFaceToTextViews(context, textViews, font, Typeface.BOLD);
	}
	/**
	 * Permet d'initialiser les polices aux textviews de l'application
	 * @param context, le contexte actuel
	 * @param font, la police à utiliser
	 * @param fontStyle, un entier représentant le style de police à appliquer
	 * @param textViews, un tableau de textviews représentant l'ensemble
	 *                     des textviews auquels on souhaite définir la police
	 */
	private static void setTypeFaceToTextViews(Context context, TextView[] textViews, Font font, int fontStyle) {
		if (!fontsLoaded) {
			loadFonts(context);
		}
		Typeface currentFont = fonts.get(font.val);

		for (int i = 0; i < textViews.length; i++) {
			if (textViews[i] != null)
				textViews[i].setTypeface(currentFont, fontStyle);
		}
	}

}
