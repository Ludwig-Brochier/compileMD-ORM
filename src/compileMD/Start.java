package compileMD;

import java.util.Scanner;
import compilation.DBCompilation;
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
		// Base de données		
		DBCompilation.getSingleton();
		
		// Méthode Scanner pour récupérer une saisie utilisateur
		Scanner keyb = new Scanner(System.in);
		System.out.println("Chemin du répertoire ?");
		
		// Saisi de l'utilisateur du répertoire de travail
		String chemin = keyb.nextLine();
		keyb.close(); // Ferme le Scanner
		
		if (chemin.isEmpty()) {
			System.out.println("Récupère le chemin actif dans la base de données");
			chemin = DBCompilation.getSingleton().getPathActif();
		} else {
			if (DBCompilation.getSingleton().getIdPath(chemin) > 0) {
				System.out.println("Mettre à jour la bdd");
				DBCompilation.getSingleton().updatePath(chemin);
			} else {
				System.out.println("Ajout du chemin dans la bdd");
				DBCompilation.getSingleton().insertNewPath(chemin);
			}
		}
		
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
			//Projet.getProjets().forEach(p -> System.out.println(p.getNom()));
			System.out.println("Lancement de la compilation -->");
			Projet.compiler();
			System.out.println("Compilation terminée. " + Projet.getProjets().size() + " projets compilés.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
