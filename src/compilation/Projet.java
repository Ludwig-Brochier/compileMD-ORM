/**
 * Classe métier permettant de compiler un fichier compilable
 */
package compilation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import gestionFichier.BaseFile;
import gestionFichier.WorkFolder;
import texteFichier.ICompilableFile;

/**
 * Représente un projet
 * Classe finale, non-héritable
 * @author Ludwig 
 */
public final class Projet {
	
	/**
	 * Liste statique des différents projets
	 */
	private static ArrayList<Projet> projets;
	public static ArrayList<Projet> getProjets(){
		return projets;
	}
		
	
	/**
	 * Nom du projet
	 */
	private String nom;
	public String getNom() {
		return nom;
	}
	
	/**
	 * Liste des fichiers composant le projet
	 */
	private ArrayList<ICompilableFile> fichiers;
	
	
	/**
	 * Constructeur de la classe Projet
	 * @param nomProjet
	 */
	private Projet(String nomProjet) {
		nom = nomProjet;
		fichiers = new ArrayList<ICompilableFile>();
	}
	
	/**
	 * Méthode qui permet d'ajouter un fichier à la compilation
	 */
	public static void ajouterACompilation(BaseFile fichierX) {
		
		// Initialise la liste des projets si n'éxiste pas encore
		if (Projet.projets == null) {
			Projet.projets = new ArrayList<Projet>();
		}
		
		// Test si le fichierX en argument de la méthode implémente l'interface ICompilable
		if (fichierX instanceof ICompilableFile) {
			
			ICompilableFile fichierAAjouter = (ICompilableFile)fichierX; // fichierX est compilable
				
			Projet projetDuFichier = Projet.projets.stream() // Passe la liste des projets en Stream
					.filter(p -> p.nom.equals(fichierAAjouter.getNomDuProjet())) // Boucle le stream et cherche le nom du projet 
					.findFirst() // Retourne le premier projet correspondant
					.orElse(null); // Retourne null si ne trouve pas le projet dans la liste
			
			// Test si le projet éxiste
			if (projetDuFichier == null) {
				projetDuFichier = new Projet(fichierAAjouter.getNomDuProjet()); // Initialise un nouveau projet
				Projet.projets.add(projetDuFichier); // Ajoute le projet à la liste des projets
				System.out.println("--> Création du projet : " + projetDuFichier.nom);
			}
			
			// Ajoute le fichierX à la liste des fichiers du projet
			projetDuFichier.fichiers.add(fichierAAjouter);
			
			System.out.println("----> Un fichier a été ajouté au projet : " + projetDuFichier.nom +
					" Nombre total de fichiers du projet : " + projetDuFichier.fichiers.size());
		}
	}
	
	public static void compiler() {
		
		// Vérifie si la liste des projets éxiste
		if (projets != null) {
			
			// Pour chaque projet
			for (Projet projet : projets) {
				
				try {					
					// Chemin du projet fictif en spécifiant le suffixe du fichier
					Path newProjet = (new File(WorkFolder.getFolder().getRepertoire().toFile(), projet.getNom() + ".md")).toPath(); 
					
					// Si le fichier du projet n'éxiste pas
					if(!Files.exists(newProjet)) {
						Files.createFile(newProjet); // Créé le fichier correspondant au projet						
					}
					
					// Pour chaque fichiers du projet
					for (ICompilableFile fichier : projet.fichiers) {
						
						List<String> lignes = Files.readAllLines(fichier.getChemin()); // Toutes les lignes d'un des fichier
						
						// Pour chaque Lignes du fichier
						for (String ligne : lignes) {
							
							// Ecrit dans le fichier la ligne en cours, en gardant les sauts de ligne initials
							Files.writeString(newProjet, ligne + System.getProperty("line.separator"), StandardOpenOption.APPEND); 							
						}
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
			}
			
		} else {
			System.out.println("Impossible de compiler, aucun projet trouvé."); // Erreur
		}
	}
}



