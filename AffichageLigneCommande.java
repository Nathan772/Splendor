import java.util.HashMap;


public class AffichageLigneCommande {
	
	public static void showPlateau(Partie game) {
		
		
		showBoard(game);
		
		System.out.println("\n\n========= JETONS ==========\n\n");
		showJeton(game.jetons_disponibles);
		
		
		System.out.println("\n\n========= JOUEURS ==========\n\n");
		
		for(var joueur : game.joueurs) {
			showJoueur(joueur);
		}
		
	}
	
	public static void showJeton(HashMap<String, Integer> ressources) {
		
		for(var entry : ressources.entrySet()) {
			
			var jeton_name = entry.getKey();
			
			if(jeton_name.equals("Vert") || jeton_name.equals("Noir") || jeton_name.equals("Bleu")) {
				jeton_name = jeton_name + " ";
			}
			var jeton_quantite = entry.getValue();
			
			System.out.println(" ―――――――――――――\n|    "+ jeton_name +"    |\n ―――――――――――――\n|      " + jeton_quantite + "      |\n ―――――――――――――\n\n");
		}
		
	}
	
	public static void showBoard(Partie game) {
		int i = 0;
		
		System.out.println("\n\n========= BOARD ==========\n\n");
		
		/*FORMATER LA CHAINE DE FAÇON A CE QU'ELL AIT LA MÊME LONGUEUR QUEL QUE SOIT LE NOMD E LA COULEUR*/
		for(var carte : game.board) {
			i++;
			
			
			var couleur_name = carte.couleur();
			
			
			if(carte.couleur().equals("Vert") || carte.couleur().equals("Noir") || carte.couleur().equals("Bleu")) {
				couleur_name = couleur_name + " ";
			}
			
			System.out.println("      CARTE : "+ i +"\n\n ――――――――――――――――――――\n|                    |\n|  " + carte.points() + "          "+ couleur_name + "  |\n|                    |\n|                    |\n|                    |\n|                    |\n|  " + couleur_name + ": " + carte.coût() + "          |\n|                    |\n ――――――――――――――――――――\n\n");
		}
		
	}
	
	public static void showJoueur(Joueur joueur) {
		
		
		System.out.println("Joueur : " + joueur.pseudo + "\n\nPoints de prestiges : " + joueur.points_prestiges + "\n\nRessources : \n");
		
		showJeton(joueur.ressources);
		
		
	}
}
