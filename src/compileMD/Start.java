package compileMD;

import java.util.Scanner;

import compilation.Projet;
import gestionFichier.WorkFolder;



/**
 * Classe de lancement du projet
 * @author Ludwig
 */
public class Start {

	/**
	 * Méthode de démarage de l'application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Méthode Scanner pour récupérer une saisie utilisateur
		Scanner keyb = new Scanner(System.in);
		System.out.println("Chemin du répertoire ?");
		
		// Saisi de l'utilisateur du répertoire de travail
		String chemin = keyb.nextLine();
		keyb.close(); // Ferme le Scanner
		
		
		System.out.println("Traitement du répertoire : '" + chemin + "'");
		
		try {
			
			WorkFolder.initialise(chemin); // Initialise le répertoire de travail
			System.out.println("Nom du répertoire : '" + WorkFolder.getFolder().getNomDuProjetDefaut() + "'");
			
			WorkFolder repertoireDeTravail = WorkFolder.getFolder(); // Récupère le Singleton du répertoire de travail
			
			// Etape Triage fichiers
			repertoireDeTravail.getFichiers().forEach(x -> x.action()); // Appel de la méthode action pour chaque fichier présent dans le répertoire de travail			
			// Affiche le nombre de fichier déplacé dans le répertoire old
			System.out.println("--> " + WorkFolder.getFolder().getNbFichierDeplace() + " fichier(s) ont été déplacé.");
			
			// Etape Compilation
			repertoireDeTravail.getFichiers().forEach(x -> Projet.ajouterACompilation(x)); // Ajoute à la compilation tout fichiers compilable présent dans le répertoire de travail			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
