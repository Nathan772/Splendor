
import java.util.Objects;

/*Demander si utile mettre une record avec un seul champ*/
public record Jeton(String couleur) {
	
	/**
	 * Constructeur du type Jeton.
	 * 
	 * @param couleur
	 *        Couleur que le Jeton doit avoir.
	 */
	public Jeton{
		Objects.requireNonNull(couleur);
		//Ici mettre verrification que la couleur est bien existante
		//On peut donc couper la fonction de Saisie.Jeton() pour faire une qui récupère la couleur
		//et créer une focntion qui renvoie true si couleur possible et false sinon à mettre ici
	}
	
	/**
	 * String representation of a Jeton.
	 */
	@Override
	public String toString() {
		
		if(this.couleur.equals("Vert")) {
			return "Emeraude";
		}
		if(this.couleur.equals("Rouge")) {
			return "Rubis";
		}
		if(this.couleur.equals("Bleu")) {
			return "Saphyr";
		}
		if(this.couleur.equals("Jaune")) {
			return "Or";
		}
		if(this.couleur.equals("Blanc")) {
			return "Diamant";
		}

		return "Onyx";
	}
	
	/**
	 * En semble des tests des méthodes du type Jeton.
	 */
	public static void main(String[] args) {
		
		var jeton = new Jeton("Blanc");
		
		System.out.println("Vous avez obtenu une pierre précieuse : " + jeton);
	}
}
