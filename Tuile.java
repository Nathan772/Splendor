import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

record Tuile(String name, HashMap<String, Integer> cout, int points_prestiges) implements Carte{
	
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
	
	@Override
	public String toString() {

		var ligne = " ―――――――――――――――――――― \n";
		var cout = new StringBuilder();
		var separator = "| ";
	
		var first_line = "|  " + this.points_prestiges + "                  |\n";
		var vide = "|                    |\n";
		var name = "|        " + this.name + "        |\n";
		
		for(var elem : this.cout().entrySet()) {
			
			var couleur_name = elem.getKey();
			var couleur_val = elem.getValue();
			
			cout.append(separator).append(couleur_name).append(": ").append(couleur_val).append("            |\n").append(vide);
		}
		
		return ligne + vide + first_line + vide + vide + name + vide + vide + cout + vide + ligne;
	}
}
