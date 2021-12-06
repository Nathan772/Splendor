import java.util.Scanner;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Arrays;
import java.util.Collections;

public class Partie implements TypePartie{
	
	private static final int VICTORY_POINTS = 15;
	
	public ArrayList<Joueur> joueurs;
	private HashMap <Integer, List<CarteDev>> pioche;
	private ArrayList<CarteDev> piocheTuile;
	private int taille_pioche;
	public HashMap <Integer, List<CarteDev>> board;
	public HashMap<String, Integer> jetons_disponibles;	/*On met une Map pour représenter les piles de jetons*/
	public ArrayList<Tuile> tuiles_board;

	
	
	/**
	 * Constructeur du type Partie.
	 */
	public Partie() {
		joueurs = new ArrayList<Joueur>();
		
		
		// Pioche des 4 niveaux de développement
		pioche = new HashMap<>();
		pioche.put(1, new ArrayList<>());
		pioche.put(2, new ArrayList<>());
		pioche.put(3, new ArrayList<>());
		
		taille_pioche = 0;
		
		// Board des 4 niveaux de développement
		board = new HashMap<>();
		board.put(1, Arrays.asList(null, null, null, null));
		board.put(2, Arrays.asList(null, null, null, null));
		board.put(3, Arrays.asList(null, null, null, null));
		
		jetons_disponibles = new HashMap<String, Integer>();
		
		tuiles_board = new ArrayList<>();
	}
	
	/**
	 * Initialise une partie. Correspond à la préparation du plateau.
	 */
	public void initialisePartie(int mode) {
		
		this.initialiseOrdreJoueurs();
		this.initialiseJetons();
		
		
		//Mode de jeu 1
		if(mode == 1) {
			
			this.initialisePioche();
			this.piocheFourCards(1);
			
		}else {
			
			try{
				this.loadDeck(Path.of("Cartes_Devs.txt"));
				
			}catch(IOException e) {
		    	System.out.println(e.getMessage());
		    	System.exit(1);
		    
		    }
			
			this.piocheFourCards(1);
			this.piocheFourCards(2);
			this.piocheFourCards(3);
			
			this.initialiseTuiles();
			
		}
	}
	
	
	
	private void loadTuiles(Path path, ArrayList<Tuile> tuiles) throws IOException {
		Objects.requireNonNull(path, "File path given is null");
		
		Tuile carte;
		
		try(var reader = Files.newBufferedReader(path)) {  //On teste et renvoie une erreur si problème
			 String line;
			 while ((line = reader.readLine()) != null) {
				 
				 carte = Tuile.fromText(line);
				 

				 tuiles.add(carte);
				 
			 }
		} // appelle reader.close()
	}
	
	private void  initialiseTuiles() {
	
		
		var all_tuiles = new ArrayList<Tuile>();
		
		try {
			this.loadTuiles(Path.of("Tuiles.txt"), all_tuiles);
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		
		for(var i = 0; i < this.joueurs.size() + 1 ;i++) {
			
			Collections.shuffle(all_tuiles);
			
			this.tuiles_board.add(all_tuiles.get(all_tuiles.size() - 1));
			
			all_tuiles.remove(all_tuiles.size() - 1);
		}
		
		
	}
	
	
	/**
	 * Modifie l'odre de la liste des joueurs afin que celui qui commence soit le joueur, le plus jeune.
	 */
	public void initialiseOrdreJoueurs() {
		
		int i = 0;
		ArrayList<Joueur> extrait = new ArrayList<Joueur>();
		ArrayList<Joueur> base = new ArrayList<Joueur>();
		
		int indice_younger = findYounger(this.joueurs);
		
		
		for(var joueur : this.joueurs) {
			
			if(i < indice_younger) {
				extrait.add(joueur);
			}else {
				base.add(joueur);
			}
			
			i++;
		}
		
		for(var joueur_extrait : extrait) {
			base.add(joueur_extrait);
		}
		
		this.joueurs = base;
	}

	
	
	/**
	 * Renvoie l'indice du joueur le plus jeune de la liste de joueurs.
	 * Renvoie la valeur -1 si problème rencontré.
	 * 
	 * @param joueurs
	 *        Liste de joueurs dont on doit trouver le plus jeune
	 *        
	 * @return Indice du joueur le plus jeune
	 */
	public static int findYounger(ArrayList<Joueur> joueurs) {
		
		int i = 0;
		int indice_younger = -1;
		
		Joueur younger = joueurs.get(i);
		
		for(var joueur : joueurs) {
			
			if(younger.age > joueur.age) {
				younger = joueur;
				indice_younger = i;
			}
			
			i++;
		}
		
		return indice_younger;
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
	
	/**
	 * Initialise la pioche consituée de cartes de manière aléatoire. Chaque carte possède une des couleurs
	 * suivantes : Rouge, Vert, Noir, Bleu, Blanc, Jaune, Blanc.
	 */
	public void initialisePioche() {
		
		/*Les cartes de la pioche ne peuvent que possèder ces couleurs*/
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc");
		
		/*Pour chaque couleur on crée 8 cartes*/
		for(var elem : couleurs) {
			for(int i = 0; i < 8 ;i++) {
				
				var map = new HashMap<String, Integer>();
				map.put(elem, 1);
				
				this.pioche.get(1).add(new CarteDev(0,elem,3,null,map));
				this.taille_pioche += 1;
			}
		}
		
		/*On mélange la pioche*/
		Collections.shuffle(this.pioche.get(1));
	}
	
	/**
	 * Pioche une carte dans la pioche et l'ajoute au plateau. Si on choisit de piocher une carte
	 * on supprime le carte du plateau et on rajoute à sa place la carte en haut de la pioche
	 * 
	 * @param  index_supp
	 * 		   Indice de la carte à remplacer dans la liste
	 */
	public CarteDev piocheOneCard(int ligne, int index_supp) {
		
		int derniere_carte = this.pioche.get(ligne).size() - 1;	

		CarteDev card_picked = this.pioche.get(ligne).remove(derniere_carte);

		
		this.board.get(ligne).set(index_supp, card_picked);
		
		return card_picked;
		
	}
	
	
	/**
	 * Pioche 4 cartes developpement dans la pioche.
	 */
	public void piocheFourCards(int ligne) {
		
		for(int i  =0; i < 4 ;i++) {
			this.piocheOneCard(ligne, i);
		}
		
		this.taille_pioche = 4;
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

        Objects.requireNonNull(list, "Array is null (indexOfMin)");

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
	public boolean enleveRessource(Jeton jeton, int quantite) {
		
		int quantite_total = this.jetons_disponibles.get(jeton.couleur()) - quantite;
		
		if(quantite_total < 0) {
			return false;
		}
		
		this.jetons_disponibles.put(jeton.couleur(), quantite_total);
		
		return true;
	}
	
	
	public void loadDeck(Path path) throws IOException {
		
		Objects.requireNonNull(path, "File path given is null");
		
		CarteDev carte;
		
		try(var reader = Files.newBufferedReader(path)) {  //On teste et renvoie une erreur si problème
			 String line;
			 while ((line = reader.readLine()) != null) {
				 
				 carte = CarteDev.fromText(line);
				 

				 this.pioche.get(carte.niveau()).add(carte);
				 
			 }
		} // appelle reader.close()
		
		Collections.shuffle(this.pioche.get(1));
		Collections.shuffle(this.pioche.get(2));
		Collections.shuffle(this.pioche.get(3));
	}
	
	
	/**
	 * Déroulement de la partie.		   
	 */
	public static void main(String[] args) {
		
		Joueur joueur;
		
		//Valeurs de jeu par défaut
		int mode_jeu = 1;
		int nb_joueurs = 2;
		int tour = 0;
		int choix;
		int choosen_card;
		
		int ligne_choosen = 1;
		/* indique si le tour du joueur c'est bien passé ou s'il faut le faire rejouer car il aurait réalisé une action impossible*/
		/* 0 = le joueur a simplement demandé ses infos, c'est à nouveau à lui de jouer, 1 = le tour précédent s'est bien passé, -1 = problème lors du tour précédent*/
		int tour_valide = 1;
		boolean points_victoires = false; 
		var scanner = new Scanner(System.in);	/*Lire les valeurs d el'utilisateur*/
		
		Partie game = new Partie();
		
		
		
		System.out.println("Quel mode de jeu (1 ou 2) choisissez vous ?  => ");
		
		if(scanner.next().equals("2")) {
			mode_jeu = 2;
			
			System.out.println("Combien de joueurs (entre 2 et 4)  participent à la partie ? => ");
			nb_joueurs = scanner.nextInt();
		}
		
		
		
		/*Enregistre les deux joueurs*/
		
		for(int i = 1; i <= nb_joueurs ;i++) {
			System.out.println("Veuillez entrer le nom et l'âge du joueur " + i);
		
			var joueur1 = Saisie.saisieJoueur();
				
			game.addPlayer(joueur1);
		}
		
		/*Initialisation de la partie*/
		game.initialisePartie(mode_jeu);
		

		
		
		/*Tant que 15 points de prestiges n'ont pas été atteint avec fin du tour*/
		
		while((!points_victoires) || (tour % game.joueurs.size() != 0)) {
			
			joueur = game.joueurs.get(tour % nb_joueurs); 	
			
			
			if(tour_valide != 0)
				AffichageLigneCommande.showPlateau(game, mode_jeu);
			
			if(tour_valide == -1)
				System.out.println("C'est de nouveau au tour de " + joueur.pseudo + " car il a essayé de faire une action impossible au tour précédent ! \n");

			System.out.println("ooooooooooooooooooooooo TOUR DU JOUEUR : " + joueur.pseudo + " ooooooooooooooooooooooo\n");
			
			
			
			System.out.println("(1) Acheter une carte\n(2) Prendre des ressources\n(3) Voir mes informations");
			
			if(mode_jeu != 1) {
				System.out.println("(4) Réserver une carte");
			}
			
			choix = scanner.nextInt();
			
			
			if(choix == 1) {
				
				/*--------- Achat de cartes --------------*/
				AffichageLigneCommande.showBoard(game);
				/* on oblige l'utilisateur a acheter une carte face visible si il est en mode de jeu 1*/
				var choix_mode_achat = 1;
				
				
				if(mode_jeu == 2) {
					System.out.println("\n1) Acheter une carte face visible\n2) Acheter une carte réservée");
					choix_mode_achat = scanner.nextInt();
				}
					
				
				if(choix_mode_achat == 2 ) {
					
					
					System.out.println("Affichage de la réserve qui fait tout buguer");
					System.out.println(joueur.reserve);
					System.out.println("\nChoisissez votre numero de carte\n");
					int carte_numero = scanner.nextInt();
					if(joueur.acheteCarte(joueur.reserve.get(carte_numero), game)) {
						tour_valide = 1;	
					}
					/* cas où la carte coûte trop cher, l'utilisateur revient au menu précédent de force car il ne peut pas se payer cette carte*/
					else {
						tour_valide = -1;
						System.out.println("\nVous n'avez pas assez de ressources pour acheter cette carte !\n");
					}	
				}
				
				else {
					System.out.println("\n\nChoisissez le numéro de la carte à acheter \n");
					
					if(mode_jeu != 1) {
						System.out.println("(Niveau - N° Carte)");
						
					
						var tab = scanner.next().split("-");
						System.out.println(tab[1]);
						choosen_card = Integer.parseInt(tab[1]) - 1;
						
						ligne_choosen = Integer.parseInt(tab[0]);
						
					}else {
						choosen_card = scanner.nextInt() - 1;
					}
					
					System.out.println("Appuyez sur (5) pour revenir au menu précédent \n ");
					
					
			
					
					if(choosen_card <= 3) {
						
						if(joueur.acheteCarte(game.board.get(ligne_choosen).get(choosen_card), game)) {
							tour_valide = 1;
							game.piocheOneCard(ligne_choosen, choosen_card);
							
						}
						/* cas où la carte coûte trop cher, l'utilisateur revient au menu précédent de force car il ne peut pas se payer cette carte*/
						else {
							tour_valide = -1;
							System.out.println("\nVous n'avez pas assez de ressources pour acheter cette carte !\n");
						}	
					}
					/* l'utilisateur revient au menu précédent*/
					else if(choosen_card == 4){
						System.out.println("\n Action annulée !\n");
						tour_valide = 0;
					}
					/* cas où la carte n'existe pas, l'utilisateur revient au menu précédent de force car le numéro de carte n'existe pas*/
					else {
						tour_valide = -1;
						System.out.println("\n Ce numéro de carte n'existe pas !\n");
					}
					
					
				}
			}
			
			else if(choix == 2){
				
				/*---------- Prendre ressources ------------*/
				
				AffichageLigneCommande.showJeton(game.jetons_disponibles, true);
				
				
				
				System.out.println("\nVoulez vous : \n\n(1) Prendre 2 jetons de la même couleur\n(2) Prendre 3 jetons de couleurs différentes "); //Mettre dans Saisie
				System.out.println("(3) Annuler votre action \n");
				choix = scanner.nextInt();
				
				
				if(choix == 1) {
					
					System.out.println("Vous avez choisi de prendre deux jetons de la même couleur, veuillez précisez leur couleur ");
					
					int valid_choice = 0;
					
					while(valid_choice != 1) {
						var jeton = Saisie.saisieJeton();
						
						//Faire une fonction qui fait simultanément les deux actions
						if(game.jetons_disponibles.get(jeton.couleur()) < 4) {
							
							System.out.println("\n/!\\ Il n'y a pas assez de ressources pour effectuer cette action\n");
							
						}else {
							joueur.addRessource(jeton,2);
							game.enleveRessource(jeton, 2);
							valid_choice += 1;
						}
					}
					
					
				    
				    tour_valide = 1;
	
				}
				else if(choix == 2){
					
					var already_choosen = new ArrayList<Jeton>();
					boolean suite;
					System.out.println("Vous avez choisi de prendre 3 jetons différents, veuillez précisez leur couleur ");
					System.out.println(" (en les séparant par la touche entrée) : \n");
					
					for(int i= 0; i < 3;i++){
						
						suite = false;
						
						while(!suite) {
							
							var jeton = Saisie.saisieJeton();	 //On accède à la classe Saisie pour la saisie des jetons

							
							if(!already_choosen.contains(jeton)) {
								
								
								if(!game.enleveRessource(jeton, 1)) {
										
									System.out.println("\n/ ! \\ Pas assez de ressource pour effectuer cette action\n");
									i --;
									break;
								}
									
								joueur.addRessource(jeton,1);
							    already_choosen.add(jeton);
							    suite = true;
								
							}else {
								System.out.println("\n/ ! \\ Couleur déjà choisie\n");
							}
						}
						  
					}
					tour_valide = 1;
				}
				/*cas où l'utilisateur souhaite annuler son action*/
				else if(choix == 3){
					System.out.println("\n Action annulée !\n");
					tour_valide = 0;
				}
				/* cas où l'utilisateur appuie sur une valeur impossible, on le fait recommencer son tour */
				else {
					tour_valide = -1;
					System.out.println("\n Cette action n'existe pas ! \n");
				}
			}
			
			else if(choix == 4) {
				
				/*Réservation de cartes*/
				
				System.out.println("1) Réserver une carte du board\n2) Réserver une carte d'une des pioches\n\n");
				
				var choix_scanner = scanner.nextInt();
				
				if(choix_scanner == 1) {
					System.out.println("Choisissez une carte du plateau\n(Niveau - N°Carte)\n");
					
					var scan = scanner.next().split("-");
					
					var niveau_carte = Integer.parseInt(scan[0]);
					var num_carte = Integer.parseInt(scan[1]) - 1;
					

					
					
					game.enleveRessource(new Jeton("Jaune"), 1);
					
					joueur.reserveCarte(game.board.get(niveau_carte).get(num_carte), game.jetons_disponibles);
					
					game.piocheOneCard(niveau_carte, num_carte);
					
				}
				
				if(choix_scanner == 2) {
					System.out.println("Donnez le niveau de carte que vous voulez piocher\n");
					
					
					game.enleveRessource(new Jeton("Jaune"), 1);
					
					joueur.reserveCarte(game.pioche.get(scanner.nextInt()).get(game.pioche.size() - 1), game.jetons_disponibles);
					
					/*Supprimer la carte de la pioche si pas fait dans la fonction*/
				}
				
				
				AffichageLigneCommande.showReserved(joueur);
				
				tour_valide = 1;
			}
			
			
			else {
				/* affichage des infos user et comme cela est automatiquement fait plus tard, on ne fait rien*/
				/*AffichageLigneCommande.showJoueur(joueur);*/
				System.out.println("\n Action annulée !\n");
				tour_valide = 0;
			}
			
			AffichageLigneCommande.showJoueur(joueur);

			
			//Le joueur a atteint 15 points, une des conditions de fin de jeu est atteinte
			
			if(joueur.points_prestiges >= VICTORY_POINTS && !points_victoires) {
				points_victoires = true;
			}
			
			
			
			
			
			if(!joueur.checkNbJetons()) {
				
				while(!joueur.checkNbJetons()) {
					
					System.out.println("\n/!\\ Vous possèdez trop de jetons veuillez en supprimer pour en avoir 10 maximum\n");
					AffichageLigneCommande.showJeton(joueur.ressources, false);
					
					System.out.println("\nJeton");
					
					var jeton = scanner.next();
					
					System.out.println("Quantite"); 
					
					var quantite = scanner.nextInt();
					
					joueur.ressources.put(jeton, joueur.enleveRessource(jeton, quantite));
					
					game.jetons_disponibles.put(jeton, game.jetons_disponibles.get(jeton) + quantite);
				}
				
			}
			
			
			
			
			/*Visit de Noble*/
			
			var nobles_visiting = new ArrayList<Tuile>();
			
			if(joueur.isNobleVisiting(nobles_visiting, game.tuiles_board)) {
				
				Tuile noble_chosen;
				
				
				if(nobles_visiting.size() == 1) {
					
					System.out.println("\n.....Un noble vient à votre visite\nIl s'agit de.... " + nobles_visiting.get(0).name() + " !\n");
					
					noble_chosen = nobles_visiting.get(0);
					
				}else {
					
					var nobles_name = new StringBuilder();
					var separator = "";
					
					for(var elem : nobles_visiting) {
						
						nobles_name.append(separator).append(elem.name());
						separator = ", ";
					}
					System.out.println("\n.....Un noble vient à votre visite\nIl s'agit de....." + nobles_name + " !\n\nChoisissez en un\n\n");
					
					noble_chosen = nobles_visiting.get(scanner.nextInt());
				}
				
				int i = 0;
				
				for(var noble : game.tuiles_board) {
					
					if(noble.equals(noble_chosen)) {
						joueur.points_prestiges += noble.points_prestiges();
						game.tuiles_board.remove(i);
					}
					
					i++;
				}
			}
			

			/*on vide le scanner pour éviter que le tour se termine sans que le joueur n'ait appuyé sur entrée*/
			scanner.nextLine();
			
			if(tour_valide == 1) {
				
				System.out.println("\nVeuillez appuyer sur la touche 'Entrée' pour terminer votre tour : \n");
				scanner.nextLine();	//On part au tour suivant une fois le joueur ayant mis Entrer
				tour ++;
			}
		}
		
		scanner.close();
		
		System.out.println("\nFÉLICIATIONS !!!!!!  " + game.isWinner()); // Fin de partie
	
	}
	
	
}







/*
========================================================= Remarque/ Notes générales ========================================================================
Probablement faire une interface carte developpement contenant les cartes de chaque niveau dans la liste que l'on prendra
Demander si pas gênant d'avoir autant de champs dans une classe
Interface entre cartes developpement et tuiles nobles
AU LIEU DE FAIRE UN CHAMPS BONUS, ON VA MPLUTÔT DIRECGTEMENT AJOUTER DABS LES RESSOURCES PERSOS QUAND ON ACHETE LA CARTE
Ne pas oublier de faire en sorte que si la taille de la pioche vaut 0 alors on ne peut plus tiré de carte, donc partie terminée.
Pour l'affichage, pas oublier que si valeurs à deux chiffres ond oit formatter les cases.
*/
