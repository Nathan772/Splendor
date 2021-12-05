import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Joueur {
	
	/**
	 * Champs constituant la classe Joueur
	 */
	public int cartes;
	public final String pseudo;
	public final int age;
	public int points_prestiges;
	public HashMap<String, Integer> ressources;
	public HashMap<String, Integer> bonus;
	public ArrayList<CarteDev> reserve;

	/**
	 * Constructeur du type Joueur
	 * 
	 * @param pseudo
	 * 		  Nom du joueur
	 * 
	 * @param age
	 * 		  Age du joueur
	 * 
	 * @param points_prestiges
	 * 		  Nombre de points de prestiges
	 */
	public Joueur(String pseudo, int age, int points_prestiges) {
		
		Objects.requireNonNull(pseudo, "Pseudo hasn't been given");
	
		if(age <= 0) {
			throw new IllegalArgumentException();
		}
		
		if(points_prestiges < 0) {
			throw new IllegalArgumentException("Prestige points can't be under 0");
		}
		
		this.cartes = 0;
		this.pseudo = pseudo;
		this.age = age;
		this.points_prestiges = points_prestiges;
		this.reserve = new ArrayList<CarteDev>();
		this.bonus = new HashMap<>();
		this.ressources = new HashMap<>();
		this.initRessourcesMap();
		this.initBonus();
		
		
	}
	
	
	public void initBonus() {
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc");
		
		for(var elem : couleurs) {
			this.bonus.put(elem, 0);
		}	
	}
	

	/**
	 * Initialise les ressources que possède un joueur.
	 */
	public void initRessourcesMap() {
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc", "Jaune");
		
		for(var elem : couleurs) {
			this.ressources.put(elem, 0);
		}	
	}
	
	/**
	 * Représentation textuelle d'un joueur.
	 */
	@Override
	public String toString() {
		return "Joueur [pseudo= "+ this.pseudo + ", age= " + this.age + ", prestige= " + points_prestiges + ", ressources=" + this.ressources +"]"; 
	}

	/**
	 * Constructeur du type Joueur. Est appelée si on crée un joueur sans donner de points de prestiges.
	 * Les points seront donc initialisés à 0.
	 * 
	 * @param pseudo
	 *        Nom du joueur
	 *        
	 * @param age
	 *        Age du joueur
	 */
	public Joueur(String pseudo, int age) {
		this(pseudo, age, 0);
	}
	
	/**
	 * Retire la quantité définie d'une ressource du joueur.
	 * 
	 * @param type_ressource
	 *        Nom d'une des couleurs représentant une ressource du jeu
	 *        
	 * @param val
	 *        Quantite de la ressource a enlever
	 * 
	 * @return Nouvelle quantité de la ressource restante
	 */
	public int enleveRessource(String type_ressource, int val) {
		
		int old_val = this.ressources.get(type_ressource);
		
		
		if(val < this.bonus.get(type_ressource)) {
			val  = 0;
		}
		else {
			val = val - this.bonus.get(type_ressource);
		}
		
		
		if(old_val < val) {
			return 0;
		}
		
		return old_val - val;
	}
	
	
	/**
	 * Réécriture de la fonction equals, permet de savoir si deux Joueurs sont egaux ou non. Renvoie
	 * true si deux joueurs sont egaux et false sinon.
	 * 
	 * @param player
	 *        Joueur à comparer.
	 *        
	 * @return True si les deux joueurs sont les mêmes et false sinon.
	 */
	@Override
	public boolean equals(Object player) {
		
		return player instanceof Joueur joueur
				&& age == joueur.age
				&&pseudo.equals(pseudo);
	}
	
	
	/**
	 * Ajoute le bonus du jeton donnée au joueur.
	 * 
	 * @param jeton_bonus
	 *        Jeton considéré comme un bonus à ajouter.
	 */
	public void addBonus(Jeton jeton_bonus) {
		
		this.ressources.put(jeton_bonus.couleur(), this.ressources.get(jeton_bonus.couleur()) + 1);
	}
	
	
	
	/**
	 * Ajout d'une quantité définie d'une ressource du joueur.
	 * 
	 * @param type_ressource
	 *        Nom d'une des couleurs représentant une ressource du jeu
	 *        
	 * @param val
	 * 		  Quantite de la ressource a enlever
	 * 
	 * @return Nouvelle quantité de la ressource ajoutée
	 */
	public int ajouteRessource(String type_ressource, int val) {
		return this.ressources.get(type_ressource) + val;
	}
	
	
	/**
	 * Achat d'une carte du jeu par le joueur. La carte est acheté si la ressource 
	 * demandée est possèdée en quantité suffisante par le joueur.
	 * 
	 * @param carte
	 *        Carte à acheter
	 * 
	 * @return true si la carte a bien pu être achetée et false sinon
	 */
	public boolean acheteCarte(CarteDev carte, Partie game) {
		
		if(!this.checkMoney(carte)) {
			return false;
		}
		
		
		this.addPrestige(carte.points());
		
		for(var elem : carte.coût().entrySet()) {
			
			var name = elem.getKey();
			var val = elem.getValue();
			
			int nouv_val;
			
			nouv_val = this.enleveRessource(name, val);
			this.ressources.put(name, nouv_val);
			
			game.jetons_disponibles.put(name,  game.jetons_disponibles.get(name) + nouv_val);
			
		}
		
		this.addBonus(new Jeton(carte.couleur()));
		this.cartes += 1;
		
		return true;
	}
	
	
	
	
	public boolean isNobleVisiting(ArrayList<Tuile> nobles_visiting, ArrayList<Tuile> tuiles_board){
		
		for(var noble : tuiles_board) {
			
			
			var is_visiting = true;
			
			for(var couleur_price : noble.cout().entrySet()) {
				var name_price = couleur_price.getKey();
				var val_price = couleur_price.getValue();
				
				if(!(this.bonus.get(name_price) >= val_price)) {
					is_visiting = false;
				}
			}
			
			if(is_visiting) {
				nobles_visiting.add(noble);
			}
			
		}
		
		if(nobles_visiting.size() != 0) {
			return true;
		}
		return false;
	}
	
	
	
	private boolean checkMoney(CarteDev card) {
		
		for(var elem : card.coût().entrySet()) {
			
			var name = elem.getKey();
			var val = elem.getValue();
			
			if(this.ressources.get(name) < val) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Ajout d'une quantite définie de points de prestiges au joueur
	 * 
	 * @param 
	 *        Quantite de points de prestiges
	 */
	public void addPrestige(int val) {
		int result = this.points_prestiges + val;
		
		if(result < 0) {
			result = 0;
		}
		
		this.points_prestiges = result;
	}

	/**
	 * Ajout d'une quantite définie d'une ressource.
	 * 
	 * @param jeton
	 * 		  Jeton représentant la ressource
	 * 
	 * @param quantite
	 *        Quantite de ressource à ajouter
	 */
	public void addRessource(Jeton jeton, int quantite) {
		
		int quantite_total = this.ressources.get(jeton.couleur()) + quantite;
		
		this.ressources.put(jeton.couleur(), quantite_total);
	}
	
	
	/**
	 * Vérifie que le nombre de jetons d'un joueur ne dépasse pas 10.
	 * 
	 * @return True si l'utilisateur possèdent 10 jetons ou moins et false sinon
	 */
	public boolean checkNbJetons() {
		
		int count = 0;
		
		for(var nb_jet : this.ressources.values()) {
			count = count + nb_jet;
		}
		
		if(count > 10) {	// mettre une constate pour 10
			return false;
		}
		
		return true;
	}

	
	
	
	
	public boolean reserveCarte(CarteDev carte, HashMap<String, Integer> ressources_jeu) {
		
		Objects.requireNonNull(carte, "Card given to keep is null");
		
		if(this.reserve.size() == 3) {
			return false;
		}
		
		this.reserve.add(carte);
		
		
		if(ressources.get("Jaune") > 0) {
			
			this.addRessource(new Jeton("Jaune"), 1);			
		}
		
		return true;
	}
	
	
	
	
	
	/**
	 * Ensemble des tests de méthodes pour le type Joueur
	 */
	public static void main(String[] args) {
		
		/*Tests de la classe Joueur*/
		
		var j1 = new Joueur("Dylan", 10);
		
		System.out.println(j1);
		
		j1.addPrestige(7);
		System.out.println(j1.points_prestiges);
		
		j1.addPrestige(-5);
		System.out.println(j1.points_prestiges);
		
		j1.addPrestige(-10);
		System.out.println(j1.points_prestiges);
		
		
	}

}






/*
 * 
 * 
 * 
 * 
 * 
 * Verifier qu'il n'y a pas deux joueurs identiques (pas le même nom)
 * 
 * 
 * 
 * Supprimer les ajoute de bonus, et champs bonus, on fera els calculs directement sur les ressources du joueur.
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * */




