
/**
 * Declaration of the type Partie. It represents a game of Splendor
 * 
 * @author dylandejesus nathanbilingi
 */
public class Partie {
	
	/**
	 * It generates the game mode choosen by the user thanks to the n° of the
	 * game mode he wants to play.
	 * 
	 * @param mode 
	 *        N° of the game mode the user want   
	 * 
	 * @return The Object Mode (ModeI or ModeII)
	 */
	private static  Mode createMode(int mode){
		
		if(mode == 1) {
			return new ModeI();
		}
		
		if(mode == 2) {
			return new ModeII();
		}
		
		return null;
	}
	
	/**
	 * It generates the game mode choosen by the user thanks to the n° of the
	 * game mode he wants to play.
	 * 
	 * @param mode 
	 *        N° of the game mode the user want   
	 * 
	 * @return The Object Mode (ModeI or ModeII)
	 */
	private static  Affichage createAffichage(int type){
		
		if(type == 1) {
			return new AffichageLigneCommande();
		}
		
		if(type == 2) {
			return new AffichageGraphique();
		}
		
		return null;
	}
	
	/**
	 * It represents the course of a Splendor game.		   
	 */
	public static void main(String[] args) {
		
		boolean endgame = false;
		int player_turn = 0;
		Joueur player;
		int tour_valide = 1;
		int game_mode = Saisie.saisieMode();
		int affichage_mode = Saisie.saisieAffichage();
				
		Affichage affichage = createAffichage(affichage_mode); /*Rajouter un objet de l'interface Affichage*/
		Mode game = createMode(game_mode);
		
		Saisie.saisieJoueurs(game, game_mode, game.choixNbJoueurs());
		
		game.initialisePartie(game_mode);
		
		while(!endgame) {
			/* on affiche les infos des users et du plateau ssi le tour précédent s'est bien passé*/
			if(tour_valide == 1)
				affichage.showPlateau(game, game_mode);
			
			player = game.joueurs().get(player_turn % game.joueurs().size());
			
			int choice = Saisie.menuSaisie(game_mode);
			
			if(choice == 1){
				tour_valide = game.achatCarte(player, game_mode, affichage);
			}
			
			else if(choice == 2){
				tour_valide = game.priseRessource(player);
			}
			
			else if(choice == 3) {
				tour_valide = affichage.showJoueur(player);
			}
			else if(choice == 4){
				tour_valide = game.reservationCarte(player);
			}
			
			//player.nobleVisiting();
			//endgame = isEndGame(); 	// Mettre une fonction de fin de tour en tant que non default de MODE
			if(tour_valide == 1)
				player_turn ++;
		}
		
		System.out.println("Félicitations : " + game.isWinner() + " !!");
	}
}

  
