package fr.umlv.affichage;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.ScreenInfo;
import fr.umlv.zen5.Event.Action;
import fr.umlv.game.Partie;
import fr.umlv.game.mode.*;
import fr.umlv.objects.CarteDev;
import fr.umlv.objects.Tuile;
import fr.umlv.players.*;

public class AffichageGraphique implements Affichage{
	
	
	/**
	 * Champs context de l'objet graphique
	 */
	private ApplicationContext context; 
	
	private ArrayList<String> listActions;
	
	
	public AffichageGraphique() {
		this.context = null;
		this.listActions = new ArrayList<String>();
		
	};
	
	
	
	
	public void launchAffichage() {

		
		Application.run(Color.ORANGE, context -> {
			
			
			this.context = context;
			Partie.startGame(this);
			
			return;
			
		});
	}

	
	
	
	
	/**
	 * Print the game board of a game.
	 * 
	 * @param game
	 *        Game to print its board
	 * 
	 * @param mode
	 *        Game mode
	 */
	public void showPlateau(Mode game, int mode) {
		
		var area = new Area();
		
		
		float width = context.getScreenInfo().getWidth();
		float height = context.getScreenInfo().getHeight();
		
		showBoardGraph(context, game, Math.round(width) / 15, Math.round(height) / (5 + 1)); /*15 = nombres de cartes par lignes, 5 = nombre de piles*/
        
        showJetonMap(game.jetons_disponibles(), "", context);
        
        showTuiles(context, game, Math.round(width) / 15, Math.round(height) / (5 + 1));
        
        area.drawBoxInstructions(context, "1) Acheter une carte \n2) Prendre des ressources \n3) Réserver une carte\n ");
        
        area.drawBoxActions(context, listActions);
        
	}
	
	
	
	
	/**
	 * 
	 * Print the available tokens on the console. It i représented as a board.
	 * 
	 * @param ressources
	 *        Resources of tokens
	 */
	public static void showJetonMap(HashMap<String, Integer> ressources, String message, ApplicationContext context) {
	
		
		
		context.renderFrame(graphics -> {
			
			int i = 0;
			
			for(var elem : ressources.entrySet()) {
				drawGem(context, 1200, 300  + (i * 75), 35, elem.getValue(),  elem.getKey());	/*Ma*/
				
				i++;
				
			}
	        
	    });
	}

	
	/**
	 * Print the cards of the board of the game token.
	 * 
	 * @param game
	 *        Game token to print it board
	 */
	public void showBoard(Mode game) {
		
		showPlateau(game, 0);
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * Print the available tokens on the console. It i représented as a board.
	 * 
	 * @param ressources
	 *        Resources of tokens
	 */
	public void showJeton(HashMap<String, Integer> ressources, String message) {
		
		return;
	}
	
	
	
	
	
	
	
	
	
	

	/**
	 * Print the cards of the board of the game token.
	 * 
	 * @param game
	 *        Game token to print it board
	 */
	public static void showBoardGraph(ApplicationContext context, Mode game, int largeur, int hauteur) {
		
		Objects.requireNonNull(game);
		
		var area = new Area();
		
		float x, y;
		
		
		y = 2;
		
		for(int i = 1 ; i < game.board().size() + 1 ; i++) {
			
			x = 4;
			var num_card = 1;
				
			
			if(game.board().get(i).get(0) != null) {
				
				area.drawCardPioche(context, largeur, (y * hauteur) + (10 * y), largeur, hauteur, Integer.toString(i));
				
				for(var carte : game.board().get(i)) {
					
					if(carte != null) {
						area.drawCard(context, (x * largeur) + (10 * x) , (y * hauteur) + (10 * y), largeur, hauteur, carte); // 10 = espace entre les cartes
					}
						
					
					num_card ++;
					
					x = x + 1;
				}
			}
			
			y = y + 1;
		}
	}
	
	
	
	
	
	/**
	 * Print the player informations.
	 * 
	 * @param joueur
	 * 	      Player given to show its game infomations 
	 */
	public int showJoueur(Participant joueur) {
		
		System.out.println("Joueur : " + joueur.pseudo() + "\n\nPoints de prestiges : " + joueur.points_prestiges() + "\n\nRessources : \n");
		System.out.println("Joueur :  " + "\n");
		/*showJeton(joueur.ressources(), "JETON");*/
		/*showJeton(joueur.bonus(), "BONUS");*/
		showReserved(joueur);
		
		return 1;
	}
	
	/**
	 * Print the Nobles Cards on the board.
	 * 
	 * @param game
	 *        Game given to show its nobles card on the board
	 */
	public void showTuiles(Mode game){
		
		showPlateau(game, 0);
	}
	
	
	public void showTuiles(ApplicationContext context, Mode game, int largeur, int hauteur){
		
		System.out.println("    -- NOBLES --   \n\n");
		
		float x, y;
		
		var area = new Area();
		
		
		x = 4;
		y = 1;
		
		for(int i = 0 ; i < game.tuiles_board().size(); i++) {
			
			area.drawCardNoble(context,(x * largeur) + (10 * x), (y * hauteur) - (30 * y), largeur, hauteur, game.tuiles_board().get(i));
			
			x = x + 1;
		}
	}
	
	
	/**
	 * Print the cards reserved by a player.
	 * 
	 * @param joueur
	 * 		  Player given.
	 */
	public void showReserved(Participant joueur) {
		
		Objects.requireNonNull(joueur);
		
		if(joueur.reserve().size() == 0) {
			System.out.println("Vous ne possèdez aucune carte réservée \n\n");
			return;
		}
		
		System.out.println("Cartes que vous avez réservé \n\n");
		
		for(var elem : joueur.reserve()) {
			System.out.println(elem + "\n");
		}
	}
	
		
	static class Area {
		
	    private Ellipse2D.Float ellipse = new Ellipse2D.Float(0, 0, 0, 0);
	    
	    void draw(ApplicationContext context, float x, float y) {
	      context.renderFrame(graphics -> {
	        // hide the previous rectangle
	        graphics.setColor(Color.ORANGE);
	        graphics.fill(ellipse);
	        
	        // show a new ellipse at the position of the pointer
	        graphics.setColor(Color.MAGENTA);
	        ellipse = new Ellipse2D.Float(x - 20, y - 20, 40, 40);
	        graphics.fill(ellipse);
	      });
	    }
	    
	  
	    
	    
	    private static void drawBoxInstructions(ApplicationContext context, String instructions){
	    	
	    	context.renderFrame(graphics -> {
	    		
	    		
	    		var x  = context.getScreenInfo().getWidth();
	    		var y = context.getScreenInfo().getHeight();
	    		
	    		var rectangle = new Rectangle2D.Float(x - ((25 * x) / 100), ((5 * y) / 100), ((20 * x) / 100) , ((20 * y) / 100));
	    		
	    		graphics.setColor(Color.BLACK);
	    		
	    		graphics.fill(rectangle);
	    		
	    		
	    		graphics.setColor(Color.WHITE);
	    		
	    		graphics.setFont(new Font("SansSerif", Font.BOLD, 10));
	    		
	    		
	    		
	    		var y_offset = 1;
	    		
	    		for(var elem : instructions.split("\n")) {
	    			graphics.drawString(elem, x - ((25 * x) / 100) + ((2 * x) / 100), ((5 * y) / 100) + (((5 * y) / 100) * y_offset));
	    			
	    			y_offset ++;
	    		}
	    		
	    		
	    		
	    		
	    	});
	    	
	    	
	    }
	    
	    
	    private static void drawBoxActions(ApplicationContext context, ArrayList<String> actions){
	    	
	    	context.renderFrame(graphics -> {
	    		
	    		
	    		var x  = context.getScreenInfo().getWidth();
	    		var y = context.getScreenInfo().getHeight();
	    		
	    		var rectangle = new Rectangle2D.Float(((5 * x) / 100), ((5 * y) / 100), ((20 * x) / 100) , ((20 * y) / 100));
	    		
	    		graphics.setColor(Color.BLACK);
	    		
	    		graphics.fill(rectangle);
	    		
	 
	    		
	    		
	    		
	    		
	    		
	    		var y_offset = 1;
	    		
	    		graphics.setColor(Color.RED);
	    		graphics.setFont(new Font("SansSerif", Font.ITALIC, 12));
	    		
	    		for(var elem : actions) {
	    			
	    		
	    			graphics.drawString(elem, ((5 * x) / 100) + ((1 * x) / 100), ((5 * y) / 100) + (((5 * y) / 100) * y_offset));
	    			
	    			graphics.setColor(Color.PINK);
		    		graphics.setFont(new Font("SansSerif",Font.ITALIC , 10));
	    			
	    			y_offset ++;
	    		}
	    		
	    		
	    		
	    		
	    	});
	    	
	    	
	    }
	    
	    
	    private void drawCardNoble(ApplicationContext context, float a, float b, int largeur, int hauteur, Tuile card) { // À changer avec l'interface Carte pour aussi faire les nobles
			
		    
		    
		     context.renderFrame(graphics -> {
		    	 
		    	 
		    	 Rectangle2D.Float rectangle = new Rectangle2D.Float(0, 0, 0, 0);
		    	 
		    	 

		        
		    	 // show a new ellipse at the position of the pointer
		    	 
		    	 graphics.setColor(Color.BLACK);
		    	 
		    	 rectangle = new Rectangle2D.Float(a - 1, b - 1 , largeur + 2 , hauteur + 2);
		    	 graphics.fill(rectangle);
		    	 
		    	 graphics.setColor(Color.PINK);
	
		    	 rectangle = new Rectangle2D.Float(a, b , largeur , hauteur);

		    	 graphics.fill(rectangle);
		        
		        
		    	 graphics.setColor(Color.BLACK);
		    	 rectangle = new Rectangle2D.Float(a + ((5 * largeur) / 100), b + ((2 * hauteur) / 100), 15, 15);
		    	 graphics.fill(rectangle);
		    	 
		        
		    	 /*PRICE*/
		        
		    	 var nb_carte = 0;
		    	 
		    	 for(var price : card.cout().entrySet()) {
		    		 drawGem(context, a + ((5 * largeur) / 100), b + ((45 * hauteur) / 100) + (nb_carte * 20), 15, price.getValue() , price.getKey());
		    		 
		    		 nb_carte ++;
		    	 }
		        
		       	 graphics.setColor(Color.WHITE);
		       	 
		    	 graphics.setFont(new Font("SansSerif", Font.BOLD, 10));
		    	 graphics.drawString(Integer.toString(card.points_prestiges()), a + ((5 * largeur) / 100)  + (15 / 2) - (15 / 6) - (15 / 8), b + ((2 * hauteur) / 100) + (15 / 2) + (15 / 6) + (15 / 6)); //15 = taille du carré
		    	 
		    
		    	
		    	 int fontsize = fontSize(card.name(), largeur);
			     graphics.setFont(new Font("SansSerif", Font.BOLD, fontsize));

			     graphics.drawString(card.name(), a + ((largeur / 2) - largeur / 4) + 1, b + ((30 * hauteur) / 100));
		        
		     });
		}  
	    
	    
	    private void drawCard(ApplicationContext context, float a, float b, int largeur, int hauteur, CarteDev card) { // À changer avec l'interface Carte pour aussi faire les nobles
			
		    
		    
		     context.renderFrame(graphics -> {
		    	 
		    	 
		    	 Rectangle2D.Float rectangle = new Rectangle2D.Float(0, 0, 0, 0);
		    	 
		    	 

		        
		    	 // show a new ellipse at the position of the pointer
		    	 
		    	 graphics.setColor(Color.BLACK);
		    	 
		    	 rectangle = new Rectangle2D.Float(a - 1, b - 1 , largeur + 2 , hauteur + 2);
		    	 graphics.fill(rectangle);
		    	 
		    	 graphics.setColor(Color.WHITE);
	
		    	 rectangle = new Rectangle2D.Float(a, b , largeur , hauteur);

		    	 graphics.fill(rectangle);
		        
		        
		    	 graphics.setColor(Color.BLACK);
		    	 rectangle = new Rectangle2D.Float(a + ((5 * largeur) / 100), b + ((2 * hauteur) / 100), 15, 15);
		    	 graphics.fill(rectangle);
		    	 
		        
		    	 /*PRICE*/
		    	 
		    	 var nb_carte = 0;
		    	 for(var price : card.coût().entrySet()) {
		    		 drawGem(context, a + ((5 * largeur) / 100), b + ((45 * hauteur) / 100) + (nb_carte * 20), 15, price.getValue() , price.getKey());
		    		 
		    		 nb_carte ++;
		    	 }
		        
		    	  
		       
		    	 //drawGem(context, a + ((5 * largeur) / 100), b + ((45 * hauteur) / 100) + 20 , 15, 7,  "Rouge");	/*20 = decalage entre les prix*/
		        
		       	 graphics.setColor(Color.WHITE);
		       	 
		    	 graphics.setFont(new Font("SansSerif", Font.BOLD, 10));
		    	 graphics.drawString(Integer.toString(card.points()), a + ((5 * largeur) / 100)  + (15 / 2) - (15 / 6) - (15 / 8), b + ((2 * hauteur) / 100) + (15 / 2) + (15 / 6) + (15 / 6)); //15 = taille du carré
		    	 
		    	 /*BONUS*/
		    	drawGem(context, a + (largeur - ((10 * largeur) / 100) - (13 / 2)), b + ((2 * hauteur) / 100), 13, -1 ,card.couleur()); // 13 = taille
		    	
		    	graphics.setColor(Color.BLACK);
		    	
		    	
		    	int fontsize = fontSize(card.object(), largeur);
		    	graphics.setFont(new Font("SansSerif", Font.BOLD, fontsize));

		    	graphics.drawString(card.object(), a + ((largeur / 2) - largeur / 4) + 1, b + ((30 * hauteur) / 100));
		        
		     });
		}  
	    
	    
	    
	    private static int fontSize(String message, int taille) {
	    	
	    	int font = taille / 10;
	    	
	    	while ((message.length() * font) > (taille + ((10 * taille) / 100))) {
	    		font--;
	    	}
	   
	    	
	    	return font;
	    }
	    
	    
	    private void drawCardPioche(ApplicationContext context, float a, float b, int largeur, int hauteur, String val) { // À changer avec l'interface Carte pour aussi faire les nobles
			 
		     context.renderFrame(graphics -> {
		    	 
		    	 
		    	 Rectangle2D.Float rectangle = new Rectangle2D.Float(0, 0, 0, 0);
		    	 
		    	 

		        
		    	 // show a new ellipse at the position of the pointer
		    	 
		    	 graphics.setColor(Color.BLACK);
		    	 
		    	 rectangle = new Rectangle2D.Float(a - 1, b - 1 , largeur + 2 , hauteur + 2);
		    	 graphics.fill(rectangle);
		    	 
		    	 graphics.setColor(Color.CYAN);
	
		    	 rectangle = new Rectangle2D.Float(a, b , largeur , hauteur);

		    	 graphics.fill(rectangle);
		        
		        
		        
		       	 graphics.setColor(Color.BLACK);
		       	 
		    	 graphics.setFont(new Font("SansSerif", Font.BOLD, 10));
		    	 graphics.drawString(val, a + ((50 * largeur) / 100)  - 2, b + ((50 * hauteur) / 100)); //15 = taille du carré
		    	 
		    	
		        
		     });
		}
	    
	    
	}
	
	
	private static void drawGem(ApplicationContext context, float a, float b, int taille, int val, String color){
		
		context.renderFrame(graphics -> {
			Ellipse2D.Float cercle1 = new Ellipse2D.Float(a - 1, b - 1, taille + 2, taille + 2);
	    	Ellipse2D.Float cercle2 = new Ellipse2D.Float(a, b, taille, taille);
	    
	        
	    	graphics.setColor(Color.BLACK);
	    	graphics.fill(cercle1);
	    	// show a new ellipse at the position of the pointer
	    	graphics.setColor(Color.WHITE);

	    	if(color.equals("Rouge")) {
	    		graphics.setColor(Color.RED);
	    	}
	    	 
	    	if(color.equals("Bleu")) {
	    		graphics.setColor(Color.BLUE);
	    	 }
	    	 
	    	 if(color.equals("Blanc")) {
	    		 graphics.setColor(Color.WHITE);
	    	 }
	    	 
	    	 if(color.equals("Vert")) {
	    		 graphics.setColor(Color.GREEN);
	    	 }
	    	 
	    	 if(color.equals("Noir")) {
	    		 graphics.setColor(Color.BLACK);
	    	 }
	    	 
	    	 if(color.equals("Jaune")) {
	    		 graphics.setColor(Color.YELLOW);
	    	 }
	    	 
	    	 graphics.fill(cercle2);		
	    	 
	    	 if(color.equals("Jaune") || color.equals("Blanc")) {
	    		 graphics.setColor(Color.BLACK);
	    	 }else {
	    		 graphics.setColor(Color.WHITE);
	    	 }
	    	 
	    	 graphics.setFont(new Font("SansSerif", Font.BOLD, taille / 2));
	    	 
	    	 String chaine;
	    	 
	    	 if(val <= 0){
	    		 chaine = "";
	    	 }else {
	    		 chaine = Integer.toString(val);
	    	 }
	    	 
	    	 graphics.drawString(chaine, a + (taille / 2) - (taille / 6), b + (taille / 2) + (taille / 4));

	      });
	}
	
	public static void main(String[] args) {
		
		var affichage = new AffichageGraphique();
		
		Application.run(Color.ORANGE, context -> {
			
			affichage.context = context;
			affichage.listActions = new ArrayList<String>();
			
			Partie.startGame(affichage);
			
			return;
			
		});
	}
}

