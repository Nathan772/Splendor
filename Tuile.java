import java.util.HashMap;
import java.util.List;


/**
 * 
 * @author dylandejesus
 */
record Tuile(String name, HashMap<String, Integer> cout, int points_prestiges) implements Carte{
	
	/**
	 *
	 * 
	 * @param line
	 * 
	 * @return 
	 */
	public static Tuile fromText(String line) {
		
		var couleurs = List.of("Blanc", "Rouge", "Bleu", "Noir", "Vert");
		var tab = line.split(" - ");
		var cout = new HashMap<String, Integer>();
		
		int cout_couleur;
		
		for(int i = 2; i < tab.length ;i++) {
			
			cout_couleur = Integer.parseInt(tab[i]);
			
			if(cout_couleur != 0) {
				cout.put(couleurs.get(i - 2), cout_couleur);
			}
		}
		
		return new Tuile(tab[1], cout,  Integer.parseInt(tab[0]));
	}
	
	
	
	
	private static String chaineFormatte(String name_card) {	//FONCTION QUI SERAIT À METTRE DANS UNE INTERFACE DE CARTEDEV		
		
		if(name_card == null) {
			return "|        mine        |\n";
		}
		int nb_espaces = (20 - name_card.length()) / 2;
		var chaine = new StringBuilder();
		var espaces = new StringBuilder();
		
		chaine.append("|");
		
		
		for(var i = 0; i < nb_espaces ;i++) {
			espaces.append(" ");
		}
		
		chaine.append(espaces).append(name_card).append(espaces);
		
		if(name_card.length() % 2 == 1) {
			chaine.append(" ");
		}
		
		chaine.append("|\n");
		
		return chaine.toString();
	}
	
	
	
	/**
	 * String représentation of a noble.
	 */
	@Override
	public String toString() {

		var ligne = " ―――――――――――――――――――― \n";
		var cout = new StringBuilder();
		var separator = "| ";
	
		var first_line = "|  " + this.points_prestiges + "                 |\n";
		var vide = "|                    |\n";
		var name = chaineFormatte(this.name);
		var spaces_separator = "";
		
		for(var elem : this.cout().entrySet()) {
			
			var couleur_name = elem.getKey();
			var couleur_val = elem.getValue();
			
			if(couleur_name.length() == 5) {
				spaces_separator = "           |\n";
			}else {
				spaces_separator = "            |\n";
			}
			
			cout.append(separator).append(couleur_name).append(": ").append(couleur_val).append(spaces_separator).append(vide);
		}
		
		return ligne + vide + first_line + vide + vide + name + vide + vide + cout + vide + ligne;
	}
}

