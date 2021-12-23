import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

/**
 * Declaration of the class Saisie. It gathers all the functions which are in relation
 * with the user. It asks to his answers in the console
 * 
 * @author dylandejesus nathanbilingi
 */
public class Saisie {
	
	/**
	 * Performs the input of a token. Returns a token if the token has successfully been created.
	 * The user is guided by an exchange between him and the console.
	 * 
	 * @return Token created with the informations given
	 */
	public static Jeton saisieJeton(){
		
		
		Scanner scanner = new Scanner(System.in);
		String text = scanner.next();  //User rentre "Violet"
 
		while(!isExistingColours(text)){  //La méthode renvoie true donc on entre dans la boucle

		    System.out.println("Valeur non recevable, veuillez entrer une nouvelle valeur.\n");
		    text = scanner.next();  //User rentre "Vert"
		}
		
		var jeton = new Jeton(text);  // Jeton Vert

		return jeton;
	}
	/**
	 * Performs the input of a token name. Returns a the token's name if the token name exists.
	 * The user is guided by an exchange between him and the console.
	 * 
	 * @return String created with the informations given
	 */	
	public static String saisieJeton_name(){
			
			Scanner scanner = new Scanner(System.in);
			String text = scanner.next();  //User rentre "Violet"
	 
			while(!isExistingColours(text)){  //La méthode renvoie true donc on entre dans la boucle
	
			    System.out.println("Valeur non recevable, veuillez entrer une nouvelle valeur.\n");
			    text = scanner.next();  //User rentre "Vert"
			}
			
			return text;
		}
	
	/**
	 * Creates a player from the information the user provides. The user enters the name/nickname 
	 * and his age.
	 * 
	 * @return Player created
	 */
	public static Joueur saisieJoueur(){
		
		Scanner scanner = new Scanner(System.in);
		Joueur joueur = null;
		
		do{
				try {
					joueur = new Joueur(scanner.next(), Integer.parseInt(scanner.next()));
			}catch(Exception e) {
				System.out.println("Erreur : Entrez un nom avec des lettres et un âge avec des nombres, pas des lettres !");
			}
		}while(joueur == null);
		System.out.println("joueur enregistré avec succès !");
		return joueur;
	}
	
	/**
	 * Determines if the given String represnts an existing color in the game. If the color exists
	 * it returns true and false if it doesn't exist.
	 * 
	 * @param chaine
	 *        String thaht represents the colour
	 * 
	 * @return True if it exists or False
	 */
	public static Boolean isExistingColours(String chaine){
		
		Objects.requireNonNull(chaine);
		
		if(chaine.equals("Vert") != true && chaine.equals("Rouge") != true && chaine.equals("Bleu") != true 
				&& chaine.equals("Blanc") != true && chaine.equals("Noir") != true) {
			return false;
		}
		return true;
	}
	
	static Boolean valideDefausse(Joueur joueur, String jeton, int nombre){
		if(nombre <= 0) {
			System.out.println("Cette action est inutile car vous vous êtes défaussés de  0 jeton. ");
			System.out.println("On recommence ! ");
			System.out.println("Couleur : ");
			return false;
		}
		else if(joueur.ressources().get(jeton) >= nombre){
			return true;	
		}
		System.out.println("Vous n'avez pas suffisamment de ressource de ce type, il faut réessayer.  ");
		System.out.println("Couleur : ");
		return false;
	}
	
	static int nb_jeton_defausse(){
		Scanner scan = new Scanner(System.in);
		int nb;
		do{
			try {
				nb = scan.nextInt();
				return nb;
			}catch(Exception e) {
			System.out.println("Erreur : Veuillez entrer un numero de jeton valide : ");
			}
		}while(true);
		
	}
	
	/**
	 *  This method allows the player to write a value if it is between two others (both are included)
	 * 
	 * @param v1 
	 * the lowest value that the user can write 
	*	@param v2
	 * the highest value that the user can write
	*  @return the choosen value
	 */
	public static int choixIntervalle(int v1, int v2) {
		Scanner scan = new Scanner(System.in);
		int choix;
		do{
				try {
				choix = scan.nextInt();
			}catch(Exception e) {
				System.out.println("Erreur : Veuillez entrez une valeur entre " + v1 + " et " +v2);
				scan.next();
				choix = -1;
			}
		}while(choix < v1 || choix > v2);
		return choix;
	}
	
	/**
	 * Check if the input is indeed a number.
	 * 
	 * @return the integer written by the user
	 */
	public static int captureInt() {
		Scanner scan = new Scanner(System.in);
		int choix;
		do{
			try {
				choix = scan.nextInt();
				return choix;
			}catch(Exception e) {
			System.out.println("Erreur : Veuillez entrer un numero de carte valide : ");
			}
		}while(true);
	
	}
	
	
	/**
	 * The card choice 
	 * This method enable the user to choose a valid card value. If they fail it resumes thanks to a loop.
	 * @return a String[] which contains the choosen card informations : its number and its level.
	 */
	public static String[] choixCarte() {
		Scanner scan = new Scanner(System.in);
		int succes = -1;
		String[] tab = {"a"};
		do{
				try {
					tab = scan.next().split("-");
					/* test pour voir si les valeurs récupérées sont au bon format, en cas d'erreur on retry*/
					var choosen_card = Integer.parseInt(tab[1]) - 1;
					var ligne_choosen = Integer.parseInt(tab[0]);
					succes = 1;
			}catch(Exception e) {
				System.out.println("Erreur : veuillez écrire au format : Niveau - N° Carte ");
				/*scan.next();*/
				succes = -1;
			}
		}while(succes == -1);
		
		return tab;
		
	}
	public static int carte_reserve_valide_2arg(Mode game, int niveau_carte, int num_carte ) {
		try {
			game.board().get(niveau_carte).get(num_carte);
		}catch(Exception e) {
			return 0;
		}
		return 1;
	
	}

	/**
	 * Check if the card is pickable
	 * 
	 * @param game
	 * @param niveau_carte
	 * @return
	 */
	
	/* récupère niveau d'une carte selon la partie : arg : partie + niv */
	public static int carte_reserve_valide_1arg(Mode game, int niveau_carte) {
		try {
			game.pioche().get(niveau_carte).get(game.pioche().size() - 1);
		}catch(Exception e) {
			return 0;
		}
		return 1;
	}
	
	/**
	 * Enables the user to choose the party type he wants.
	 * 
	 * @param game 
	 * This variable gives information regarding the current party
	 * @return
	 */
	public static int saisieMode() {
		int mode_jeu;
		System.out.println("Quel mode de jeu (1 ou 2) choisissez-vous ?  => ");
		mode_jeu = choixIntervalle(1,2);
		return mode_jeu;
	}
	
	
	/**
	 * Enables the user to choose the party type he wants.
	 * 
	 * @param game 
	 * This variable gives information regarding the current party
	 * @return
	 */
	public static int saisieAffichage() {
		int mode_affichage;
		System.out.println("Quel affichage souhaitez vous ? (1) Affichage en ligne de commande, (2) Affichage Graphique  =>  ");
		mode_affichage = choixIntervalle(1,2);
		return mode_affichage;
	}
	
	
	/**
	 * Allows the users to choose the number of player they wants.
	 * 
	 * @param game
	 * This variable gives information regarding the current party
	 * @param mode_jeu
	 * This variable gives information regarding the game mode (1 or 2)
	 * @return
	 */
	public static int choixNbJoueurs(int max) {
		System.out.println("Combien de joueurs participent à la partie (choisissez un nombre entre 2 et 4) ?");
		var nb_joueurs = Saisie.choixIntervalle(2, max);
		//saisie_joueur(game,mode_jeu,nb_joueurs);
		return nb_joueurs;	
	}
	
	/**
	 * Allows the users to write their names, and their age
	 * 
	 * @param game
	 * This variable gives information regarding the current party
	 * @param mode_jeu
	 * This variable gives information regarding the game mode (1 or 2)
	 * @param nb_joueurs
	 * This variable gives information regarding the game mode (1 or 2)
	 * @return
	 */
	public static Mode saisieJoueurs(Mode game, int mode_jeu, int nb_joueurs) {
		
		Objects.requireNonNull(game);
		
		if(mode_jeu != 1 && mode_jeu != 2) {
			throw new IllegalArgumentException("mode_jeu !=1 && mode_jeu != 2");
		}
		
		if(nb_joueurs < 2 || nb_joueurs > 4) {
			throw new IllegalArgumentException("nb_joueurs > 4 or nb_joueurs < 2");
		}
		
		for(int i = 1; i <= nb_joueurs ;i++) {
			System.out.println("Veuillez entrer le nom et l'âge du joueur " + i);
		
			var joueur1 = Saisie.saisieJoueur();	
			game.addPlayer(joueur1);
		}
		return game;
	}
	
	/**
	* This method allows to write the player's answers for each game option.
	* @param mode_jeu 
	* it refers to the gameplay choosen by the users
	*
	* @return it returns an int which allow to know what actions the player wants to do 
	*/
	public static int menuSaisie(int mode_jeu) {
	        int answer;

	        if(mode_jeu != 1 && mode_jeu != 2) {
	            throw new IllegalArgumentException(" 'mode_jeu' must be a value between 1 and 2");
	        }
	        
	    
	        
	        System.out.println("(1) Acheter une carte\n(2) Prendre des ressources\n(3) Voir mes informations");
	        if(mode_jeu != 1) {
	            System.out.println("(4) Réserver une carte");
	        }
	        
	        answer = choixIntervalle(1, 4);
	        /*if(answer == 3){
	            Affiche infos joueur
	            return menuSaisie(mode_jeu);
	        }*/
	        return answer;        
	}
	
	/**
	 *  This method allows the player to buy a card
	 * 
	 * @param mode_jeu 
	 * the game mode choosen by the users
	*	@param game
	 * the game state 
	*  @return the new game state
	 */
	public static int choixAchatCarte(Mode game, Affichage affichage) {
		
		Objects.requireNonNull(game);

		affichage.showBoard(game);
		
		System.out.println("\n1) Acheter une carte face visible\n2) Acheter une carte réservée");
		return Saisie.choixIntervalle(1, 2);

	}
	
	/**
	 *  This method allows the player to choose a card
	 * 
	 * @param mode_jeu 
	 * the game mode choosen by the users 
	*  @return an Hashmap which contains the card data : its number and its level. key = level, value = number
	 */
	public static HashMap<Integer, Integer> achatCarteNonReservee(int mode_jeu) {
		
		if(mode_jeu != 1 && mode_jeu != 2) {
			throw new IllegalArgumentException("mode_jeu must be 1 or 2");
		}
		int choosen_card;
		/* on attribue à ligne_choosen la valeur un par défaut, elle sera toujours utilisée telle quelle si on est dans le mode de jeu 1*/
		int ligne_choosen = 1;
		System.out.println("\n\nChoisissez le numéro de la carte à acheter \n");
		
		if(mode_jeu != 1) {
			System.out.println("(Niveau - N° Carte)");	
			var tab = Saisie.choixCarte();
			System.out.println(tab[1]);
			choosen_card = Integer.parseInt(tab[1]) - 1;
			ligne_choosen = Integer.parseInt(tab[0]);
		//13 lignes	
		}else {
			choosen_card = Saisie.captureInt()- 1;
		}	
		var carte = new HashMap<Integer,Integer>();
		carte.put(ligne_choosen, choosen_card);
		return carte;
		
	}
	
	/* fonction permettant d'acheter une carte réservée
	 */
	/**
	 *  This method allows the player to buy a card that were placed in his reserve
	 * 
	 * @param joueur
	 * the player whom we will look at the reserve
	*  @return an int which is the chronological order of appearance of the chosen card
	*  -2 : le numéro de carte n'existe pas
	*  -1 : il n'y a pas de carte dans la réserve.
	*  1 : transaction succeed 
	*  another number means : the card position +1 
	 */
	public static int achatCarteReservee(Joueur joueur, Affichage affichage) {
		Objects.requireNonNull(joueur);
		/* cas où la partie avec les cartes réservées n'est pas vide*/
		if(joueur.reserve().size() > 0) {
			System.out.println("\nChoisissez votre numero de carte parmi les suivantes \n");
			affichage.showReserved(joueur);
			var carte_numero = Saisie.captureInt();
			/* cas où le numéro de la carte est valide*/
			if(carte_numero <= joueur.reserve().size() && carte_numero > 0) { 
				return carte_numero;
				/* cas où l'utilisateur peut payer */
				//validationAchatReserve(game, nb_joueurs, mode_jeu, points_victoires, tour, tour_valide, joueur, carte_numero);
			}
			/* cas où le numéro de la carte n'est pas valide*/
			else {
				System.out.println("\nCe numéro de carte n'existe pas !\n");
				return -2;
			}
		}
		/* cas où la partie avec les cartes réservées est vide*/
		else {
			/* échec, la partie avec les cartes réservées est vide*/
			System.out.println("\nVous n'avez pas réservé de carte ! Cette action est donc impossible ! \n");
			System.out.println("\nVeuillez réserver une carte avant de vouloir acheter une carte réservée ! \n");
			return -1;
		}
		//20 lignes*
	}
	
	/* fonction permettant d'acheter une carte réservée
	 */
	/**
	 *  This method allows the player to buy a card that were placed in his reserve
	 * 
	 * @param joueur
	 * the player whom we will look at the reserve
	*  @return int 
	*  an int which is the chronological order of appearance of the chosen card
	 */
	public static int choixJeton(){
		System.out.println("\nVoulez vous : \n\n(1) Prendre 2 jetons de la même couleur\n(2) Prendre 3 jetons de couleurs différentes "); //Mettre dans Saisie
		System.out.println("(3) Annuler votre action \n");
		return Saisie.choixIntervalle(1,3);
	}
	
	/**
	 *  This method allows the player to choose from where he plans to reserve a card
	 * 
	*  @return int
	*  an int which represents the place from where the user wants to take its card.
	 */
	public static int choixReservation() {
		/*Réservation de cartes*/
		
		System.out.println("1) Réserver une carte du board\n2) Réserver une carte d'une des pioches\n\n");
		
		var choix_scanner = Saisie.choixIntervalle(1, 2);
		return choix_scanner;
	}
	
	/**
	 *  This method allows the player to choose a card from the board and reserve it
	 * 
	*  @return HashMap
	*  it represents the card that the user has chosen : key : its level and value : its number.
	 */
	public static HashMap<Integer, Integer>reservationCartePlateau() {
		/*Réservation de cartes*/
		System.out.println("Choisissez une carte du plateau\n(Niveau - N°Carte)\n");
		var tab = Saisie.choixCarte();
		var niveau_carte = Integer.parseInt(tab[0]);
		var num_carte = Integer.parseInt(tab[1]) - 1;
		var card = new HashMap<Integer, Integer>();
		card.put(niveau_carte, num_carte);
		return card;
	}
	
	/**
	 * This method enables to the user to choose the card's level he wants to reserve
	 * 
	 * 
	 * @return int
	 * an int which represents the card level between 1 and 3.
	 * 
	 */
	public static int reservationCartePioche(){
		System.out.println("Donnez le niveau de carte que vous voulez piocher\n");
		
		var niveau_carte = Saisie.choixIntervalle(1,3);
		
		return niveau_carte;
	}
	
	/**
	 *  enables to the user to end its turn
	 * 
	 * 
	 * @return int
	 * an int which represents the card level between 1 and 3.
	 * 
	 */
	public static void saisieFinTour(){
		Scanner scan = new Scanner(System.in);
		String choix = "a";
		do{
			try {
				choix = scan.nextLine();
			}catch(Exception e) {
			choix = null;
			System.out.println("Erreur : Veuillez taper sur entrée ");
			}
		}while(choix == null);
	}
	
	
	
	/**
	 * All the function's test in relation with the class Saisie.
	 * 
	 * @param args
	 *       Parameters of the console
	 */
	public static void main(String[] args) {
		
		System.out.println(isExistingColours("Rouge"));
	}
}