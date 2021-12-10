package com.snatik.matches.ui;

import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.snatik.matches.R;
import com.snatik.matches.common.Shared;

public class DifficultyView extends LinearLayout {//Classe dédiée à la vue de la difficulté

	private ImageView mTitle;//vue de la difficulté
	
	public DifficultyView(Context context) {
		this(context, null);
	}
	
	public DifficultyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//Fixation de la vue
		LayoutInflater.from(context).inflate(R.layout.difficult_view, this, true);
		setOrientation(LinearLayout.VERTICAL);
		mTitle = (ImageView) findViewById(R.id.title);
	}

	/**
	 * Permet de régler la difficulté d'une partie
	 * @param difficulty, un entier correspondant à la difficulté que l'on souhaite mettre en place
	 * @param stars, un entier correspondant au nombre d'étoiles que l'on souhaite utiliser pour ce niveau
	 */
	public void setDifficulty(int difficulty, int stars) {
		String titleResource = String.format(Locale.US, "button_difficulty_%d_star_%d", difficulty, stars);
		int drawableResourceId = Shared.context.getResources().getIdentifier(titleResource, "drawable", Shared.context.getPackageName());
		mTitle.setImageResource(drawableResourceId);
	}
}
