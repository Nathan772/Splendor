import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Arrays;
import java.util.Collections;

public class Partie {
	
	private static final int VICTORY_POINTS = 15;
	
	public ArrayList<Joueur> joueurs;
	private ArrayList<Carte> pioche;
	private int taille_pioche;
	private List<Carte> board;
	private HashMap<String, Integer> jetons_disponibles;	/*On met une Map pour représenter les piles de jetons*/
	//taille pioche (peut-être)
	
	
	/**
	 * Constructeur du type Partie.
	 */
	public Partie() {
		joueurs = new ArrayList<Joueur>();
		pioche = new ArrayList<Carte>();
		taille_pioche = 0;
		board = Arrays.asList(null, null, null, null);
		jetons_disponibles = new HashMap<String, Integer>();
	}
	
	/**
	 * Initialise une partie. Correspond à la préparation du plateau.
	 */
	public void initialisePartie() {
		this.initialisePioche();
		this.initialiseJetons();
		this.piocheFourCards();
	}

	
	/**
	 * Initialise les jetons disponibles d'une partie. Il y a 7 jetons par couleur
	 */
	
	public void initialiseJetons() {
		this.jetons_disponibles.put("Rouge", 7);	/*On mettra une constante dans la classe pour 7 jetons*/
		this.jetons_disponibles.put("Vert", 7);
		this.jetons_disponibles.put("Bleu", 7);
		this.jetons_disponibles.put("Noir", 7);
		this.jetons_disponibles.put("Jaune", 7);
		this.jetons_disponibles.put("Blanc", 7);
	}
	
	
	/*Méthode initialisant une pioche aléatoire des cartes*/
	
	
	/**
	 * Initialise la pioche consituée de cartes de manière aléatoire. Chaque carte possède une des couleurs
	 * suivantes : Rouge, Vert, Noir, Bleu, Blanc, Jaune, Blanc.
	 */
	public void initialisePioche() {
		
		/*Les cartes de la pioche ne peuvent que possèder ces couleurs*/
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc" ,"Jaune");
		
		/*Pour chaque couleur on crée 8 cartes*/
		for(var elem : couleurs) {
			for(int i = 0; i < 8 ;i++) {
				this.pioche.add(new Carte(elem,3,1));
				this.taille_pioche += 1;
			}
		}
		
		/*On mélange la pioche*/
		Collections.shuffle(this.pioche);
	}
	
	
	/**
	 * Pioche une carte dans la pioche et l'ajoute au plateau. Si on choisit de piocher une carte
	 * on supprime le carte du plateau et on rajoute à sa place la carte en haut de la pioche
	 * 
	 * @param  index_supp
	 * 		   Indice de la carte à remplacer dans la liste
	 */
	public Carte piocheOneCard(int index_supp) {
		
		int derniere_carte = this.taille_pioche - 1;
		
		this.taille_pioche -= 1;
		
		Carte carte_picked = this.pioche.remove(derniere_carte);
		
		this.board.set(index_supp, carte_picked);
		
		return carte_picked;
		
	}
	
	
	/**
	 * Pioche 4 cartes developpement dans la pioche.
	 */
	public void piocheFourCards() {
		
		for(int i  =0; i < 4 ;i++) {
			this.piocheOneCard(i);
		}
	}
	
	/**
	 * Ajoute une joueur à la liste des joueurs de la partie.
	 * 
	 * @param  player
	 * 		   Joueur a ajouté
	 */
	public void addPlayer(Joueur player) {
		this.joueurs.add(player);
	}
	
	/**
	 * Détermine qui parmis la liste de joueurs est le vainqueur. Le vainqueur
	 * est celui possèdant le plus de points de prestiges. Si deux joueurs
	 * possèdent le même nombre de points, on prend celui qui possède le moins
	 * de cartes.
	 */
	public Joueur isWinner() {
		
		ArrayList<Joueur> classement = new ArrayList<Joueur>();
		int nb_best_players = 0;
		
		
		for(var joueur : this.joueurs) {
			
			if(joueur.points_prestiges >= VICTORY_POINTS) {
				classement.add(joueur);
				nb_best_players += 1;
			}
		}
		
		sortClassement(classement, nb_best_players);
		return topClassement(classement);
	}
	
	/**
	 * Echange deux valeurs de la liste de joueurs. On echange les deux valeurs se trouvant
	 * aux indices 'index1' et 'index2'.
	 * 
	 * @param  index1
	 *         Premier indice
	 * 
	 * @param  index2
	 * 	       Second indice
	 */
	private static void swap(ArrayList<Joueur> list, int index1, int index2) {
		
		Joueur tmp = list.get(index2);

        list.set(index1, list.get(index2));
        list.set(index2, tmp);
	}
	
	/**
	 * Trouve le joueur ayant le plus de points de prestiges dans la liste de joueur.
	 * Si deux joueurs ont le même nombre de points de prestiges on garde celui qui possède
	 * le moins de cartes de développement. La Méthode renvoie l'objet de type Joueur.
	 * 
	 * @param  classement
	 * 		   Liste des joueurs
	 */
	public static Joueur topClassement(ArrayList<Joueur> classement) {
		
		Joueur best_player = classement.get(0);
		int i = 0;
		
		while(classement.get(i).points_prestiges == best_player.points_prestiges && i < classement.size() - 1) {
			
			if(classement.get(i).cartes < best_player.cartes) {
				best_player = classement.get(i);
			}
			
			i++;
		}
		
		return best_player;
	}
	
	/**
	 * Renvoie l'index du joueur ayant le moins de points de prestiges dans l'intervalle d'indexs [debut, fin[.
	 * 
	 * @param  list
	 * 			Liste de joueurs
	 * 
	 * @param  debut
	 *         Entier représentant l'index de début
	 * 
	 * @param  fin
	 *         Entier représentant l'index de fin
	 */
	private static int indexOfMin(ArrayList <Joueur> list, int debut, int fin){

        //Objects.requireNonNull(list, "Array is null (indexOfMin)");

        if(debut < 0){
            throw new IllegalArgumentException("Debut index given under 0 (indexOfMin)");
        }
        if(fin < 0){
            throw new IllegalArgumentException("End index given under 0 (indexOfMin)");
        }

        Joueur min = list.get(debut);
        int index = debut;
        int min_index = debut;

        for(var i = debut; i < fin; i++){
            if(compareJoueur(min, list.get(i))){
                min = list.get(i);
                min_index = index;
            }
            index ++;
        }

        return min_index;
    }
	
	/**
	 * Renvoie true si j1 possède moins de points de prestiges que j2
	 * 
	 * @param  j1
	 * 		   Joueur 1
	 * 
	 * @param  j2
	 *         Joueur 2
	 */
	public static boolean compareJoueur(Joueur j1, Joueur j2){
		
		if(j1.points_prestiges < j2.points_prestiges) {
			return true;
		}
        return false;
    }
	
	/**
	 * Trie la liste de joueurs dans l'ordre Croissant. La comparaison entre les joueurs se fait sur
	 * le nombre de points de prestiges, à points de prestiges égaux c'est le joueur possèdant le moins de
	 * cartes développement qui l'emporte.
	 * 
	 * @param  classement
	 * 	 	   Liste de joueurs
	 * 
	 * @param  size
	 * 		   Taille de la liste 
	 */
	public static boolean sortClassement(ArrayList<Joueur> classement, int size) {
		
		for(var i = 0; i < size ; i++){
            swap(classement, i, indexOfMin(classement, i, size));     /*On va jusqu'à array.length car indexOfMin() va dans l'intervalle [debut, fin[*/
        }
		
		return true;
	}
	
	/**
	 * Enlève une quantité définie de jetons disponibles.
	 * 
	 * @param jeton
	 * 		  Type du jeton à supprimer
	 * 
	 * @param quantite
	 * 		  Quantité de jetons à supprimer
	 * 			
	 *
	 */
	public void enleveRessource(Jeton jeton, int quantite) {
		
		int quantite_total = this.jetons_disponibles.get(jeton.couleur()) - quantite;
		
		this.jetons_disponibles.put(jeton.couleur(), quantite_total);
	}
	
	
	/**
	 * Déroulement de la partie.		   
	 */
	public static void main(String[] args) {
		
		Joueur joueur;
		int tour = 0;
		boolean points_victoires = false; 
		var scanner = new Scanner(System.in);	/*Lire les valeurs d el'utilisateur*/
		
		Partie game = new Partie();
		
		
		
		/*METTRE L'ENREGISTREMENT DANS UNE MÉTHODE*/
		/*Enregistre les deux joueurs*/
		
		for(int i = 1; i <= 2 ;i++) {
			System.out.println("Veuillez entrer le nom et l'âge du joueur " + i);
		
			joueur = new Joueur(scanner.next(), scanner.nextInt());
				
			game.addPlayer(joueur);
		}
	
		
		/*Initialisation de la partie*/
		game.initialisePartie();
		
		
		
		/** Note : Ecrire une méthode qui trie la liste de joueurs dans l'ordre d'âge puis sens aiguille d'une montre*/
		
		
		/*Tant que 15 points de prestiges n'ont pas été atteint avec fin du tour*/
		
		while((!points_victoires) || (tour % game.joueurs.size() != 0)) {
			
			joueur = game.joueurs.get(tour % 2); 	/** Note : Là on devra changer le 2 en nombre de joueur dans la partie*/
			
			
			/** Note : Mettre des méthodes d'affichage plus propres (peut-être dans une classe à part AffichageCommande)*/
			
			System.out.println("\nPlateau :\n" + game.board + "\n");
			System.out.println("\nJetons :\n" + game.jetons_disponibles + "\n\n");
			System.out.println("Tour du joueur : " + joueur.pseudo + "\n");
			System.out.println("(1) Acheter une carte\n(2) Prendre des ressources\n");
			

			if(scanner.nextInt() == 1) {
				
				/*--------- Achat de cartes --------------*/
				
				System.out.println("\nChoisissez le numéro de la carte à acheter");
				
				int choosen_card = scanner.nextInt() - 1;
				
				if(choosen_card <= 4) {
					
					if(joueur.acheteCarte(game.board.get(choosen_card))) {
						
						game.piocheOneCard(1);
						
					}else {
						
						System.out.println("\nVous n'avez pas assez de ressources pour acheter cette carte !\n");
					}	
				}
				
				
			}else {
				
				/*---------- Prendre ressources ------------*/
				
				System.out.println("\nVeuillez entrer trois noms de couleur");
				/*on vide le scanner*/
				scanner.nextLine();
				/** Note : Mettre ça dans une boucle for(3)*/
				/*var jeton1 = new Jeton(scanner.nextLine());
				var jeton2 = new Jeton(scanner.nextLine());
				var jeton3 = new Jeton(scanner.nextLine());
				
				joueur.addRessource(jeton1, 3);*/ 	/** Note : Au lieu de la quantité il faudra mettre le nombre*/
				/*game.enleveRessource(jeton1, 3);*/   /** Note : On fera plutôt une focntion qui ajoute la ressource au joueur et enlève du plateau*/
				
				/*joueur.addRessource(jeton2, 3);
				game.enleveRessource(jeton2, 3);
				
				joueur.addRessource(jeton3, 3);
				game.enleveRessource(jeton3, 3);*/
				
				for(int i= 0; i < 3;i++){
					  var jeton = Saisie.saisieJeton();	 //On accède à la classe Saisie pour la saisie des jetons
					  joueur.addRessource(jeton,3);
					  game.enleveRessource(jeton, 3);
					}
			}
			
			System.out.println(joueur + "\n\n");	//Affiche l'état du joueur à la fin de sont tour

			
			//Le jouer a atteint 15 points, une des conditions de fin de jeu est atteinte
			
			if(joueur.points_prestiges >= VICTORY_POINTS && !points_victoires) {
				points_victoires = true;
			}
			
			tour ++;
			
		}
		
		System.out.println("\nFélicitations " + game.isWinner());
		
		System.out.println("\nFin");	//Fin de jeu
		
		
		
		
		/*
		game.piocheOneCard(1);
		
		
		System.out.println(game.board);
	
		
		j.acheteCarte(game.board.get(choosen_card));
		
		System.out.println(j);
		j.showRessources();
		
		*/
		/*scanner.close();*/
	}
	
}












/*
========================================================= Remarque/ Notes générales ========================================================================
Probablement faire une interface carte developpement contenant les cartes de chaque niveau dans la liste que l'on prendra
Demander si pas gênant d'avoir autant de champs dans une classe
*/


 

