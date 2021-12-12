import java.util.Objects;
import java.util.Scanner;

public class Saisie {
	
	/**
	 * Effectue la saisie d'un jeton. Renvoie un jeton si le jeton a bien pu être créé.
	 * L'utilisateur est guidé par un échange entre lui et la console.
	 * 
	 * @return Le Jeton créé à partir de la couleur choisie.
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
	 * Crée un joueur à partir des informations que donne l'utilisateur.
	 * L'utilisateur renseigne donc le nom ou pseudo ainsi que son âge.
	 * 
	 * @return L'objet Joueur créé.
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
	 * Détermine si la chaîne donnée représente une couleur existante dans le jeu.
	 * Si oui elle renvoie true sinon false.
	 * 
	 * @param chaine
	 *        Chaîne représentant une couleur.
	 * 
	 * @return True ou False selon si la couleurs appartient au jeu.
	 */
	public static Boolean isExistingColours(String chaine){
		
		Objects.requireNonNull(chaine);
		
		if(chaine.equals("Vert") != true && chaine.equals("Rouge") != true && chaine.equals("Bleu") != true 
				&& chaine.equals("Blanc") != true && chaine.equals("Noir") != true) {
			return false;
		}
		return true;
	}
	
	/**
	 * Ensemble des tests liés à la classe Saisie.
	 * 
	 * @param args
	 *        Paramètres de la ligne de commande.
	 */
	public static void main(String[] args) {
		System.out.println(isExistingColours("Rouge"));
	}

}
