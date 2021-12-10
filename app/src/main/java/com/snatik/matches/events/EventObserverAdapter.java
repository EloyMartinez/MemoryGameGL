package com.snatik.matches.events;

import com.snatik.matches.events.engine.FlipDownCardsEvent;
import com.snatik.matches.events.engine.GameWonEvent;
import com.snatik.matches.events.engine.HidePairCardsEvent;
import com.snatik.matches.events.ui.BackGameEvent;
import com.snatik.matches.events.ui.FlipCardEvent;
import com.snatik.matches.events.ui.NextGameEvent;
import com.snatik.matches.events.ui.ResetBackgroundEvent;
import com.snatik.matches.events.ui.ThemeSelectedEvent;
import com.snatik.matches.events.ui.DifficultySelectedEvent;
import com.snatik.matches.events.ui.StartEvent;


public class EventObserverAdapter implements EventObserver {//Classe dédié à l'observateur d'évènements

	//En cas d'évènement de retournement de carte
	public void onEvent(FlipCardEvent event) {
		throw new UnsupportedOperationException();
	}
	//En cas d'évènement de changement de dfifficulté
	@Override
	public void onEvent(DifficultySelectedEvent event) {
		throw new UnsupportedOperationException();
	}
	//En cas d'évènement de masquage de paire de cartes
	@Override
	public void onEvent(HidePairCardsEvent event) {
		throw new UnsupportedOperationException();
	}
	//En cas de retournement de l'ensemble des cartes
	@Override
	public void onEvent(FlipDownCardsEvent event) {
		throw new UnsupportedOperationException();
	}
	//En cas d'évènement de lancement d'évènement
	@Override
	public void onEvent(StartEvent event) {
		throw new UnsupportedOperationException();
	}
	//En cas d'évènement de selection de thème
	@Override
	public void onEvent(ThemeSelectedEvent event) {
		throw new UnsupportedOperationException();
	}
	//En cas d'évènement de victoire du joueur
	@Override
	public void onEvent(GameWonEvent event) {
		throw new UnsupportedOperationException();
	}
	//En cas d'évènement de retour au jeu
	@Override
	public void onEvent(BackGameEvent event) {
		throw new UnsupportedOperationException();		
	}
	//En cas d'évènement de nouvelle partie
	@Override
	public void onEvent(NextGameEvent event) {
		throw new UnsupportedOperationException();		
	}
	//En cas d'évènement de réinitialisation du fond d'écran
	@Override
	public void onEvent(ResetBackgroundEvent event) {
		throw new UnsupportedOperationException();		
	}

}
