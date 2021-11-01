import java.util.HashMap;
import java.lang.StringBuilder;


public class AffichageLigneCommande {
	
	public static void showPlateau(Partie game) {
		
		
		showBoard(game);
		
		showJeton(game.jetons_disponibles, true);
		
		
		System.out.println("\n\n========================================== JOUEURS ==========================================\n\n");
		
		for(var joueur : game.joueurs) {
			showJoueur(joueur);
			System.out.println("\n");
		}
		
	}
	
	/**
	 * NE SURTOUT PAS OUBLIER DE FAIRE UNE CHAINE SPÉCIALE POUR LES LIGNES DU DESSUS ET DU BAS + BIEN FORMATTÉ SI JAMAIS 
	 * ON A UNE VALEUR À DEUX CHIFFRES.
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
	
	public static void showBoard(Partie game) {
		
		
		StringBuilder first_line = new StringBuilder();
		StringBuilder second_line = new StringBuilder();
		
		String separator = "|  ";
		String second_separator = "|  ";
		
		int i = 0;
		
		
		System.out.println("\n\n========================================== BOARD ==========================================\n\n");
		
		/*FORMATER LA CHAINE DE FAÇON A CE QU'ELL AIT LA MÊME LONGUEUR QUEL QUE SOIT LE NOMD E LA COULEUR*/
		for(var carte : game.board) {
			
			i++;
			
			
			var couleur_name = carte.couleur();
			
			
			if(carte.couleur().equals("Vert") || carte.couleur().equals("Noir") || carte.couleur().equals("Bleu")) {
				couleur_name = couleur_name + " ";
			}
			
			
			first_line.append(separator).append(carte.points()).append("          ").append(couleur_name);
			second_line.append(second_separator).append(couleur_name).append(": ").append(carte.coût());
			
			
			separator = "  | |  ";
			second_separator = "          | |  ";
			
			//System.out.println("      CARTE : "+ i +"\n\n ――――――――――――――――――――\n|                    |\n|  " + carte.points() + "          "+ couleur_name + "  |\n|                    |\n|                    |\n|                    |\n|                    |\n|  " + couleur_name + ": " + carte.coût() + "          |\n|                    |\n ――――――――――――――――――――\n\n");
		}
		
		first_line.append("  |");
		second_line.append("          |");
		
		
		System.out.println("        Carte 1                Carte 2                Carte 3                Carte 4      \n");
		System.out.println(" ――――――――――――――――――――   ――――――――――――――――――――   ――――――――――――――――――――   ――――――――――――――――――――");
		
		System.out.println("|                    | |                    | |                    | |                    |");
		
		System.out.println(first_line);
		
		System.out.println("|                    | |                    | |                    | |                    |");
		System.out.println("|                    | |                    | |                    | |                    |");
		System.out.println("|                    | |                    | |                    | |                    |");
		System.out.println("|                    | |                    | |                    | |                    |");
		
		System.out.println(second_line);
		
		System.out.println(" ――――――――――――――――――――   ――――――――――――――――――――   ――――――――――――――――――――   ――――――――――――――――――――");
		
	}
	
	public static void showJoueur(Joueur joueur) {
		
		
		System.out.println("Joueur : " + joueur.pseudo + "\n\nPoints de prestiges : " + joueur.points_prestiges + "\n\nRessources : \n");
		
		showJeton(joueur.ressources, false);
		
		
	}
}
