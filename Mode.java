package fr.umlv.game.mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.umlv.players.*;
import fr.umlv.objects.*;
import fr.umlv.affichage.*;
import fr.umlv.saisie.*;



/**
 * 
 * 
 * 
 * FAIRE UNE FONCTION endOfTurn() regroupant toutes les actions de fin dr tour, les nobles, le
 * 
 * 
 * 
 * @author dylandejesus
 *
 */
public interface Mode {

	/**
	 * Number of victory points
	 */
	static final int VICTORY_POINTS = 15;
	
	public ArrayList<Joueur> joueurs();
	
	public HashMap<Integer, List<CarteDev>> board();
	
	public HashMap <Integer, List<CarteDev>> pioche();
	
	public ArrayList<Tuile> tuiles_board();
	
	public HashMap<String, Integer> jetons_disponibles();
	
	public int taille_pioche();
	
	
	void initialisePartie();
	
	int achatCarte(Joueur joueur, Affichage affiche);
	
	int reservationCarte(Joueur joueur);
	
	int giveNbPlayersPossible();
	
	void endOfTurn(Affichage affichage, Joueur player);
	
	
	/**
	 * Allows the users to choose the number of player they wants.
	 * @return
	 * the number of players choosen. 
	 */
	public int choixNbJoueurs();
	
	public void nobleVisiting(Joueur joueur, Affichage affichage);
	
	
	
	
	/**
	 * Add a player in the game list of players
	 * 
	 * @param  player
	 * 		   Player to add
	 */
	public default void addPlayer(Joueur player) {
		this.joueurs().add(player);
	}
	
	
	/**
	 * Draw a card from the deck and add it to the board. If you choose to draw a card
	 * remove the card from the board and add the top card of the deck in its place
	 * 
	 * @param  index_supp
	 * 		   Index of the card ti replace
	 */
	public default CarteDev piocheOneCard(int ligne, int index_supp) {
		
		int derniere_carte = this.pioche().get(ligne).size() - 1;	

		CarteDev card_picked = this.pioche().get(ligne).remove(derniere_carte);

		
		this.board().get(ligne).set(index_supp, card_picked);
		
		return card_picked;
	}
	
	
	/**
	 * Draw 4 cards from the deck.
	 */
	default void piocheFourCards(int ligne) {
		
		for(int i  =0; i < 4 ;i++) {
			
			this.piocheOneCard(ligne, i);
		}
	}
	
	
	/**
	 * Find the player with the most prestige points in the player list.
	 * If two players have the same number of prestige points, the one with the
	 * the less development cards. The Method returns the object of type Player.
	 * 
	 * @param  classement
	 * 		   List of players
	 */
	private static Joueur topClassement(ArrayList<Joueur> classement) {
		
		Objects.requireNonNull(classement);
		
		Joueur best_player = classement.get(0);
		int i = 0;
		
		while(classement.get(i).points_prestiges() == best_player.points_prestiges() && i < classement.size() - 1) {
			
			if(classement.get(i).cartes() < best_player.cartes()) {
				best_player = classement.get(i);
			}
			
			i++;
		}
		
		return best_player;
	}
	
	
	/**
	 * Exchange two values from the list of players. We exchange the two values located
	 * index1' and 'index2'.
	 * 
	 * @param  index1
	 *         First index
	 * 
	 * @param  index2
	 * 	       Second index
	 */
	private static void swap(ArrayList<Joueur> list, int index1, int index2) {
		
		Joueur tmp = list.get(index2);

        list.set(index1, list.get(index2));
        list.set(index2, tmp);
	}
	
	/**
	 * Returns true if j1 has less prestige points than j2
	 * 
	 * @param  j1
	 * 		   First player
	 * 
	 * @param  j2
	 *         Second player
	 */
	private static boolean compareJoueur(Joueur j1, Joueur j2){
		
		Objects.requireNonNull(j1);
		Objects.requireNonNull(j2);
		
		if(j1.points_prestiges() < j2.points_prestiges()) {
			return true;
		}
        return false;
    }
	
	
	
	/**
	 * Returns the index of the player with the least prestige points in the index interval [start, end[.
	 * 
	 * @param  list
	 * 			List of players
	 * 
	 * @param  debut
	 *         Integer representing the start index
	 * 
	 * @param  fin
	 *         Integer representing the end index
	 */
	private static int indexOfMin(ArrayList <Joueur> list, int debut, int fin){

        Objects.requireNonNull(list);

        if(debut < 0){
            throw new IllegalArgumentException("Debut index given under 0 (indexOfMin)");
        }
        if(fin < 0){
            throw new IllegalArgumentException("End index given under 0 (indexOfMin)");
        }

        Joueur min = list.get(debut);
        int index = debut;
        int min_index = debut;

        for(var i = debut; i < fin; i++){
            if(compareJoueur(min, list.get(i))){
                min = list.get(i);
                min_index = index;
            }
            index ++;
        }

        return min_index;
    }
	
	/**
	 * Sorts the list of players in ascending order. The comparison between the players is done on
	 * the number of prestige points, with equal prestige points the player with the least
	 * development cards wins.
	 * 
	 * @param  classement
	 * 	 	   List of players
	 * 
	 * @param  size
	 * 		   List size
	 */
	private static boolean sortClassement(ArrayList<Joueur> classement, int size) {
		
		Objects.requireNonNull(classement);
		
		for(var i = 0; i < size ; i++){
            swap(classement, i, indexOfMin(classement, i, size));     /*On va jusqu'à array.length car indexOfMin() va dans l'intervalle [debut, fin[*/
        }
		
		return true;
	}
	
	
	/**
	 * Removes a set amount of available tokens.
	 * 
	 * @param jeton
	 * 		  Type of tokens to remove
	 * 
	 * @param quantite
	 * 		  Number of tokens to remove
	 * 
	 * @return True if it has been successfully removed or false.		
	 *
	 */
	private boolean enleveRessource(Jeton jeton, int quantite) {
		
		int quantite_total = this.jetons_disponibles().get(jeton.couleur()) - quantite;
		
		if(quantite_total < 0) {
			return false;
		}
		
		this.jetons_disponibles().put(jeton.couleur(), quantite_total);
		
		return true;
	}
	
	
	
	
	
	/**
	 * This method enables to know either a user succeed or not to acquire two ressources
	 * 
	 * @param joueur
	 *       the player who his concerned by the purchase
	 * @return 
	 * an int which represents either the transaction occured properly;
	 * 
	 * 1 = transaction has worked
	 * 0 = transaction canceled by the user
	 * -1 = transaction failed
	 */
	private int choixDeuxJetons(Joueur joueur){
		System.out.println("Vous avez choisi de prendre deux jetons de la même couleur, veuillez précisez leur couleur ");
		
		int valid_choice = 0;
		
		while(valid_choice != 1) {
			var jeton = Saisie.saisieJeton();
			
			//Faire une fonction qui fait simultanément les deux actions
			if(this.jetons_disponibles().get(jeton.couleur()) < 4) {
				
				System.out.println("\n/!\\ Il n'y a pas assez de ressources pour effectuer cette action\n");
				
			}else {
				joueur.addRessource(jeton,2);
				this.enleveRessource(jeton, 2);
				valid_choice += 1;
			}
		}
		return valid_choice;
		
	}
	
	
	
	/**
	 * This method handles the whole process necessary to acquire 3 tokens
	 * 
	 * @param joueur
	 *       the player who his concerned by the purchase
	 * @return 
	 * an int which represents either the transaction occured properly;
	 * 
	 * 1 = transaction has worked
	 * 0 = transaction canceled by the user
	 * -1 = transaction failed
	 */
	private int choixTroisJetons(Joueur joueur){
		
		Objects.requireNonNull(joueur);
		
		var already_choosen = new ArrayList<Jeton>();
		boolean suite;
		
		System.out.println("Vous avez choisi de prendre 3 jetons différents, veuillez précisez leur couleur ");
		System.out.println(" (en les séparant par la touche entrée) : \n");
		
		for(int i= 0; i < 3;i++){
			
			suite = false;
			
			while(!suite) {
				
				var jeton = Saisie.saisieJeton();	 //On accède à la classe Saisie pour la saisie des jetons

				
				if(!already_choosen.contains(jeton)) {
					
					
					if(!this.enleveRessource(jeton, 1)) {
							
						System.out.println("\n/ ! \\ Pas assez de ressource pour effectuer cette action\n");
						i --;
						break;
					}
						
					joueur.addRessource(jeton,1);
				    already_choosen.add(jeton);
				    suite = true;
					
				}else {
					System.out.println("\n/ ! \\ Couleur déjà choisie\n");
				}
			}
			  
		}
		return 1;
		
	}
	
	
	
	
	/**
	 * This method handles the whole process necessary to make the user obtain ressources
	 * 
	 * @param joueur
	 *       the player who his concerned by the purchase
	 * @return 
	 * an int which represents either everything occured well or not
	 * 
	 * 1 = everything worked
	 * 0 = the player has canceled his choice
	 * -1 = a problem occured the user has to redo his turn
	 */
	public default int priseRessource(Joueur joueur, Affichage affichage){

		affichage.showJeton(this.jetons_disponibles(), "JETON");
		
		Objects.requireNonNull(joueur);
		
		var choix = Saisie.choixJeton();
		
		/* cas où l'utilisateur veut prendre 2 jetons identiques*/
		if(choix == 1) {
			var res = choixDeuxJetons(joueur);
			if(!joueur.checkNbJetons())
				defausse(joueur, affichage);
			return res;
		}
		
		/* cas où l'utilisateur veut prendre 3 jetons différents*/
		else if(choix == 2) {
			var res = choixTroisJetons(joueur);
			if(!joueur.checkNbJetons())
				defausse(joueur, affichage);
			return res;
		}
		/* cas où l'utilisateur annule son action*/
		return 0;
	}
	
	public default void defausse(Joueur joueur, Affichage affichage){

		while(!joueur.checkNbJetons()) {
					
					System.out.println("\n/!\\ Vous possèdez trop de jetons veuillez en supprimer " + (joueur.NbJetons()-10) + "  pour en avoir 10 maximum\n");
					affichage.showJeton(joueur.ressources(), null);
					
					
					
					String jeton;
					int quantite;
					do{
						System.out.println("\nJeton : \n ");
						
						jeton = Saisie.saisieJeton_name();
						
						System.out.println("Quantite : \n"); 
						
						quantite = Saisie.nb_jeton_defausse();
						
					}while(Saisie.valideDefausse(joueur, jeton, quantite) == false || joueur.NbJetons_loose(quantite) == false);
					
					System.out.println("\n Suppression réussie ! \n");
					joueur.ressources().put(jeton, joueur.enleveRessource(jeton, quantite, this.jetons_disponibles()));
					
					this.jetons_disponibles().put(jeton, this.jetons_disponibles().get(jeton) + quantite);
		}
	}
	
	
	/**
	 * This method check if the user as in the worst cast scenario 10 tokens
	 * If they possess more it force him to loss some
	 * 
	 * @param joueur
	 *       the player who his concerned by the checking
	 * 
	 */
	public default void controleJeton(Joueur joueur, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		
		if(!joueur.checkNbJetons()) {
			
			while(!joueur.checkNbJetons()) {
				
				System.out.println("\n/!\\ Vous possèdez trop de jetons veuillez en supprimer " + (joueur.NbJetons() - 10) + "  pour en avoir 10\n");
				affichage.showJeton(joueur.ressources(), null);
				
				String jeton;
				int quantite;
				do{
					System.out.println("\nJeton : ");
					
					jeton = Saisie.saisieJeton_name();
					
					System.out.println("Quantite :"); 
					
					quantite = Saisie.nb_jeton_defausse();
					
				}while(Saisie.valideDefausse(joueur, jeton, quantite) == false || joueur.NbJetons_loose(quantite));
				
				System.out.println("\n Suppression réussie ! \n");
				joueur.ressources().put(jeton, joueur.enleveRessource(jeton, quantite, this.jetons_disponibles()));
				
				this.jetons_disponibles().put(jeton, this.jetons_disponibles().get(jeton) + quantite);
			}
			
		}
	}
	
	/**
	 * Determine who from the list of players is the winner. The winner
	 * is the one with the most prestige points. If two players
	 * have the same number of points, the player with the least number of cards is
	 * cards.
	 */
	public default Joueur isWinner() {
		
		ArrayList<Joueur> classement = new ArrayList<Joueur>();
		int nb_best_players = 0;
		
		
		for(var joueur : this.joueurs()) {
			
			if(joueur.points_prestiges() >= VICTORY_POINTS) {
				classement.add(joueur);
				nb_best_players += 1;
			}
		}
		
		sortClassement(classement, nb_best_players);
		return topClassement(classement);
	}
	
	
	
	default boolean isEndgame(Joueur player, int player_turn, boolean game_state) {
		
		if(player.points_prestiges() >= VICTORY_POINTS && !game_state && player_turn % this.joueurs().size() == 0) {
			return true;
		}
		
		return false;
		
	}
	
}
