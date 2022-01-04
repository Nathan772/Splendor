package fr.umlv.game.mode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.umlv.players.*;
import fr.umlv.objects.*;
import fr.umlv.affichage.*;
import fr.umlv.saisie.*;
import fr.umlv.game.Partie;

/**
 * 
 * @author dylandejesus
 */
public class ModeII implements Mode {
	
	
	/**
	 * Players list on the game
	 */
	private ArrayList<Joueur> joueurs;
	
	/**
	 * Pick  of dev cards
	 */
	private HashMap <Integer, List<CarteDev>> pioche;
	
	/**
	 * Pick size
	 */
	private int taille_pioche;
	
	/**
	 * Board of the game
	 */
	private HashMap <Integer, List<CarteDev>> board;
	
	/**
	 * Toeksn available
	 */
	private HashMap<String, Integer> jetons_disponibles;	/*On met une Map pour représenter les piles de jetons*/
	
	/**
	 * Nobles cards on the board
	 */
	private ArrayList<Tuile> tuiles_board;
	
	public int taille_pioche(){
		return this.taille_pioche;
	}
	
	
	/**
	 * Constructor of the type Partie.
	 */
	public ModeII() {
		
		joueurs = new ArrayList<Joueur>();
		
		// Pioche des 4 niveaux de développement
		pioche = new HashMap<>();
		pioche.put(1, new ArrayList<>());
		pioche.put(2, new ArrayList<>());
		pioche.put(3, new ArrayList<>());
		
		taille_pioche = 0;
		
		// Board des 4 niveaux de développement
		board = new HashMap<>();
		board.put(1, Arrays.asList(null, null, null, null));
		board.put(2, Arrays.asList(null, null, null, null));
		board.put(3, Arrays.asList(null, null, null, null));
		
		jetons_disponibles = new HashMap<String, Integer>();
		
		tuiles_board = new ArrayList<>();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public ArrayList<Joueur> joueurs() {
		return this.joueurs;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<Integer, List<CarteDev>> board() {
		return this.board;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Integer> jetons_disponibles() {
		return this.jetons_disponibles;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Tuile> tuiles_board() {
		return this.tuiles_board;
	}
	
	/**
	 * 
	 */
	public HashMap <Integer, List<CarteDev>> pioche(){
		return this.pioche;
	}
	
	
	/**
	 * Initialize a game
	 * 
	 * @param mode
	 *        Game mode
	 */
	public void initialisePartie() {
		
		this.initialiseOrdreJoueurs();
		this.initialiseJetons();
	
		try{
			this.loadDeck(Path.of("src/fr/umlv/data_files/Cartes_Devs.txt"));
			
		}catch(IOException e) {
	    	System.out.println(e.getMessage());
	    	System.exit(1);
	    
	    }
		
		this.piocheFourCards(1);
		this.piocheFourCards(2);
		this.piocheFourCards(3);
		
		this.initialiseTuiles();
	}
	
	/**
	 * This fonction return the 4 maximum number of player on a game in Mode II
	 */
	public int giveNbPlayersPossible() {
		return 4;
	}
	
	/**
	 * Load Noble card in a file.
	 * 
	 * 
	 * @param path
	 *        File path
	 *        
	 * @param tuiles
	 *        Noble Cards
	 *        
	 * @throws IOException If there is a problem in the read of the file
	 */
	private void loadTuiles(Path path, ArrayList<Tuile> tuiles) throws IOException {
		
		Objects.requireNonNull(path, "File path given is null");
		
		Tuile carte;
		
		try(var reader = Files.newBufferedReader(path)) {  //On teste et renvoie une erreur si problème
			 String line;
			 while ((line = reader.readLine()) != null) {
				 
				 carte = Tuile.fromText(line);
				 

				 tuiles.add(carte);
				 
			 }
		} // appelle reader.close()
	}
	
	/**
	 * Initialise Noble cards. There are (nb player + 1) nobles on the board
	 */
	private void  initialiseTuiles() {
	
		var all_tuiles = new ArrayList<Tuile>();
		
		try {
			this.loadTuiles(Path.of("src/fr/umlv/data_files/Tuiles.txt"), all_tuiles);
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		for(var i = 0; i < this.joueurs.size() + 1 ;i++) {
			
			Collections.shuffle(all_tuiles);
			
			this.tuiles_board.add(all_tuiles.get(all_tuiles.size() - 1));
			
			all_tuiles.remove(all_tuiles.size() - 1);
		}
	}
	
	
	/**
	 * Change the order of the players list to make the the younger player start.
	 */
	private void initialiseOrdreJoueurs() {
		
		int i = 0;
		ArrayList<Joueur> extrait = new ArrayList<Joueur>();
		ArrayList<Joueur> base = new ArrayList<Joueur>();
		
		int indice_younger = findYounger(this.joueurs);
		
		
		for(var joueur : this.joueurs) {
			
			if(i < indice_younger) {
				extrait.add(joueur);
			}else {
				base.add(joueur);
			}
			
			i++;
		}
		
		for(var joueur_extrait : extrait) {
			base.add(joueur_extrait);
		}
		
		this.joueurs = base;
	}

	
	/**
	 * 
	 * Return the index of the youngest player in the list. Returns the value -1 if there is a problem.
	 * 
	 * @param joueurs
	 *        List of the players
	 *        
	 * @return Index of the the youngest player
	 */
	private static int findYounger(ArrayList<Joueur> joueurs) {
		
		int i = 0;
		int indice_younger = -1;
		
		Joueur younger = joueurs.get(i);
		
		for(var joueur : joueurs) {
			
			if(younger.age() > joueur.age()) {
				younger = joueur;
				indice_younger = i;
			}
			
			i++;
		}
		
		return indice_younger;
	}
	
	
	/**
	 * Initialise all the available tokens. There are 7 tokens available by color.
	 */
	private void initialiseJetons() {
		
		this.jetons_disponibles.put("Rouge", 7);	/*On mettra une constante dans la classe pour 7 jetons*/
		this.jetons_disponibles.put("Vert", 7);
		this.jetons_disponibles.put("Bleu", 7);
		this.jetons_disponibles.put("Noir", 7);
		this.jetons_disponibles.put("Jaune", 5);
		this.jetons_disponibles.put("Blanc", 7);
	}
	
	/**
	 * Load the development card where the informations are in a file. It makes the picks.
	 * 
	 * @param path
	 *        File path
	 *        
	 * @throws IOException
	 */
	private void loadDeck(Path path) throws IOException {
		
		Objects.requireNonNull(path, "File path given is null");
		
		CarteDev carte;
		
		try(var reader = Files.newBufferedReader(path)) {  //On teste et renvoie une erreur si problème
			 String line;
			 while ((line = reader.readLine()) != null) {
				 
				 carte = CarteDev.fromText(line);
				 

				 this.pioche.get(carte.niveau()).add(carte);
				 
				 this.taille_pioche += 1;
				 
			 }
		} // appelle reader.close()
		
		Collections.shuffle(this.pioche.get(1));
		Collections.shuffle(this.pioche.get(2));
		Collections.shuffle(this.pioche.get(3));
	}
	
	
	/**
	 * Remove the noble chosen 
	 * 
	 * @param noble_chosen
	 *        Noble chosen to remove
	 */
	private void efface_noble(Tuile noble_chosen) {
		
		var iterator = this.tuiles_board.iterator();
		
		while(iterator.hasNext()) {
			
			var tuile1 = iterator.next();
			
			if(tuile1.equals(noble_chosen)) {
				
				System.out.println("Vous avez choisi : " + tuile1.name() + " ");
				iterator.remove();
			}
				
		}
	}
	 
	/**
	 *  This method validate or invalidate a player's buying attempt
	 * 
	 * @param joueur
	 *        the player whom we will look at the reserve and the ressources
	 *        
	 * @param carte_numer
	 *        the number of the card that the user wants to buy. It allows to identify it.
	 *        
	 *  @return an int which the value indicates either the operation succeed or failed
	 *  
	 *  -1 = failure
	 *  1 = success
	 */
	private int validationAchatReserve(Joueur joueur, int carte_numero) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(this);
		Objects.requireNonNull(carte_numero);
		
		if(carte_numero <= 0) {
			return -1;
		}
		
		//3 lignes
		if(joueur.acheteCarte(joueur.reserve().get(carte_numero-1), this)) {
			
			var carte_delete = joueur.reserve().get(carte_numero-1);
			joueur.reserve().remove(carte_delete);
			System.out.println("\n Votre achat a été réalisé avec succès ! \n");
			return 1;
			
		}
		
		/* cas où la carte coûte trop cher, l'utilisateur revient au menu précédent de force car il ne peut pas se payer cette carte:  tour_valide = -1;*/
		System.out.println("\nVous n'avez pas assez de ressources pour acheter cette carte !\n");
		return -1;
	}
	
	/**
	 *  This method validate or invalidate a player's buying attempt
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
	private int validationAchatNonReserve(Joueur joueur, HashMap<Integer, Integer> carte ) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(this);
		Objects.requireNonNull(carte);
		
		var niveau = carte.entrySet().stream().findFirst().get().getKey();
		var choosen_card = carte.entrySet().stream().findFirst().get().getValue();
		HashMap<Integer,Partie> res = new HashMap<Integer,Partie>();
		
		if(choosen_card <= 3 && choosen_card >= 0 && niveau >= 1 && niveau <= 3) {
		
			if(joueur.acheteCarte(this.board().get(niveau).get(choosen_card), this)) {
				System.out.println("\nVotre carte a été achetée avec succès !\n");
				this.piocheOneCard(niveau, choosen_card);	
				return 1;
			}
			/* cas où la carte coûte trop cher, l'utilisateur revient au menu précédent de force car il ne peut pas se payer cette carte*/
			else {
				System.out.println("\nVous n'avez pas assez de ressources pour acheter cette carte !\n");
				return -1;
			}
		}
		/* cas où la carte n'existe pas, l'utilisateur revient au menu précédent de force car le numéro de carte n'existe pas*/
		System.out.println("\n Ce numéro de carte n'existe pas !\n");
		return 0;
	}
	
	/**
	 * This method handles the whole process necessary for a transaction
	 * 
	 * @param joueur
	 *       the player who his concerned by the purchase
	 * @param mode_jeu
	 *       the game mode which had been chosen.
	 * @return 
	 * an int which represents either the transaction occured properly;
	 * 
	 * 1 = transaction has worked
	 * 0 = transaction canceled by the user
	 * -1 = transaction failed
	 */
	public int achatCarte(Joueur joueur, Affichage affichage) {
		
		affichage.showBoard(this);
		
		Objects.requireNonNull(this);
		Objects.requireNonNull(joueur);
		
		var choix = Saisie.choixAchatCarte(this, affichage);
		
		/*Carte du Board*/
		if(choix == 1) {
			
			var carte = Saisie.achatCarteNonReservee(2);
			return validationAchatNonReserve(joueur, carte);
		}
		
		/*Carte Reserve*/
		var carte = Saisie.achatCarteReservee(joueur, affichage);
		return validationAchatReserve(joueur, carte);
	}

	/**
	 * This method handles the whole process necessary to enable a card reservation for the user
	 * 
	 * @param joueur
	 *       the player who his concerned by the rservation
	 * @return 
	 * an int which represents either everything occured well or not
	 * 
	 * 1 = everything worked
	 * -1 = a problem occured the user has to redo his turn
	 */
	public int reservationCarte(Joueur joueur) {
		
		Objects.requireNonNull(joueur);
		
		var choix = Saisie.choixReservation();
		
		if(choix ==  1) {
			var carte = Saisie.reservationCartePlateau();
			return validationReservationCartePlateau(joueur,carte);
		}
		
		var carte = Saisie.reservationCartePioche();
		
		return validationReservationCartePioche(joueur,carte);
	}
	
	/**
	 * This method validate or nullify the action of a card reservation made by a user
	 * 
	 * @param joueur
	 *       the player who his concerned by the reservation
	 *  @param niveau_carte
	 *       the card that interests the user level 
	 * @return 
	 * an int which represents either everything occured well or not
	 * 
	 * 1 = everything worked
	 * -1 = a problem occured the user has to redo his turn
	 */
	private int validationReservationCartePioche(Joueur joueur, int niveau_carte) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(niveau_carte);
		
		if(Saisie.carte_reserve_valide_1arg(this,niveau_carte) != 0){
			joueur.reserveCarte(this.pioche.get(niveau_carte).get(this.pioche.size() - 1), this.jetons_disponibles);
			return 1;
		}
		
		System.out.println("Erreur les cartes de ce niveau ne sont plus disponibles! Vous recommencez votre tour ");
		return -1;		

	}
	
	/**
	 * This method handles the whole process necessary to make the user obtain ressources
	 * 
	 * @param joueur
	 *       the player who his concerned by the purchase
	 *       carte
	 *       the card that the players want to reserve
	 * @return 
	 * an int which represents either everything occured well or not
	 * 
	 * 1 = everything worked
	 * -1 = a problem occured the user has to redo his turn
	 */
	private int validationReservationCartePlateau(Joueur joueur, HashMap<Integer, Integer> carte) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(carte);
		
		var niveau_carte = carte.entrySet().stream().findFirst().get().getKey();
		var num_carte =  carte.entrySet().stream().findFirst().get().getValue();
		
		if(Saisie.carte_reserve_valide_2arg(this,niveau_carte,num_carte) != 0){
			
			joueur.reserveCarte(this.board.get(niveau_carte).get(num_carte), this.jetons_disponibles);
			this.piocheOneCard(niveau_carte, num_carte);
			return 1;
		}
		
		System.out.println("Erreur ce numéro de carte n'existe pas, vous recommencez votre tour !");
		return -1;		
	}
	
	/**
	 * Allows the users to choose the number of player they wants.
	 * @return
	 * the number of players choosen. 
	 */
	@Override
	public int choixNbJoueurs() {
		return Saisie.choixNbJoueurs(this.giveNbPlayersPossible());	
	}
	
	/**
	 * this function handles a noble entrance from the possibility for the player to choose a noble until prestige add.
	 *    
	 */
	//19 lignes
	@Override
	public void nobleVisiting(Joueur joueur, Affichage affichage) {
		var nobles_visiting = new ArrayList<Tuile>();
		if(joueur.isNobleVisiting(nobles_visiting, this.tuiles_board)) {
			
			Tuile noble_chosen;
			
			
			if(nobles_visiting.size() == 1) {
				
				System.out.println("\n.....Un noble vient à votre visite\nIl s'agit de.... " + nobles_visiting.get(0).name() + " !\n");
				noble_chosen = nobles_visiting.get(0);
				
			}else {
				noble_chosen = Saisie.choixNoble(this, affichage,joueur,nobles_visiting);
			}
			joueur.addPrestige(noble_chosen.points_prestiges());
			this.efface_noble(noble_chosen);
			System.out.println("Vous avez maitnenant : " + joueur.points_prestiges());
			System.out.println(" points de prestige ! "); 
			Saisie.passer();
			
			if(this.jetons_disponibles().get("Jaune") > 0) {
				joueur.addRessource(new Jeton("Jaune"), 1);
				this.jetons_disponibles().put("Jaune", jetons_disponibles().get("Jaune")-1);
			}
		}
	
	}
	
	
	
	public void endOfTurn(Affichage affichage, Joueur player) {
		
		this.nobleVisiting(player, affichage);
		
		affichage.showJoueur(player);
		Saisie.saisieFinTour();
	}
	
}
