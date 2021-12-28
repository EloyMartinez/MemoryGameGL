package com.snatik.matches.engine;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.snatik.matches.R;
import com.snatik.matches.common.Shared;
import com.snatik.matches.events.ui.ResetBackgroundEvent;
import com.snatik.matches.fragments.DifficultySelectFragment;
import com.snatik.matches.fragments.GameFragment;
import com.snatik.matches.fragments.MenuFragment;
import com.snatik.matches.fragments.ThemeSelectFragment;

public class ScreenController {//Classe destinée à gérer l'écran du jeu

	private static ScreenController mInstance = null;//Instance de l'écran, assure qu'il y a un écran unique
	private static List<Screen> openedScreens = new ArrayList<Screen>();//Liste des écrans ouverts
	private FragmentManager mFragmentManager;//Permet de gérer les activités

	private ScreenController() {
	}

	//Permet de retourner l'instance de l'écran
	public static ScreenController getInstance() {
		if (mInstance == null) {//S'il n'y a pas d'instance, alors la créer pour la retourner
			mInstance = new ScreenController();
		}
		return mInstance;
	}

	public static enum Screen {//Listage des différents types d'écrans
		MENU,
		GAME,
		DIFFICULTY,
		THEME_SELECT
	}
	/**
	 * Permet d'obtenir l'écran précédent de l'application
	 * @return un écran correspondant au précédent
	 */
	public static Screen getLastScreen() {
		return openedScreens.get(openedScreens.size() - 1);
	}

	/**
	 * Permet d'afficher un écran déterminé sur l'application
	 * @param screen, un écran correspondant à l'écran que l'on souhaite afficher sur l'application
	 */
	public void openScreen(Screen screen) {
		mFragmentManager = Shared.activity.getSupportFragmentManager();//Permet de recupérer le manager d'activités

		//Suppression des écrans précédents, permet de mieux gérer la consommation de l'application
		if (screen == Screen.GAME && openedScreens.get(openedScreens.size() - 1) == Screen.GAME) {
			openedScreens.remove(openedScreens.size() - 1);
		} else if (screen == Screen.DIFFICULTY && openedScreens.get(openedScreens.size() - 1) == Screen.GAME) {
			openedScreens.remove(openedScreens.size() - 1);
			openedScreens.remove(openedScreens.size() - 1);
		}
		Fragment fragment = getFragment(screen);
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();//Mise à jour de l'activité
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.commit();
		openedScreens.add(screen);//Ouverture du nouvel écran
	}
	/**
	 * Permet de revenir à l'écran précédent
	 * @return un booleen, expliquant si le changement de page a pu ou non être opéré
	 */
	public boolean onBack() {
		if (openedScreens.size() > 0) {//Vérification qu'il y ai bien une page ouverte
			Screen screenToRemove = openedScreens.get(openedScreens.size() - 1);
			openedScreens.remove(openedScreens.size() - 1);//Suppression de la page actuelle
			if (openedScreens.size() == 0) {
				return true;
			}
			Screen screen = openedScreens.get(openedScreens.size() - 1);//Retour + ouverture de la page précédente
			openedScreens.remove(openedScreens.size() - 1);
			openScreen(screen);
			if ((screen == Screen.THEME_SELECT || screen == Screen.MENU) &&
					(screenToRemove == Screen.DIFFICULTY || screenToRemove == Screen.GAME)) {
				Shared.eventBus.notify(new ResetBackgroundEvent());//Remise à jour de l'écran de fond
			}
			return false;
		}
		return true;
	}

	/**
	 * Permet de retourner un fragment en fonction du type d'écran donné
	 * @param screen, un écran correspondant à l'écran que l'on veut analyser
	 * @return un fragment correspondant au fragment associé à un type d'écran
	 */
	private Fragment getFragment(Screen screen) {
		//Vérification du type d'activité
		switch (screen) {
			case MENU:
				return new MenuFragment();
			case DIFFICULTY:
				return new DifficultySelectFragment();
			case GAME:
				return new GameFragment();
			case THEME_SELECT:
				return new ThemeSelectFragment();
			default:
				break;
		}
		return null;
	}
}
