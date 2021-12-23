
/**
 * Interface représentant la manière d'affichage. On y trouve
 * les affciahges en ligne de commande ainsi que l'affichage graphique 
 * 
 * @author dylandejesus
 */
public interface Affichage {

	public void showPlateau(Mode game, int mode);
	public void showBoard(Mode game);
	public void showReserved(Joueur joueur);
	public int showJoueur(Joueur joueur);
	
}
