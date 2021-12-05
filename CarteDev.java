import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


record CarteDev(int niveau, String couleur, int points , String object, HashMap<String, Integer> coût) implements Carte {
	
	
	/**
	 * Constructeur du type Carte
	 * 
	 * @param couleur
	 * @param coût
	 * @param points
	 */
	public CarteDev{
		
		/*Verifier avec switchs toutes les couleurs*/
		
		Objects.requireNonNull(couleur, "Carte de couleur null");
		
		if(points < 0) {
			throw new IllegalArgumentException("Points de prestiges négatifs de la carte");
		}
		
	}
	
	
	public static CarteDev fromText(String line) {
		
		var couleurs = List.of("Blanc", "Bleu", "Vert", "Rouge", "Noir");
		var tab = line.split(" - ");
		var cout = new HashMap<String, Integer>();
		
		int cout_couleur;
		
		for(int i = 4; i < tab.length ;i++) {
			
			cout_couleur = Integer.parseInt(tab[i]);
			
			if(cout_couleur != 0) {
				cout.put(couleurs.get(i - 4), cout_couleur);
			}
		}
		
		return new CarteDev(Integer.parseInt(tab[0]), tab[1], Integer.parseInt(tab[2]), tab[3], cout);
	}
	
	@Override
	public String toString() {

		var ligne = " ―――――――――――――――――――― \n";
		var cout = new StringBuilder();
		var separator = "| ";
	
		var first_line = "|  " + this.points + "          " + this.couleur + "   |\n";
		var vide = "|                    |\n";
		var name = "|        " + this.object + "        |\n";
		
		for(var elem : this.coût().entrySet()) {
			
			var couleur_name = elem.getKey();
			var couleur_val = elem.getValue();
			
			cout.append(separator).append(couleur_name).append(": ").append(couleur_val).append("            |\n").append(vide);
		}
		
		return ligne + vide + first_line + vide + vide + name + vide + vide + cout + vide + ligne;
	}
	
	
}
