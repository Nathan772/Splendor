import java.util.HashMap;
import java.lang.StringBuilder;


public class AffichageLigneCommande {
	
	/**
	 * Print the 
	 * 
	 * @param game
	 */
	public static void showPlateau(Partie game, int mode) {
		
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
	
	/**
	 * Affiche les jetons disponibles sur le plateau.
	 * 
	 * @param ressources
	 */
	public static void showJeton(HashMap<String, Integer> ressources, String message) {
		
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
	public static void showBoard(Partie game) {
		
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
	 */
	public static void showJoueur(Joueur joueur) {
		
		
		System.out.println("Joueur : " + joueur.pseudo() + "\n\nPoints de prestiges : " + joueur.points_prestiges() + "\n\nRessources : \n");
		System.out.println("Joueur :  " + "\n");
		showJeton(joueur.ressources(), "JETON");
		showJeton(joueur.bonus(), "BONUS");
		
		
	}
	
	/**
	 * Print the 
	 * 
	 * @param game
	 */
	public static void showTuiles(Partie game){
		
		System.out.println("    -- NOBLES --   \n\n");
		
		for(int i = 0 ; i < game.tuiles_board().size(); i++) {
			
			System.out.println(game.tuiles_board().get(i));
		}
	}
	
	
	/**
	 * Print the cards reserved by a player.
	 * 
	 * @param joueur
	 * 		  Player of the
	 */
	public static void showReserved(Joueur joueur) {
		
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



/*
 * 
 * 
 * 
 * 
 * Faire en sorte que si on a des chiffres l'affichage s'adapte.
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * */
