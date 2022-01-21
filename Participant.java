package fr.umlv.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.umlv.objects.*;
import fr.umlv.saisie.*;
import fr.umlv.game.mode.Mode;

public interface Participant {
	
	/**
	 * cartes field getter.
	 * 
	 * @return cartes field value
	 */
	default int cartes() {
		return this.cartes();
	}
	
	/**
	 * pseudo field getter.
	 * 
	 * @return pseudo field value
	 */
	default String pseudo() {
		return this.pseudo();
	}
	
	/**
	 * age field getter.
	 * 
	 * @return age field value
	 */
	default int age() {
		return this.age();
	}
	
	/**
	 * points_prestiges field getter.
	 * 
	 * @return points_prestiges field value
	 */
	default int points_prestiges() {
		return this.points_prestiges();
	}
	
	/**
	 * ressources field getter.
	 * 
	 * @return ressources field value
	 */
	default HashMap<String, Integer> ressources() {
		return this.ressources();
	}
	
	/**
	 * bonus field getter.
	 * 
	 * @return bonus field value
	 */
	default HashMap<String, Integer> bonus() {
		return this.bonus();
	}
	
	/**
	 * reserve field getter.
	 * 
	 * @return reserve field value
	 */
	default ArrayList<CarteDev> reserve(){
		return this.reserve();
	}
	
	/**
	 * Initialize all the coloured tokens bonus (earned by the player during the game). All the tokens start with the value 0. 
	 */
	private void initBonus() {
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc");
		
		for(var elem : couleurs) {
			this.bonus().put(elem, 0);
		}	
	}
	
	/**
	 * Initialize all the couloured tokens (those which represent the ressources of the player). All the tokens start with value 0.
	 */
	private void initRessourcesMap() {
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc", "Jaune");
		
		for(var elem : couleurs) {
			this.ressources().put(elem, 0);
		}	
	}
	

	/**
	 * Remove a player ressources. It removes the ressources thanks to the value given and give it to the bank of tokens. 
	 * 
	 * @param type_ressource
	 *        Name of one of the colors represented in the game
	 *        
	 * @param val
	 *        Number of tokens to remove
	 * 
	 * @return New value of tokens the player have
	 */
	default int enleveRessource(String type_ressource, int val, HashMap<String, Integer> ressources_banque) {
		
		Objects.requireNonNull(type_ressource);
		Objects.requireNonNull(ressources_banque);
		int prix;
		int old_val = this.ressources().get(type_ressource);	//Ressources of the player
		
		//New val taking in count the bonus
		if(val < this.bonus().get(type_ressource)) {
			val  = 0;
		}
		else {
			val = val - this.bonus().get(type_ressource);
		}
		
		if(old_val < val) {
			
			/* à essayer si le code ne marche pas*/
			/*int nouv_val;
			nouv_val = this.enleveRessource(name, val, game.jetons_disponibles());
			this.ressources.put(name, nouv_val);
			
			game.jetons_disponibles().put(name,  game.jetons_disponibles().get(name) + nouv_val);	*/
			
			ressources_banque.put("Jaune", ressources_banque.get("Jaune") + ((-1)*(old_val - val)));	//Jokers rendus à la banque
			this.ressources().put("Jaune", this.ressources().get("Jaune") - ((-1)*(old_val - val)));	//Joker retirés au joueur
			return 0;
		}
		
		return old_val - val;
	}
	
	
	/**
	 * Add the bonus token given ti the player.
	 * 
	 * @param jeton_bonus
	 *        Token considered like the bonus to add
	 */
	private void addBonus(Jeton jeton_bonus) {
		
		Objects.requireNonNull(jeton_bonus);
		
		this.bonus().put(jeton_bonus.couleur(), this.bonus().get(jeton_bonus.couleur()) + 1);
	}
	
	/*
	 * this function enables the developer to choose by a cheat code the number of bonus he wants to begin the part
	 * 
	 * 
	 */
	
	default void cheatBonus() {
		
		System.out.println("Vous êtes entré dans une partie réservée au développeur. Vous pouvez vous octroyer des bonus : \n");
		System.out.println("Couleur : \n");
		
		var jeton = Saisie.saisieJeton();
		
		System.out.println("Nombre : \n");
		
		var quantite = Saisie.choixIntervalle(1,9, null, 0);
		this.bonus().put(jeton.couleur(), quantite);
	}
	
	/**
	 * Adding a defined amount of a player's resource.
	 * 
	 * @param type_ressource
	 *        Name of one of the colors représenting a resource of the game.
	 *        
	 * @param val
	 * 		  Number of tokens to remove
	 * 
	 * @return New number of tokens the player has
	 */
	default int ajouteRessource(String type_ressource, int val) {
		
		Objects.requireNonNull(type_ressource);
		
		return this.ressources().get(type_ressource) + val;
	}
	
	

	
	
	/**
	 * Returns true if a Noble is visiting the player and false otherwise. A Noble is visiting a player if
	 * he possesses the same bonus as the Noble cost. Many nobles can visit a player
	 * 
	 * @param nobles_visiting
	 *        List of nobles who visit the player
	 *        
	 * @param tuiles_board
	 *        Nobles on the board
	 * 
	 * @return Returns true if a Noble is visiting the player and false otherwise
	 */
	default boolean isNobleVisiting(ArrayList<Tuile> nobles_visiting, ArrayList<Tuile> tuiles_board){

		Objects.requireNonNull(tuiles_board);
		
		for(var noble : tuiles_board) {
			
			var is_visiting = true;
			
			for(var couleur_price : noble.cout().entrySet()) {
				var name_price = couleur_price.getKey();
				var val_price = couleur_price.getValue();
				
				if(!(this.bonus().get(name_price) >= val_price)) {
					is_visiting = false;
				}
			}
			
			if(is_visiting) {
				nobles_visiting.add(noble);
			}	
		}
		
		if(nobles_visiting.size() != 0) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Check if the player has enough tokens in his resources to pay the card given.
	 *
	 * @param card
	 *        Card to pay
	 *        
	 * @return Returns ture if the card can be earned or false.
	 */
	
	private boolean checkMoney(CarteDev card, Mode game) {
		
		Objects.requireNonNull(card);
		Objects.requireNonNull(game);
		
		var val_joker = this.ressources().get("Jaune");
		
		for(var elem : card.cout().entrySet()) {
			
			var name = elem.getKey();
			var val = elem.getValue();

			/* on vérifie que le user, grâce à ses ressources et/ou ses bonus puisses acheter la carte*/
			if(this.ressources().get(name) + this.bonus().get(name) < val) {
				
				if(val_joker <= 0) {
					return false;
				}
				if(val_joker + this.ressources().get(name) + this.bonus().get(name) < val){
					return false;
				}
				/* on attribue au joker une nouvelle valeujr en retirant ce qui a pu être payé avec les bonus et les ressources */
				val_joker = val_joker - ( val - (this.ressources().get(name)+ this.bonus().get(name)) );
			}
		}
		
		return true;
	}
	

	/**
	 * Adding of an amount of a token.
	 * 
	 * @param jeton
	 *        Token type to add
	 *        
	 * @param quantite
	 *        	Number of tokens to add
	 */
	default void addRessource(Jeton jeton, int quantite) {
		
		Objects.requireNonNull(jeton);
		
		int quantite_total = this.ressources().get(jeton.couleur()) + quantite;
		
		this.ressources().put(jeton.couleur(), quantite_total);
	}
	
	
	/**
	 * Check if the number of tokens of a player is under 10.
	 * 
	 * @return True if the user has 10 tokens or under and false otherwise.
	 */
	default boolean checkNbJetons() {
		
		int count = 0;
		
		for(var nb_jet : this.ressources().values()) {
			count = count + nb_jet;
		}
		
		if(count > 10) {	// mettre une constate pour 10
			return false;
		}
		
		return true;
	}
	
	/**
	 * return the number of token possessed by a player
	 * 
	 * @return the number of token that the player possess
	 */
	default int NbJetons() {
		
		int count = 0;
		
		for(var nb_jet : this.ressources().values()) {
			count = count + nb_jet;
		}
		
		return count;
	}
	
	/**
	 * Check if the number of token that the player want to loose isn't too many.
	 * @arg the number of token that the player wants to loose.
	 * @return a boolean 
	 * it represents the possibility for the player to loose these tokens. If it's too much, it returns false.
	 * else it returns true.
	 */
	default boolean NbJetons_loose(int quantite) {
		/* cette variable enregistre le nombre de token du joueur*/
		int count = 0;
		
		for(var nb_jet : this.ressources().values()) {
			count = count + nb_jet;
		}
		/* le joueur souhaite retirer trop de token, action annulée*/
		if(count - quantite < 10) {
			System.out.println("Vous avez essayé de retirer trop de jetons... On recommence \n ");
			return false;
		}
		/*le joueur retire un nombre de token acceptable*/
		return true;
	}
	
	/**
	 * Makes a card reservation for the player.
	 * 
	 * @param carte
	 *        card to reserve
	 *        
	 * @param ressources_jeu
	 *        Tokens available in the game (bank)
	 *        
	 * @return True if the reservation is possible (under 3 reserved cards) and false otherwise.
	 */
	default boolean reserveCarte(CarteDev carte, HashMap<String, Integer> ressources_jeu) {
		
		Objects.requireNonNull(carte);
		
		if(this.reserve().size() == 3) {
			System.out.println("Désolé il n'est pas possible de réserver plus de 3 cartes !");
			return false;
		}
		
		this.reserve().add(carte);
		
		
		
		
		return true;
	}
	
	
}
