package com.snatik.matches.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.ThumbnailUtils;

import com.snatik.matches.common.Shared;

public class Utils {//Classe dédiée à la gestion de l'appareil

	/**
	 * Permet d'obtenir le nombre de pixels
	 * @param dp, un entier correspondant à la définition de l'image
	 * @return  le nombre de pixels
	 */
	public static int px(int dp) {
		return (int) (Shared.context.getResources().getDisplayMetrics().density * dp);
	}
	/**
	 * Permet d'obtenir la largeur de l'ecran
	 * @return  un entier correspondant à la largeur de l'ecran
	 */
	public static int screenWidth() {
		return Shared.context.getResources().getDisplayMetrics().widthPixels;
	}
	/**
	 * Permet d'obtenir la hauteur de l'écran
	 * @return  un entier correspondant à la hauteur de l'écran
	 */
	public static int screenHeight() {
		return Shared.context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * Permet de redéfinir les dimensions d'une image
	 * @param newHeight, un entier correspondant à la nouvelle hauteur à appliquer
	 * @param newWidth, un entier correspondant à la nouvelle largeur à appliquer
	 * @param source, un bitmap correspondant à l'image à redimensionner
	 * @return  un bitmap correspondant à la nouvelle image redimensionnée
	 */
	public static Bitmap crop(Bitmap source, int newHeight, int newWidth) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();

		// Compute the scaling factors to fit the new height and width,
		// respectively.
		// To cover the final image, the final scaling will be the bigger
		// of these two.
		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.max(xScale, yScale);

		// Now get the size of the source bitmap when scaled
		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		// Let's find out the upper left coordinates if the scaled bitmap
		// should be centered in the new size give by the parameters
		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		// The target rectangle for the new, scaled version of the source bitmap
		// will now
		// be
		RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

		// Finally, we create a new bitmap of the specified size and draw our
		// new,
		// scaled bitmap onto it.
		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}
	/**
	 * Permet de réduire une image
	 * @param reqHeight, un entier correspondant à la hauteur requise
	 * @param reqWidth, un entier correspondant à la largeur requise
	 * @param resource,un entier correspondant à l'image à réduire
	 * @return  un bitmap, correspondant à l'image réduite
	 */
	public static Bitmap scaleDown(int resource, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(Shared.context.getResources(), resource);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(Shared.context.getResources(), resource, options);
	}

	/**
	 * Permet de réduire un bitmap d'image
	 * @param factor, un entier correspondant au facteur de réduction
	 * @param wallpaper, un bitmap correspondant à l'image à réduire
	 * @return  un bitmap représentant l'image réduite
	 */
	public static Bitmap downscaleBitmap(Bitmap wallpaper, int factor) {
		// convert to bitmap and get the center
		int widthPixels = wallpaper.getWidth() / factor;
		int heightPixels = wallpaper.getHeight() / factor;
		return ThumbnailUtils.extractThumbnail(wallpaper, widthPixels, heightPixels);
	}
	/**
	 * Permet de calculer la taille d'échantillonnage
	 * @param reqWidth, un entier représentant la largeur requise
	 * @param options, une option de bitmapFactory représentant les paramètres du nouveau bitmap
	 * @param reqHeight, un entier représentant l'hauteur requise
	 * @return  un entier représenant la taille d'échantillonnage
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
}
