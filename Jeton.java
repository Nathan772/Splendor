package projet;
import java.util.Objects;

/*Demander si utile mettre une record avec un seul champ*/
public record Jeton(String couleur) {
	
	public Jeton{
		Objects.requireNonNull(couleur);
	}
	
	/**
	 * Ecriture human-readable des jetons
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
	 * Ensemble des tests des méthodes du type Jeton
	 */
	public static void main(String[] args) {
		
		var jeton = new Jeton("Noir");
		
		System.out.println("Vous avez obtenu une pierre précieuse : " + jeton);
	}
}
