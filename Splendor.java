package fr.umlv.game;

import fr.umlv.affichage.Affichage;
import fr.umlv.saisie.Saisie;
import fr.umlv.affichage.*;
import fr.umlv.saisie.*;

public class Splendor {

	
	public static void main(String[] args) {
		
		int affichage_mode = Saisie.saisieAffichage();
		
		Affichage affichage = Partie.createAffichage(affichage_mode);
		
		affichage.launchAffichage();
		
	}
}
