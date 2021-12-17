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
	
	static Boolean valide_defausse(Joueur joueur, String jeton, int nombre){
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
	
	/* accepte une valeur saisie si elle est comprise dans un intervalle */
	public static int choix_intervalle(int v1, int v2) {
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
	 * @return
	 */
	
	/* fait un scanner et recommence tant que ce n'est pas un entier */
	public static int capture_int() {
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
	 * 
	 * @return
	 */
	
	/* écriture au format X-Y*/
	public static String[] choix_carte() {
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
	public static int carte_reserve_valide_2arg(Partie game, int niveau_carte, int num_carte ) {
		try {
			game.board().get(niveau_carte).get(num_carte);
		}catch(Exception e) {
			return 0;
		}
		return 1;
	
	}

	/**
	 * Chexk if the card is pickable
	 * 
	 * @param game
	 * @param niveau_carte
	 * @return
	 */
	
	/* récupère niveau d'une carte selon la partie : arg : partie + niv */
	public static int carte_reserve_valide_1arg(Partie game, int niveau_carte) {
		try {
			game.pioche().get(niveau_carte).get(game.pioche().size() - 1);
		}catch(Exception e) {
			return 0;
		}
		return 1;
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
