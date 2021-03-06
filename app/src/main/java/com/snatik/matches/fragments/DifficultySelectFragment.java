package com.snatik.matches.fragments;

import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.snatik.matches.R;
import com.snatik.matches.common.Memory;
import com.snatik.matches.common.Shared;
import com.snatik.matches.events.ui.DifficultySelectedEvent;
import com.snatik.matches.themes.Theme;
import com.snatik.matches.ui.DifficultyView;

public class DifficultySelectFragment extends Fragment {//Classe dédiée à la sélection de la difficulté

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(Shared.context).inflate(R.layout.difficulty_select_fragment, container, false);
        Theme theme = Shared.engine.getSelectedTheme();//Obtention du thème

        //Affichage des différents niveaux du thème
        DifficultyView difficulty1 = (DifficultyView) view.findViewById(R.id.select_difficulty_1);
        difficulty1.setDifficulty(1, Memory.getHighStars(theme.id, 1));
        setOnClick(difficulty1, 1);

        DifficultyView difficulty2 = (DifficultyView) view.findViewById(R.id.select_difficulty_2);
        difficulty2.setDifficulty(2, Memory.getHighStars(theme.id, 2));
        setOnClick(difficulty2, 2);

        DifficultyView difficulty3 = (DifficultyView) view.findViewById(R.id.select_difficulty_3);
        difficulty3.setDifficulty(3, Memory.getHighStars(theme.id, 3));
        setOnClick(difficulty3, 3);

        DifficultyView difficulty4 = (DifficultyView) view.findViewById(R.id.select_difficulty_4);
        difficulty4.setDifficulty(4, Memory.getHighStars(theme.id, 4));
        setOnClick(difficulty4, 4);

        DifficultyView difficulty5 = (DifficultyView) view.findViewById(R.id.select_difficulty_5);
        difficulty5.setDifficulty(5, Memory.getHighStars(theme.id, 5));
        setOnClick(difficulty5, 5);

        DifficultyView difficulty6 = (DifficultyView) view.findViewById(R.id.select_difficulty_6);
        difficulty6.setDifficulty(6, Memory.getHighStars(theme.id, 6));
        setOnClick(difficulty6, 6);

        animate(difficulty1, difficulty2, difficulty3, difficulty4, difficulty5, difficulty6);

        Typeface type = Typeface.createFromAsset(Shared.context.getAssets(), "fonts/grobold.ttf");


        TextView text1 = (TextView) view.findViewById(R.id.time_difficulty_1);
        text1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        text1.setTypeface(type);
        text1.setText(getBestTimeForStage(theme.id, 1));

        TextView text2 = (TextView) view.findViewById(R.id.time_difficulty_2);
        text2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        text2.setTypeface(type);
        text2.setText(getBestTimeForStage(theme.id, 2));

        TextView text3 = (TextView) view.findViewById(R.id.time_difficulty_3);
        text3.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        text3.setTypeface(type);
        text3.setText(getBestTimeForStage(theme.id, 3));

        TextView text4 = (TextView) view.findViewById(R.id.time_difficulty_4);
        text4.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        text4.setTypeface(type);
        text4.setText(getBestTimeForStage(theme.id, 4));

        TextView text5 = (TextView) view.findViewById(R.id.time_difficulty_5);
        text5.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        text5.setTypeface(type);
        text5.setText(getBestTimeForStage(theme.id, 5));

        TextView text6 = (TextView) view.findViewById(R.id.time_difficulty_6);
        text6.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        text6.setTypeface(type);
        text6.setText(getBestTimeForStage(theme.id, 6));

        return view;

    }
    /**
     * Permet d'obtenir le meilleur temps pour un niveau
     * @param difficulty, un entier représentant la difficulté du niveau
     * @param theme, un entier représentant le thème du niveau
     * @return un string représenant le meilleur temps pour un niveau
     */
    private String getBestTimeForStage(int theme, int difficulty) {
        int bestTime = Memory.getBestTime(theme, difficulty);
        if (bestTime != -1) {//Si un temps a déja été réalisé
            int minutes = (bestTime % 3600) / 60;
            int seconds = (bestTime) % 60;//Formattage du temps
            String result = String.format("BEST : %02d:%02d", minutes, seconds);
            return result;
        } else {
            String result = "BEST : -";
            return result;
        }
    }
    /**
     * Permet d'animer une vue
     * @param view, une vue contenant différents objets que l'on souhaite animer
     */
    private void animate(View... view) {
        AnimatorSet animatorSet = new AnimatorSet();
        Builder builder = animatorSet.play(new AnimatorSet());
        for (int i = 0; i < view.length; i++) {//Animation de tous les éléments de la vue
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view[i], "scaleX", 0.8f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view[i], "scaleY", 0.8f, 1f);
            builder.with(scaleX).with(scaleY);
        }
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.start();
    }
    /**
     * Permet de définir une difficulté lorsque l'on clique dessus
     * @param view, la vue sur laquelle l'utilisateur va cliquer
     * @param difficulty, un entier représentant la difficulté selectionnée
     */
    private void setOnClick(View view, final int difficulty) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shared.eventBus.notify(new DifficultySelectedEvent(difficulty));
            }
        });
    }
}
