import java.util.Objects;
import java.util.HashMap;
import java.util.List;

public class Joueur {

	public int cartes;
	public final String pseudo;
	public final int age;
	public int points_prestiges;
	public HashMap<String, Integer> ressources;
	public HashMap<String, Integer> bonus;

	
	
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
		this.ressources = new HashMap();
		this.initRessourcesMap();
		this.bonus = new HashMap();
		
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
		
		if(old_val < val) {
			return 0;
		}
		
		return old_val - val;
	}
	
	
	
	@Override
	public boolean equals(Object player) {
		
		return player instanceof Joueur joueur
				&& age == joueur.age
				&&pseudo.equals(pseudo);
	}
	
	
	/**
	 * 
	 * @param jeton_bonus
	 */
	public void addBonus(Jeton jeton_bonus) {
		
		if(this.bonus.getOrDefault(jeton_bonus.couleur(), null) == null) {
			this.bonus.put(jeton_bonus.couleur(), 1);
		}else {
			
			var incrementation = this.bonus.getOrDefault(jeton_bonus.couleur(), null) + 1;
			this.bonus.put(jeton_bonus.couleur(), incrementation);
		}
	}
	
	
	/**
	 * 
	 * @param jeton
	 * @param initial_val
	 * @return
	 */
	public int newCost(Jeton jeton, int initial_val) {
		
		int nouv_val = initial_val;
		var isBonus = this.bonus.getOrDefault(jeton.couleur(), null);
		
		if(isBonus != null) {
			nouv_val -= isBonus;
		}
		
		return nouv_val;
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
	public boolean acheteCarte(Carte carte) {
		/*FAIRE TEST DE PRÉSENCE ETC...*/
		
		int nouv_val;
		int portefeuille = this.ressources.get(carte.couleur());
		
		if(portefeuille >= carte.coût()) {
			
			this.addPrestige(carte.points());
			
			int nouv_cout = this.newCost(new Jeton(carte.couleur()),carte.coût());
			
			
			nouv_val = this.enleveRessource(carte.couleur(), nouv_cout);
			this.ressources.put(carte.couleur(), nouv_val);
			this.addBonus(new Jeton(carte.couleur()));
			this.cartes += 1;
			
		}else {
			return false;
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
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * */



