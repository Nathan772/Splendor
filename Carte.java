import java.util.Objects;


public record Carte(String couleur, int coût, int points) {
	
	
	/**
	 * Constructeur du type Carte
	 * 
	 * @param couleur
	 * @param coût
	 * @param points
	 */
	public Carte{
		
		/*Verifier avec switchs toutes les couleurs*/
		
		Objects.requireNonNull(couleur, "Carte de couleur null");
		
		if(coût < 0) {
			throw new IllegalArgumentException("Coût négatif de la carte");
		}
		
		if(points < 0) {
			throw new IllegalArgumentException("Points de prestiges négatifs de la carte");
		}
		
	}
	
}
