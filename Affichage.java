package fr.umlv.affichage;

import java.util.HashMap;

import fr.umlv.game.mode.*;
import fr.umlv.players.*;

/**
 * Interface représentant la manière d'affichage. On y trouve
 * les affciahges en ligne de commande ainsi que l'affichage graphique 
 * 
 * @author dylandejesus
 */
public interface Affichage {

	public void showPlateau(Mode game, int mode);
	public void showTuiles(Mode game);
	public void showJeton(HashMap<String, Integer> ressources, String message);
	public void showBoard(Mode game);
	public void showReserved(Participant joueur);
	public int showJoueur(Participant joueur);
	
	public void launchAffichage();
	
}
