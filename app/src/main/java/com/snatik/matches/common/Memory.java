package com.snatik.matches.common;

import android.content.Context;
import android.content.SharedPreferences;

public class Memory {//Classe destinée à gérer la mémoire des records du joueur

    private static final String SHARED_PREFERENCES_NAME = "com.snatik.matches";
    private static String highStartKey = "theme_%d_difficulty_%d";// Clé représentant le meilleur score d'un jueur
    private static String bestTimeKey = "themetime_%d_difficultytime_%d";//Clé représentant le meilleur temps mis par le joueur pour accomplir un niveau

    /**
     * Permet de sauvegarder le nombre d'étoiles obtenues en cas de nouveau record
     * @param theme, un entier correspondant au thème choisi lors de la dernière partie
     * @param difficulty, un entier correspondant à la difficulté choisie lors de la dernière partie
     * @param stars, un entier correspondant au nombre d'étoiles obtenues lors de la dernière partie
     */
    public static void save(int theme, int difficulty, int stars) {
        //Récupération du meilleur score du joueur pour un certain niveau
        int highStars = getHighStars(theme, difficulty);
        //Mise à jour du meilleur nombre d'étoiles à ce niveau
        if (stars > highStars) {
            SharedPreferences sharedPreferences = Shared.context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                    Context.MODE_PRIVATE);//Accès à la mémoire
            SharedPreferences.Editor edit = sharedPreferences.edit();
            String key = String.format(highStartKey, theme, difficulty);
            edit.putInt(key, stars).commit();//Insertion de la paire clé/valeur (niveau/nombre d'étoiles) dans la mémoire
        }
    }

    /**
     * Permet de sauvegarder le temps obtenu en cas de nouveau record
     * @param theme, un entier correspondant au thème choisi lors de la dernière partie
     * @param difficulty, un entier correspondant à la difficulté choisie lors de la dernière partie
     * @param passedSecs, un entier correspondant au temps réalisé lors de la dernière partie
     */
    public static void saveTime(int theme, int difficulty, int passedSecs) {
        //Récupération du meilleur temps du joueur pour un certain niveau
        int bestTime = getBestTime(theme, difficulty);
        //Mise à jour du meilleur temps si aucun temps n'a été réalisé OU qu'il y a une amélioration du meilleurs temps
        if (passedSecs < bestTime || bestTime == -1) {
            SharedPreferences sharedPreferences = Shared.context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                    Context.MODE_PRIVATE);//Accès à la mémoire
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String timeKey = String.format(bestTimeKey, theme, difficulty);
            editor.putInt(timeKey, passedSecs);
            editor.commit();//Insertion de la paire clé/valeur (niveau, temps réalisé) dans la mémoire
        }
    }

    /**
     * Permet d'obtenir le nombre d'étoile maximum pour un niveau déterminé
     * @param theme, un entier correspondant au thème choisi lors de la dernière partie
     * @param difficulty, un entier correspondant à la difficulté choisie lors de la dernière partie
     * @return un entier correspondant au nombre d'étoiles maximum obtenu dans ce niveau
     */
    public static int getHighStars(int theme, int difficulty) {
        SharedPreferences sharedPreferences = Shared.context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);//Accès à la mémoire
        String key = String.format(highStartKey, theme, difficulty);
        return sharedPreferences.getInt(key, 0);//Récupération du meilleur score pour un certain niveau
    }

    /**
     * Permet d'obtenir le temps minimal mis pour réaliser un niveau déterminé
     * @param theme, un entier correspondant au thème choisi lors de la dernière partie
     * @param difficulty, un entier correspondant à la difficulté choisie lors de la dernière partie
     * @return un entier correspondant au temps minimal obtenu dans ce niveau
     */
    public static int getBestTime(int theme, int difficulty) {
        SharedPreferences sharedPreferences = Shared.context.getSharedPreferences
                (SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);//Accès à la mémoire
        String key = String.format(bestTimeKey, theme, difficulty);
        return sharedPreferences.getInt(key, -1);//Récupération du meilleur temps pour un niveau donné
    }
}
