package projet;

import java.util.Objects;
import java.util.Scanner;

public class Saisie {
	
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
	
public static Joueur saisieJoueur(){
		  Scanner scanner = new Scanner(System.in);
		  var joueur = new Joueur(scanner.next(), scanner.nextInt());
		  return joueur;
		}
	
	public static Boolean isExistingColours(String chaine){
		
		Objects.requireNonNull(chaine);
		
		if(chaine.equals("Vert") != true && chaine.equals("Rouge") != true && chaine.equals("Bleu") != true 
				&& chaine.equals("Jaune") != true
				&& chaine.equals("Blanc") != true && chaine.equals("Noir") != true) {
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(isExistingColours("Rouge"));
	}

}
