package com.snatik.matches.model;

import com.snatik.matches.themes.Theme;

/**
 * This is instance of active playing game
 * 
 * @author sromku
 */
public class Game {//Classe dédiée à la gestion d'une partie

	/**
	 * The board configuration
	 */
	public BoardConfiguration boardConfiguration;

	/**
	 * The board arrangment
	 */
	public BoardArrangment boardArrangment;

	/**
	 * The selected theme
	 */
	public Theme theme;

	public GameState gameState;//état du jeu
}
