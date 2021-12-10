package com.snatik.matches.themes;

import java.util.List;

public class Theme {//Classe dédiée à la modélisation d'un thème

	public int id;//identifiant du thème
	public String name;//Nom du thème
	public String backgroundImageUrl;//Image d'arrière plan
	public List<String> tileImageUrls;//Liste des urls des images des vignettes
	public List<String> adKeywords;//liste des mots clés du thème
	public String backgroundSoundUrl;//Liste des urls de sons d'arrière plan du thème
	public String clickSoundUrl;//Liste des urls des sons de clics du thème
}
