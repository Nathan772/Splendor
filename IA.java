package fr.umlv.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.umlv.objects.*;
import fr.umlv.saisie.*;
import fr.umlv.affichage.Affichage;
import fr.umlv.copie.Copie;
import fr.umlv.game.Partie;
import fr.umlv.game.mode.Mode;

/* note pour moi-même pour l'ia faire trois tentatives d'achat de carte de trois niveaux différents, du plus haut au plus bas. Tentative sur les cartes de numéro random.
 * Si ça échoue les 3 fois, faire une autre action qui est sûre à 100%*/

public class IA implements Participant {
	
	
	/**
	 * Number of cards earned by a player
	 */
	private int cartes;
	
	/**
	 * Name of the player during the game
	 */
	private final String pseudo;
	
	/**
	 * Age of the player
	 */
	private final int age;
	
	/**
	 * Number of points value
	 */
	private int points_prestiges;
	
	/**
	 * Player's resources (tokens he has)
	 */
	private HashMap<String, Integer> ressources;
	
	/**
	 * Player's bonus (when he pays developments cards)
	 */
	private HashMap<String, Integer> bonus;
	
	/**
	 * All the reserved cards of a player
	 */
	private ArrayList<CarteDev> reserve;
	/*
	 * The card that the player will buy for the next turn
	 * its value is null if he won't buy anything.
	 * Its value is filled during the test phase.
	 * The "value" value is -1 if the player want to buy a reserved card.
	 * 
	 */
	private HashMap<Integer,Integer> next_achat;
	
	/*
	 * The card that the player will buy for the next turn
	 * its value is null if he won't buy anything.
	 * Its value is filled during the test phase.
	 */
	
	/*
	 * this variable enable to know if the player will buy a reserved card or a card on the board.
	 * Its value is 1 if it's a reserved card and 0 if it is from the board. -1 means no buying attempt.
	 */
	private int achat_type;

	/**
	 * Constructor of the type Joueur.
	 * 
	 * @param pseudo
	 * 		 Player name
	 * 
	 * @param age
	 * 		  Player age
	 * 
	 * @param points_prestiges
	 * 		  Points value
	 */
	public IA(String pseudo, int age, int points_prestiges) {
		
		Objects.requireNonNull(pseudo, "Pseudo hasn't been given");
	
		if(age <= 0) {
			throw new IllegalArgumentException();
		}
		
		if(points_prestiges < 0) {
			throw new IllegalArgumentException("Prestige points can't be under 0");
		}
		
		this.cartes = 0;
		this.pseudo = pseudo;
		this.age = age;
		this.points_prestiges = points_prestiges;
		this.reserve = new ArrayList<CarteDev>();
		this.bonus = new HashMap<>();
		this.ressources = new HashMap<>();
		this.initRessourcesMap();
		this.initBonus();
		this.next_achat = null;
		this.achat_type = 0;
		
		/*RAJOUTER INITIALISATION DE LA GRAINE*/
	}
	
	/**
	 * Constructor of the type Joueur. Is called if we create a player without giving any Points value.
	 *The points value are initialized with value 0.
	 * 
	 * @param pseudo
	 *        Player name
	 *        
	 * @param age
	 *        Age of the player
	 */
	public IA(String pseudo, int age) {
		this(pseudo, age, 0);
	}
	
	/**
	 * cartes field getter.
	 * 
	 * @return cartes field value
	 */
	public int cartes() {
		return this.cartes;
	}
	
	/**
	 * pseudo field getter.
	 * 
	 * @return pseudo field value
	 */
	public String pseudo() {
		return this.pseudo;
	}
	
	/**
	 * age field getter.
	 * 
	 * @return age field value
	 */
	public int age() {
		return this.age;
	}
	
	/**
	 * points_prestiges field getter.
	 * 
	 * @return points_prestiges field value
	 */
	public int points_prestiges() {
		return this.points_prestiges;
	}
	
	/**
	 * ressources field getter.
	 * 
	 * @return ressources field value
	 */
	public HashMap<String, Integer> ressources() {
		return this.ressources;
	}
	
	/**
	 * bonus field getter.
	 * 
	 * @return bonus field value
	 */
	public HashMap<String, Integer> bonus() {
		return this.bonus;
	}
	
	/**
	 * reserve field getter.
	 * 
	 * @return reserve field value
	 */
	public ArrayList<CarteDev> reserve(){
		return this.reserve;
	}
	
	/**
	 * Initialize all the coloured tokens bonus (earned by the player during the game). All the tokens start with the value 0. 
	 */
	private void initBonus() {
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc");
		
		for(var elem : couleurs) {
			this.bonus.put(elem, 0);
		}	
	}
	
	/**
	 * Initialize all the couloured tokens (those which represent the ressources of the player). All the tokens start with value 0.
	 */
	private void initRessourcesMap() {
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc", "Jaune");
		
		for(var elem : couleurs) {
			this.ressources.put(elem, 0);
		}	
	}
	
	/**
	 * String representation of a Player.
	 */
	@Override
	public String toString() {
		return "Joueur [pseudo= "+ this.pseudo + ", age= " + this.age + ", prestige= " + points_prestiges + ", ressources=" + this.ressources +"]"; 
	}
	
	/** This function choose a random action that the AI will do.
	 * 
	 *  the variable "achat valide" enables to be sure that the player can buy card
	 *  if its value is true they can
	 *  in the other case, they can't
	 * @return The action choosen randomly.
	 * */
	private int randomChoice(boolean achat_valide){
		int action = (int)(100*Math.random());
		if(achat_valide == true) {
			/* on choisit de prendre des ressources avec 50% de chances*/
			if(action >= 0 && action <= 49)
				return 2;
			/* on choisit d'acheter une carte avec 30% de chances */
			if(action >= 50 && action <= 89)
				return 1;
			/* on choisit de réserver une carte*/
			return 4;
		}
		/* on choisit de prendre des ressources avec 80% de chances*/
		if(action >= 0 && action <= 80)
			return 2;
		/* on choisit de réserver une carte avec 20% de chances*/
		return 4;
		
		
	}

	
	/**
	 *  This method allow check if the AI can buy a card and returns the card chosne
	 * 
	 * @param mode_jeu 
	 * the game mode choosen by the users 
	 * @param numero 
	 * number of the previous tested card
	 * @param niveau 
	 * level of the previous tested card
	*  @return an Hashmap which contains the card data : its number and its level. key = level, value = number
	*  if it returns null, the player can't buy the caed.
	 */ 
	/*public HashMap<Integer, Integer> TestAchatCarteNonReserveeIA(int mode_jeu, int numero, int niveau, Mode game) {
		
		if(mode_jeu != 1 && mode_jeu != 2) {
			throw new IllegalArgumentException("mode_jeu must be 1 or 2");
		}
		/*
		 * création de la carte à tester
		var carte = new HashMap<Integer,Integer>();
		
		var copie = this;
		copie.checkMoney(carte, game);
		carte.put(numero, niveau);
		return carte;
		
	}*/

	
	
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
			if(this.ressources.get(name) + this.bonus.get(name) < val) {
				
				if(val_joker <= 0) {
					return false;
				}
				if(val_joker + this.ressources.get(name) + this.bonus.get(name) < val){
					return false;
				}
				/* on attribue au joker une nouvelle valeur en retirant ce qui a pu être payé avec les bonus et les ressources */
				val_joker = val_joker - ( val - (this.ressources.get(name)+ this.bonus.get(name)) );
			}
		}
		
		return true;
	}
	
	/**
	 *  This method check if the game would validate or invalidate aN AI buying attempt
	 * 
	 * @param joueur
	 * 		   The player whom we will look at the reserve and the ressources
	 * 
	 * @param carte_numero
	 *        The numbers of the card that the user wants to buy. It allows to identify it.
	 *        
	*  @return An int which the value indicates either the operation succeed or failed
	*  
	*  -1 = failure
	*  1 = success
	*  0 = the player wanted to cancel
	 */
	
/** This create a card with a random number and a random level
 * 
 * @return an HashMap which represents the random card that had been created.
 */

	
private HashMap<Integer, Integer> RandomCardChoosen(){
	var carte = new HashMap<Integer,Integer>();
	/* on choisit un numero de carte entre 0 et 3*/
	var choosen_card =  (int)(4*Math.random());
	/* on choisit un numero de carte entre 1 et 3*/
	var ligne_choosen =  (int)(2*Math.random())+1;
	carte.put(ligne_choosen, choosen_card);
	return carte;
}

/**
 *  This method check if an AI can buy a card. For this purpose it launch several attempts (5 times) with random card level and number.
 * 
 * @param game
 * 		  The game state at this moment
 * 
 *        
*  @return boolean which indicates if the AI can buy the card.
*  True means "yes", false means "no".
*  
 */
private boolean testBuyableCard(Mode game){
	var copieGame = game.deepClone();
	var copieiA = (IA)this.clone();
	/* on tente cinq fois d'acheter une carte avec des informations aléatoires mais possibles*/
	for(var i = 0 ; i < 5;i++) {
		var carte = RandomCardChoosen();
		var choosen_card = carte.entrySet().stream().findFirst().get().getKey();
		var ligne_choosen = carte.entrySet().stream().findFirst().get().getValue();
		/* carte qui va être testée*/
		var realCard = copieGame.board().get(choosen_card).get(choosen_card);
		/* si l'ia peut acheter cette carte, il la renvoie*/
		if(copieiA.checkMoney(realCard,copieGame)) {
			this.next_achat.remove(0);
			/* le user choisit d'acheter une carte qui n'est pas dans la réserve*/
			this.achat_type = 0;
			/* on enregistre la carte qu'il achètera*/
			this.next_achat.put(choosen_card, ligne_choosen);
			return true;
		}
			
	}
	return false;
}


/**
 *  This method check either an AI can buy a specific card which hadn't been reserved.
 *  It only does it into the function not really in the party.
 * 
 * @param game
 * 		  The game state at this moment
 * 
 * @param card
 * 		The card that the AI try to buy
 *        
*  @return a boolean which indicates either the operation succeed or failed
*  
*  true == success
*  false == fail
 */

private boolean testAiCheckMoney(CarteDev card, Mode game) {
		
		Objects.requireNonNull(card);
		Objects.requireNonNull(game);
		/* la deep copie n'est peut-être pas nécessaire, à vérifier*/
		var copieIa= (IA)this.clone();
		var copieGame = game.deepClone(); 
		var copieCard = card;
		var val_joker = copieIa.ressources().get("Jaune");
		
		for(var elem : copieCard.cout().entrySet()) {
			
			var name = elem.getKey();
			var val = elem.getValue();

			/* on vérifie que le user, grâce à ses ressources et/ou ses bonus puisses acheter la carte*/
			if(copieIa.ressources.get(name) + copieIa.bonus.get(name) < val) {
				
				if(val_joker <= 0) {
					return false;
				}
				if(val_joker + copieIa.ressources.get(name) + copieIa.bonus.get(name) < val){
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Addin of some points values.
	 * 
	 * @param val
	 *        Points value to add
	 */
	public void addPrestige(int val) {
		int result = this.points_prestiges + val;
		
		if(result < 0) {
			result = 0;
		}
		
		this.points_prestiges = result;
	}
	
	/**
	 * The player buys a card from the game. The card is bought if the resource
	 * requested is possessed in sufficient quantity by the player.
	 * 
	 * @param carte
	 *        Card to earn
	 *        
	 * @param game
	 *        Game in which buy the card
	 * 
	 * @return True if the card has successfully been earned and false otherwise.
	 */
	public boolean acheteCarte(CarteDev carte, Mode game) {
		
		Objects.requireNonNull(carte);
		Objects.requireNonNull(game);
		
		if(!this.checkMoney(carte, game)) {
			return false;
		}
		
		this.addPrestige(carte.points());
		
		for(var elem : carte.cout().entrySet()) {
			
			var name = elem.getKey();
			var val = elem.getValue();
			/* ressources récupérées par la banque*/
			var recover = val;
			/* cas où le user n'a pas assez de ressources du type recherché sauf quand il utilise des ressources en or*/
			if(this.ressources.get(name) < recover)
				recover = this.ressources.get(name);
			//System.out.println("coût de la ressource : "+val);
			int nouv_val;
			
			nouv_val = this.enleveRessource(name, val, game.jetons_disponibles());
			this.ressources.put(name, nouv_val);
			/*System.out.println("ressource récupérée : "+name);
			System.out.println("quantité récupérée : "+nouv_val);*/
			game.jetons_disponibles().put(name,  game.jetons_disponibles().get(name) + recover);	
		}
		
		this.addBonus(new Jeton(carte.couleur()));
		this.cartes += 1;
		
		return true;
	}
	
	/**
	 * Add the bonus token given ti the player.
	 * 
	 * @param jeton_bonus
	 *        Token considered like the bonus to add
	 */
	private void addBonus(Jeton jeton_bonus) {
		
		Objects.requireNonNull(jeton_bonus);
		
		this.bonus.put(jeton_bonus.couleur(), this.bonus.get(jeton_bonus.couleur()) + 1);
	}
	
	/**
	 *  This method allows the player to choose a card
	 * 
	 * @param mode_jeu 
	 * the game mode choosen by the users 
	 * @param numero 
	 * number of the previous tested card
	 * @param niveau 
	 * level of the previous tested card
	*  @return an Hashmap which contains the card data : its number and its level. key = level, value = number
	 */
	public static HashMap<Integer, Integer> choixAchatCarteNonReserveeIA(int mode_jeu, int numero, int niveau) {
		
		if(mode_jeu != 1 && mode_jeu != 2) {
			throw new IllegalArgumentException("mode_jeu must be 1 or 2");
		}
		/*
		 * création de la carte à tester
		 */
		var carte = new HashMap<Integer,Integer>();
		carte.put(numero, niveau);
		return carte;
		
	}
	
	/**
	 *  This method test if the user can validate or invalidate a iabuying attempt
	 * 
	 * @param ia
	 *        the player whom we will look at the reserve and the ressources
	 *        
	 * @param carte_numero
	 *        the number of the card that the user wants to buy. It allows to identify it.
	 *        
	 *  @return a boolean which the value indicates either the operation succeed or failed
	 *  
	 *  true = success
	 *  false = failure
	 */
	private boolean testIavalidationAchatReserve(IA ia, int carte_numero, Mode game) {
		
		Objects.requireNonNull(ia);
		Objects.requireNonNull(this);
		Objects.requireNonNull(carte_numero);
		var game_copie = game.deepClone();
		if(carte_numero <= 0) {
			return false;
		}
		//3 lignes
		if(ia.acheteCarte(ia.reserve().get(carte_numero-1), game_copie)) {
			return true;
			
		}
		return false;
	}
	public int TestIaAchatCarteReservee(Mode game){
		Objects.requireNonNull(this);
		var game_copie = game.deepClone();
		var ia = (IA)this.clone();
		/* cas où la partie avec les cartes réservées n'est pas vide*/
		if(this.reserve().size() > 0) {
			for(var carte_num = this.reserve().size(); carte_num >= 1 ;carte_num--) {
				/* cas où le numéro de la carte est valide*/
				if(testIavalidationAchatReserve(ia, carte_num, game_copie)) {
					/* on supprime les anciennes cartes à acheter*/
					this.next_achat.remove(0);
					/* le user choisit la carte qu'il va prendre*/
					this.next_achat.put(-1, carte_num);
					/* le user prend une carte réservé*/
					this.achat_type = 1;
					return 1;
				}	
				/* cas où l'utilisateur peut payer */
				//validationAchatReserve(game, nb_joueurs, mode_jeu, points_victoires, tour, tour_valide, joueur, carte_numero);
			}
		}
		/* échec, aucune carte n'est achetable*/
		return -1;

	}

	
	/**This function do a deep copy of an Arraylist.
	 * @param the arraylist that one wants to copy
	 * 
	 * @return the copy which has its own references.
	 */
	/*ArrayList<CarteDev> reserve;
	public static <U>ArrayList<U> copieArray(ArrayList<U> originale) {
		Objects.requireNonNull(hash, "the hashmap argument can't be null");
		
		if(U == joueur)
		HashMap<T,U> copie;
		if(hash.size() == 0)
			return copie;*/
		/* copie les éléments*/
		/*for(HashMap.Entry<T,U> entry : hash.entrySet()){
			var cle = entry.getKey();
			var value = entry.getValue();
			copie.put(cle, value);
		}*/
		/* renvoie la copie qui a ses propres références pour les valeurs*/
		/*return copie;
	}*/
	
	
	
	/*
	 * à supprimer
	 * public IA copieIA() {
		Copie copie1 = new Copie();
		IA copie = new IA(this.pseudo(), this.age(),this.points_prestiges());
		copie.cartes = this.cartes;
		copie.ressources = copie1.copieHashmap(this.ressources);
		copie.bonus = copie1.copieHashmap(this.bonus);
		return copie;
	}*/
	
	@Override
	/**This function do a deep copy of an AI.
	 * 
	 * @return the copy which has its own references.
	 */
	protected Object clone(){
		Objects.requireNonNull(this);
		var copie1 = new Copie();
		IA copie = new IA(this.pseudo(), this.age(),this.points_prestiges());
		copie.cartes = this.cartes;
		copie.ressources = copie1.copieHashmap(this.ressources);
		copie.bonus = copie1.copieHashmap(this.bonus);
		/* à finir la copie de la réserve n'a pas été faite*/
		/*copie.reserve = this.reserve.clone();*/
		Collections.copy(copie.reserve, this.reserve);
		return (IA)copie;	
	}
	public static void main(String[] args) {
		var ia1 = new IA("ll", 12, 0);
		var hash2 = new HashMap<Integer,IA>();
		/* cast nécessaire lorsqu'on utilise clone*/
		IA ia2 = (IA)ia1.clone();
		var hash = new HashMap<Integer,IA>();
		/*hash.put(1,  ia1);*/
		/* fait une copie pronfonde car il utilise les méthodes des champs lorsqu'on appelle clone*/
		hash2 = (HashMap<Integer,IA>)hash.clone();
		hash2.put(1, ia1);
		/*hash.put("ooo", 3);
		CarteDev carte1 = new CarteDev(1,"abc",1,"obj",hash);
		ia2.reserve.add(carte1);*/
		System.out.println("la réserve de hash : "+hash.toString());
		System.out.println("la réserve de hash2 : "+hash2.toString());
		
	}
	/*
	private ArrayList<CarteDev> reserve;*/

	/**

	/*
	public int achatCarte() {
		Objects.requireNonNull(this);
		var carte = Saisie.achatCarteNonReservee(1);
		return validationAchatNonReserve(joueur, carte);
		
	}*/
	/*
	private boolean testAchatIA(Mode game){
		TestachatCarteNonReserveeIA(int mode_jeu, int numero, int niveau);
		
	}*/
	/** This function enable to choose which action the IA will do.
	 * 
	 * @return The action choosen by the user.
	 * */
	/*private int testIA(Mode game) {*/
		/* récupère si oui ou non le joueur peut acheter une carte*/
		/*boolean acheter_autoriser;
		acheter_autoriser = testAchatIA(game);
		return randomChoice(acheter_autoriser);
	}*/
	
	/**
	 * This function enables the user to choose his action
	 * @return 1 means they want to buy card, 2 they want to take tokens, 3 they want to display their information , 4 they want to reserve a card
	 * 
	 * 
	 */
	/*public int priseDecisionMenu(int game_mode, Mode game){*/
		/* Mettre la méthode de saisie */
		/*return testIA(game);
	}*/

}
