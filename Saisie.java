package projet;

import java.util.Objects;
public class Saisie {
	
	public static Boolean isExistingColours(String chaine){
		Objects.requireNonNull(chaine);
		if(chaine.equals("Vert") != true && chaine.equals("Rouge") != true && chaine.equals("Bleu") != true 
				&& chaine.equals("Jaune") != true
				&& chaine.equals("Blanc") != true && chaine.equals("Onyx") != true) {
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(isExistingColours("Rouge"));
	}

}
