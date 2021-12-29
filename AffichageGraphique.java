package fr.umlv.affichage;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Objects;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.ScreenInfo;
import fr.umlv.zen5.Event.Action;

import fr.umlv.game.mode.*;
import fr.umlv.players.*;

public class AffichageGraphique implements Affichage{
	
	
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
		
		Objects.requireNonNull(game);
		
		if(mode != 1) {
			showTuiles(game);
		}
		
		showBoard(game);
		showJeton(game.jetons_disponibles(), "JETON");
		
		System.out.println("\n\n========================================== JOUEURS ==========================================\n\n");
		
		for(var joueur : game.joueurs()) {
			showJoueur(joueur);
			System.out.println("\n");
		}	
	}
	
	
	
	
	/**
	 * 
	 * Print the available tokens on the console. It i représented as a board.
	 * 
	 * @param ressources
	 *        Resources of tokens
	 */
	public static void showJeton(HashMap<String, Integer> ressources, String message) {
		
		StringBuilder chaine_tab = new StringBuilder();
		StringBuilder chaine_noms = new StringBuilder();
		StringBuilder chaine_quantite = new StringBuilder();
		String separator = "|    ";
		String separator_quantite = "|      ";
		
		if(message != null) {
			System.out.println("\n\n========================================== "+ message + " ==========================================\n\n");
		}
		
		for(var entry : ressources.entrySet()) {
			
			var jeton_name = entry.getKey();
			
			if(jeton_name.equals("Vert") || jeton_name.equals("Noir") || jeton_name.equals("Bleu")) {
				jeton_name = jeton_name + " ";
			}
			var jeton_quantite = entry.getValue();
			
		
			 chaine_noms.append(separator).append(jeton_name);
			 chaine_quantite.append(separator_quantite).append(jeton_quantite);
			 
			 separator = "    |    ";
			 separator_quantite = "      |      ";
		}
		
		chaine_noms.append("    |");
		chaine_quantite.append("      |");
		for(var i = 0; i< ressources.size();i++) {
			chaine_tab.append(" ―――――――――――――");
		}
		System.out.println(chaine_tab);
		System.out.println(chaine_noms);
		System.out.println(chaine_tab);
		System.out.println(chaine_quantite);
		System.out.println(chaine_tab);
	}

	
	/**
	 * Print the cards of the board of the game token.
	 * 
	 * @param game
	 *        Game token to print it board
	 */
	public void showBoard(Mode game) {
		
		Objects.requireNonNull(game);
		
		for(int i = 1 ; i < game.board().size() + 1 ; i++) {
			
			var num_card = 1;
			
			if(game.board().get(i).get(0) != null) {
				
				
				
				for(var carte : game.board().get(i)) {
					
					
					
					if(carte != null)
						System.out.println(carte);
					num_card ++;	
				}
			}
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
		showJeton(joueur.ressources(), "JETON");
		showJeton(joueur.bonus(), "BONUS");
		showReserved(joueur);
		
		return 1;
	}
	
	/**
	 * Print the Nobles Cards on the board.
	 * 
	 * @param game
	 *        Game given to show its nobles card on the board
	 */
	public static void showTuiles(Mode game){
		
		System.out.println("    -- NOBLES --   \n\n");
		
		for(int i = 0 ; i < game.tuiles_board().size(); i++) {
			
			System.out.println(game.tuiles_board().get(i));
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
	    
	    
	    private void drawCard(ApplicationContext context, float a, float b) {
			
			//Rectangle2D.Float rectangle = new Rectangle2D.Float(0, 0, 0, 0);
		    
		    
		     context.renderFrame(graphics -> {
		    	 
		    	 Rectangle2D.Float rectangle = new Rectangle2D.Float(0, 0, 0, 0);
		    	 
		    	 
		    	 
		        // hide the previous rectangle
		        graphics.setColor(Color.ORANGE);
		        graphics.fill(rectangle);
		        
		        // show a new ellipse at the position of the pointer
		        graphics.setColor(Color.WHITE);
		        
		        
		        rectangle = new Rectangle2D.Float(a, b , 215 , 300);

		        graphics.fill(rectangle);
		        
		        
		        graphics.setColor(Color.BLACK);
		    	 rectangle = new Rectangle2D.Float(a + 15, b + 15, 20, 20);
		    	 graphics.fill(rectangle);
		    	 
		        
		        
		        
		       drawGem(context, a + 20, b + 200 , "Bleu"); 
		       
		       drawGem(context, a + 20, b + 235, "Rouge");
		        
		       	 graphics.setColor(Color.WHITE);
		    	 graphics.setFont(new Font("SansSerif", Font.BOLD, 14));
		    	 graphics.drawString("4", a + 20, b + 30);
		    	 
		    	drawGem(context, a + (200 - 30) ,b + 15,"Bleu");
		    	
		    	graphics.setColor(Color.BLACK);
		    	graphics.setFont(new Font("SansSerif", Font.BOLD, 14));
		    	 graphics.drawString("Venise", a + 75, b + 125);
		        
		      });
		}  
	}
	
	
	private static void drawGem(ApplicationContext context, float a, float b, String color){
		
		context.renderFrame(graphics -> {
			
	    	 Ellipse2D.Float cercle = new Ellipse2D.Float(a, b, 25, 25);
	    
	        
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
	    	 
	    	 graphics.fill(cercle);		
	    	 
	    	 graphics.setColor(Color.WHITE);
	    	 graphics.setFont(new Font("SansSerif", Font.BOLD, 14));
	    	 graphics.drawString("4", a + 8, b + 18);

	      });
	}
	
	public static void main(String[] args) {
		
		
		
		Application.run(Color.ORANGE, context -> {
		      
			
			
				
			
			
		      // get the size of the screen
		      ScreenInfo screenInfo = context.getScreenInfo();
		      float width = screenInfo.getWidth();
		      float height = screenInfo.getHeight();
		      System.out.println("size of the screen (" + width + " x " + height + ")");
		      
		      /**/
		      context.renderFrame(graphics -> {
		        graphics.setColor(Color.ORANGE);
		        graphics.fill(new  Rectangle2D.Float(0, 0, width, height));
		      });
		      
		      
		      Area area2 = new Area();
				
				
		      area2.drawCard(context, 50, 50);
		      
		      Area area = new Area();
		      
		      for(;;) {
		        Event event = context.pollOrWaitEvent(10);
		        if (event == null) {  // no event
		          continue;
		        }
		        Action action = event.getAction();
		        if (action == Action.KEY_PRESSED || action == Action.KEY_RELEASED) {
		          System.out.println("abort abort !");
		          context.exit(0);
		          return;
		        }
		        System.out.println(event);
		        
		        Point2D.Float location = event.getLocation();
		        area.drawCard(context, location.x, location.y);
		      }
		    });
	}
}

