package fr.umlv.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.umlv.objects.*;
import fr.umlv.saisie.*;
import fr.umlv.affichage.Affichage;
import fr.umlv.game.mode.Mode;

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
	
	/** This function choose a random action that the IA will do.
	 * 
	 *  the variable "achat valide" enables to be sure that the player can buy card
	 *  if its value is true they can
	 *  in the other case, they can't
	 * @return The action choosen randomly.
	 * */
	private int randomChoice(boolean achat_valide){
		int action = (int)(100*Math.random());
		if(achat_valide == true) {
			/* on choisit de prendre des ressources avec 80% de chances*/
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
	public static HashMap<Integer, Integer> TestachatCarteNonReserveeIA(int mode_jeu, int numero, int niveau) {
		
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
