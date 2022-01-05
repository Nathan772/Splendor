package fr.umlv.affichage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.awt.Color;
import java.lang.StringBuilder;

import fr.umlv.game.Partie;
import fr.umlv.game.mode.*;
import fr.umlv.players.*;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;


/**
 * Declaration of the class AffichageLigneCommande. It gathers all the functions thah print objets on
 * the console
 * 
 * @author dylandejesus nathanbilingi
 */
public class AffichageLigneCommande implements Affichage{
	
	
	public void launchAffichage() {
		Partie.startGame(this);
	}
	
	
	/**
	 * Print the game board of a game.
	 * 
	 * @param game
	 *        Game to print its board
	 * 
	 * @param mode
	 *        Game mode
	 */
	public void showPlateau(Mode game, int mode) {
		
		Objects.requireNonNull(game);
		
		if(mode != 1) {
			showTuiles(game);
		}
		
		showBoard(game);
		showJeton(game.jetons_disponibles(), "JETON");
		
		System.out.println("\n\n========================================== JOUEURS ==========================================\n\n");
		
		for(var joueur : game.joueurs()) {
			showJoueur(joueur);
			System.out.println("\n");
		}	
	}
	
	
	
	
	public void affichageMessageInstructionsBox(String message) {
		affichageMessage(message);
		
	}
	
	public void affichageMessageActions(String message) {
		affichageMessage(message);
		
	}
	
	
	
	public void affichageMessage(String message) {
		System.out.println(message);
	}
	
	
	/**
	 * 
	 * Print the available tokens on the console. It i représented as a board.
	 * 
	 * @param ressources
	 *        Resources of tokens
	 */
	public void showJeton(HashMap<String, Integer> ressources, String message) {
		
		StringBuilder chaine_tab = new StringBuilder();
		StringBuilder chaine_noms = new StringBuilder();
		StringBuilder chaine_quantite = new StringBuilder();
		String separator = "|    ";
		String separator_quantite = "|      ";
		
		if(message != null) {
			System.out.println("\n\n========================================== "+ message + " ==========================================\n\n");
		}
		
		for(var entry : ressources.entrySet()) {
			
			var jeton_name = entry.getKey();
			
			if(jeton_name.equals("Vert") || jeton_name.equals("Noir") || jeton_name.equals("Bleu")) {
				jeton_name = jeton_name + " ";
			}
			var jeton_quantite = entry.getValue();
			
		
			 chaine_noms.append(separator).append(jeton_name);
			 chaine_quantite.append(separator_quantite).append(jeton_quantite);
			 
			 separator = "    |    ";
			 separator_quantite = "      |      ";
		}
		
		chaine_noms.append("    |");
		chaine_quantite.append("      |");
		for(var i = 0; i< ressources.size();i++) {
			chaine_tab.append(" ―――――――――――――");
		}
		System.out.println(chaine_tab);
		System.out.println(chaine_noms);
		System.out.println(chaine_tab);
		System.out.println(chaine_quantite);
		System.out.println(chaine_tab);
	}

	
	/**
	 * Print the cards of the board of the game token.
	 * 
	 * @param game
	 *        Game token to print it board
	 */
	public void showBoard(Mode game) {
		
		Objects.requireNonNull(game);
		
		for(int i = 1 ; i < game.board().size() + 1 ; i++) {
			
			var num_card = 1;
			
			if(game.board().get(i).get(0) != null) {
				
				System.out.println("\n\n    -- NIVEAU " + i + " --\n\n");
				
				for(var carte : game.board().get(i)) {
					
					System.out.println("\n      Carte n° " + num_card + "\n");
					
					if(carte != null)
						System.out.println(carte);
					num_card ++;	
				}
			}
		}
	}
	
	/**
	 * Print the player informations.
	 * 
	 * @param joueur
	 * 	      Player given to show its game infomations 
	 */
	public int showJoueur(Participant joueur) {
		
		System.out.println("Joueur : " + joueur.pseudo() + "\n\nPoints de prestiges : " + joueur.points_prestiges() + "\n\nRessources : \n");
		System.out.println("Joueur :  " + "\n");
		showJeton(joueur.ressources(), "JETON");
		showJeton(joueur.bonus(), "BONUS");
		this.showReserved(joueur);
		return 0;
	}
	
	/**
	 * Print the Nobles Cards on the board.
	 * 
	 * @param game
	 *        Game given to show its nobles card on the board
	 */
	public void showTuiles(Mode game){
		
		System.out.println("    -- NOBLES --   \n\n");
		
		for(int i = 0 ; i < game.tuiles_board().size(); i++) {
			
			System.out.println(game.tuiles_board().get(i));
		}
	}
	
	
	/**
	 * Print the cards reserved by a player.
	 * 
	 * @param joueur
	 * 		  Player given.
	 */
	public void showReserved(Participant joueur) {
		
		Objects.requireNonNull(joueur);
		
		if(joueur.reserve().size() == 0) {
			System.out.println("Vous ne possèdez aucune carte réservée \n\n");
			return;
		}
		
		System.out.println("Cartes que vous avez réservé \n\n");
		
		for(var elem : joueur.reserve()) {
			System.out.println(elem + "\n");
		}
	}
}
