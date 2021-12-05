import java.util.HashMap;
import java.lang.StringBuilder;


public class AffichageLigneCommande {
	
	/**
	 * Affiche le plateau de jeu.
	 * 
	 * @param game
	 */
	public static void showPlateau(Partie game, int mode) {
		
		if(mode != 1) {
			showTuiles(game);
		}
		
		
		showBoard(game);
		
		showJeton(game.jetons_disponibles, true);
		
		
		System.out.println("\n\n========================================== JOUEURS ==========================================\n\n");
		
		for(var joueur : game.joueurs) {
			showJoueur(joueur);
			System.out.println("\n");
		}
		
	}
	
	/**
	 * Affiche les jetons disponibles sur le plateau.
	 * 
	 * @param ressources
	 */
	public static void showJeton(HashMap<String, Integer> ressources, boolean message) {
		
		StringBuilder chaine_noms = new StringBuilder();
		StringBuilder chaine_quantite = new StringBuilder();
		String separator = "|    ";
		String separator_quantite = "|      ";
		
		
		
		if(message) {
			System.out.println("\n\n========================================== JETONS ==========================================\n\n");
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
			 
			 
			//System.out.println(" ―――――――――――――\n|    "+ jeton_name +"    |\n ―――――――――――――\n|      " + jeton_quantite + "      |\n ―――――――――――――\n\n");
		}
		
		chaine_noms.append("    |");
		chaine_quantite.append("      |");
		
		
		System.out.println(" ――――――――――――― ――――――――――――― ――――――――――――― ――――――――――――― ――――――――――――― ―――――――――――――");
		System.out.println(chaine_noms);
		System.out.println(" ――――――――――――― ――――――――――――― ――――――――――――― ――――――――――――― ――――――――――――― ―――――――――――――");
		System.out.println(chaine_quantite);
		System.out.println(" ――――――――――――― ――――――――――――― ――――――――――――― ――――――――――――― ――――――――――――― ―――――――――――――");
		
	}
	
	/**
	 * Affiche les cartes constituant le board.
	 * 
	 * @param game
	 */
	public static void showBoard(Partie game) {
		
	
		
		
		for(int i = 1 ; i < game.board.size() + 1 ; i++) {
			
			if(game.board.get(i).get(0) != null) {
				
				System.out.println("       NIVEAU " + i);
				
				for(var carte : game.board.get(i)) {
					if(carte != null)
						System.out.println(carte);
				}
			}
			
		}
		
		
	
		
		
//		StringBuilder first_line = new StringBuilder();
//		StringBuilder second_line = new StringBuilder();
//		StringBuilder third_line = new StringBuilder();
//		StringBuilder fourth_line = new StringBuilder();
//		StringBuilder fifth_line = new StringBuilder();
//		
//		String separator = "|  ";
//		String second_separator = "|  ";
//		
//		
//		System.out.println("\n\n========================================== BOARD ==========================================\n\n");
//		
//		/*FORMATER LA CHAINE DE FAÇON A CE QU'ELL AIT LA MÊME LONGUEUR QUEL QUE SOIT LE NOM DE LA COULEUR*/
//		for(var carte : game.board.get(1)) {
//			
//			
//			var card_name = carte.couleur();
//			
//			
//			if(carte.couleur().equals("Vert") || carte.couleur().equals("Noir") || carte.couleur().equals("Bleu")) {
//				card_name = card_name + " ";
//			}
//			
//			
//			first_line.append(separator).append(carte.points()).append("          ").append(card_name);
//			
//			int r = 1;
//			
//			for(var couleur : carte.coût().entrySet()) {
//				
//				var couleur_name = couleur.getKey();
//				var couleur_value = couleur.getValue();
//				
//				switch(r) {
//					case 1 :{
//						second_line.append(second_separator).append(couleur_name).append(": ").append(couleur_value);
//						break;
//					}
//					case 2 :{
//						third_line.append(second_separator).append(couleur_name).append(": ").append(couleur_value);
//						break;
//					}
//					case 3 :{
//						fourth_line.append(second_separator).append(couleur_name).append(": ").append(couleur_value);
//						break;
//					}
//					case 4 :{
//						fifth_line.append(second_separator).append(couleur_name).append(": ").append(couleur_value);
//						break;
//					}
//				};
//				
//				r ++;
//			}
//			
//
//			
//			
//			
//			separator = "  | |  ";
//			second_separator = "          | |  ";
//			
//			//System.out.println("      CARTE : "+ i +"\n\n ――――――――――――――――――――\n|                    |\n|  " + carte.points() + "          "+ couleur_name + "  |\n|                    |\n|                    |\n|                    |\n|                    |\n|  " + couleur_name + ": " + carte.coût() + "          |\n|                    |\n ――――――――――――――――――――\n\n");
//		}
//		
//		first_line.append("  |");
//		second_line.append("          |");
//		third_line.append("          |");
//		fourth_line.append("          |");
//		fifth_line.append("          |");
//		
//		
//		System.out.println("        Carte 1                Carte 2                Carte 3                Carte 4      \n");
//		System.out.println(" ――――――――――――――――――――   ――――――――――――――――――――   ――――――――――――――――――――   ――――――――――――――――――――");
//		
//		System.out.println("|                    | |                    | |                    | |                    |");
//		
//		System.out.println(first_line);
//		
//		System.out.println("|                    | |                    | |                    | |                    |");
//		System.out.println("|                    | |                    | |                    | |                    |");
//		System.out.println("|                    | |                    | |                    | |                    |");
//		System.out.println("|                    | |                    | |                    | |                    |");
//		
//		System.out.println(second_line);
//		System.out.println(third_line);
//		System.out.println(fourth_line);
//		System.out.println(fifth_line);
//		
//		System.out.println(" ――――――――――――――――――――   ――――――――――――――――――――   ――――――――――――――――――――   ――――――――――――――――――――");
		
	}
	
	/**
	 * Affiche les information d'un joueur
	 * 
	 * @param joueur
	 */
	public static void showJoueur(Joueur joueur) {
		
		
		System.out.println("Joueur : " + joueur.pseudo + "\n\nPoints de prestiges : " + joueur.points_prestiges + "\n\nRessources : \n");
		
		showJeton(joueur.ressources, false);
		
		
	}
	
	
	public static void showTuiles(Partie game){
		
		for(int i = 0 ; i < game.tuiles_board.size(); i++) {
			
			System.out.println(game.tuiles_board.get(i));
				
		
			
		}
	}
	
	
	
	public static void showReserved(Joueur joueur) {
		
		System.out.println("Cartes que vous avez réservé \n\n");
		
		for(var elem : joueur.reserve) {
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
