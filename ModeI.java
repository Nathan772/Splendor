import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ModeI implements Mode {
	/**
	 * Number of victory points
	 */
	private static final int VICTORY_POINTS = 15;
	
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
	
	
	/**
	 * Constructor of the type Partie.
	 */
	public ModeI() {
		
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
	
	
	public HashMap <Integer, List<CarteDev>> pioche(){
		return this.pioche;
	}
	
	public int taille_pioche(){
		return this.taille_pioche;
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
	
		this.initialisePioche();
		this.piocheFourCards(1);
	}
	
	/**
	 * 
	 */
	public int giveNbPlayersPossible() {
		return 2;
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
		this.jetons_disponibles.put("Blanc", 7);
	}
	
	/**
	 * Initialize the deck made up of cards in a random manner. Each card has one of the following colors
	 * following colors: Red, Green, Black, Blue, White, Yellow, White.
	 */
	private void initialisePioche() {
		
		/*Les cartes de la pioche ne peuvent que possèder ces couleurs*/
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc");
		
		/*Pour chaque couleur on crée 8 cartes*/
		for(var elem : couleurs) {
			for(int i = 0; i < 8 ;i++) {
				
				var map = new HashMap<String, Integer>();
				map.put(elem, 1);
				
				this.pioche.get(1).add(new CarteDev(0,elem,3,null,map));
				this.taille_pioche += 1;
			}
		}
		
		/*On mélange la pioche*/
		Collections.shuffle(this.pioche.get(1));
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
		if(choosen_card <= 3) {
		
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
		/* l'utilisateur revient au menu précédent*/
		else if(choosen_card == 4){
			System.out.println("\n Action annulée !\n");
			return 0;
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
	 *       
	 * @param mode_jeu
	 *       the game mode which had been chosen.
	 *       
	 * @return an int which represents either the transaction occured properly;
	 * 
	 * 1 = transaction has worked
	 * 0 = transaction canceled by the user
	 * -1 = transaction failed
	 */
	
	public int achatCarte(Joueur joueur, Affichage affichage) {
		
		affichage.showBoard(this);
		Objects.requireNonNull(this);
		Objects.requireNonNull(joueur);
		
		var carte = Saisie.achatCarteNonReservee(1);
		return validationAchatNonReserve(joueur, carte);
		
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
		
		return -1; 	/*Aucune reservation*/
	}
	
	
	
	/**
	 * Allows the users to choose the number of player they wants.
	 * 
	 * @return
	 * the number of player choosen. In this specific mode it must be the maximum allowed by this mode, 2, there is no choice.
	 */
	@Override
	public int choixNbJoueurs() {
		return this.giveNbPlayersPossible();	
	}
}

