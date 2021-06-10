package compileMD;

import java.io.File;
import java.nio.file.Path;
import java.util.Scanner;
import gestionFichier.WorkFolder;



/**
 * Classe de lancement du projet
 * @author nicolas
 */
public class Start {

	/**
	 * Méthode de démarage de l'application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scanner keyb = new Scanner(System.in);
		System.out.println("Chemin du répertoire ?");
		
		String chemin = keyb.nextLine();
		keyb.close();
		
		
		System.out.println("Traitement du répertoire : '" + chemin + "'");
		
		try {
			
			WorkFolder.initialise(chemin);
			System.out.println("Nom du répertoire : '" + WorkFolder.getFolder().getNomDuProjetDefaut() + "'");
			
			WorkFolder repertoireDeTravail = WorkFolder.getFolder();
			
			// Déclaration du dossier old
			Path oldRepertoire = repertoireDeTravail.getFolder().getRepertoireOld();
			int nbFichiersOld = 0;
			int newNbFichiersOld = 0;			
			
			// Compte le nombre de fichiers dans le dossier old au démarrage de l'application			
			if (oldRepertoire != null) {
				File[] f = oldRepertoire.toFile().listFiles();
				for (int i = 0; i < f.length; i++) {
					if (f[i].isFile()) {
						nbFichiersOld++;
					}
				}
			}			
			// Affiche le nombre de fichiers dans le dossier old au démarrage de l'application			
			System.out.println("Le dossier \"Old\" contient déjà " + nbFichiersOld + " fichier(s)");
			
			repertoireDeTravail.getFichiers().forEach(x -> x.action());
			
			// Compte à nouveau le nombre de fichiers dans le répertoire old
			if (oldRepertoire != null) {
				File[] f = oldRepertoire.toFile().listFiles();
				for (int i = 0; i < f.length; i++) {
					if (f[i].isFile()) {
						newNbFichiersOld++;
					}
				}
			}
			// Affiche le nombre de fichiers déplacés dans le dossier old
			int nbFichiersDeplace = newNbFichiersOld - nbFichiersOld;
			System.out.println(nbFichiersDeplace + " fichier(s) ont été déplacés dans le dossier \"Old\".");
			if (nbFichiersDeplace > 0) {
				System.out.println("Le dossier \"Old\" contient maintenant " + newNbFichiersOld + " fichier(s)");
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
