/**
 * Classe métier permettant de compiler un fichier compilable
 */
package compilation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gestionFichier.BaseFile;
import gestionFichier.TextFile;
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
	private Projet(String nomProjet)  {
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
		
		// Variable correspondant au retour chariot pour la mise en page du fichier texte compilé
		String retourCharriot = System.getProperty("line.separator");
		
		// Vérifie si la liste des projets éxiste
		if (projets != null) {
			
			// Pour chaque projet
			for (Projet projet : projets) {
				
				String titreLivre = projet.getNom().replace('_', ' '); // Titre du livre
				int numChapitre = 0; // Instanciation du numéro de chapitre		
				boolean firstFichier = false;
				try {					
					
					// Chemin du projet fictif en spécifiant le suffixe du fichier
					Path newProjet = (new File(WorkFolder.getFolder().getRepertoire().toFile(), projet.getNom() + "_compile.txt")).toPath(); 
					
					// Si le fichier du projet n'éxiste pas
					if(!Files.exists(newProjet)) {
						Files.createFile(newProjet); // Créé le fichier correspondant au projet			
						// Ecriture du titre du livre dans le fichier texte compilé
						Files.writeString(newProjet, titreLivre + retourCharriot + retourCharriot, StandardOpenOption.APPEND);
						firstFichier = true;
					}	
					
					projet.ecriturelignes(projet, newProjet, numChapitre, firstFichier);
										
					// Instancie le nouveau fichier compilé en fichier Texte via la fabrique de BaseFile
					TextFile newLivre = (TextFile)BaseFile.Fabrik(newProjet);
					
					int[] stats = projet.getStatistiques(titreLivre, newLivre);
					
					projet.sauvegardeLivre(titreLivre, stats[0], stats[1]);
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
		} else {
			System.out.println("Impossible de compiler, aucun projet trouvé."); // Erreur
		}		
	}
	
	/*
	 * Méthode pour insérer les fichiers d'un projet dans un livre
	 * Gestion des chapitres et des séparateurs
	 */
	public void ecriturelignes(Projet projet, Path newProjet, int numChapitre, boolean firstFichier) {
		String retourCharriot = System.getProperty("line.separator");
		
		try {
			// Pour chaque fichiers du projet
			for (ICompilableFile fichier : projet.fichiers) {						
				
				// Si fichier représente un nouveau chapitre
				if (fichier.getChemin().toString().endsWith("-.md")) {	
					// Ecriture du numéro de chapitre
					numChapitre++;
					Files.writeString(newProjet,retourCharriot + "Chapitre " + numChapitre + retourCharriot + retourCharriot, StandardOpenOption.APPEND);							
				}
				
				else {
					if (!firstFichier) {
						// Ecriture du séparateur entre fichiers intra chapitre
						Files.writeString(newProjet, retourCharriot + "***" + retourCharriot + retourCharriot, StandardOpenOption.APPEND);
					}
				}							
				
				List<String> lignes = Files.readAllLines(fichier.getChemin()); // Toutes les lignes d'un des fichier
				
				// Pour chaque lignes du fichier
				for (String ligne : lignes) {
					
					// Ecrit dans le fichier la ligne en cours
					Files.writeString(newProjet, ligne + retourCharriot, StandardOpenOption.APPEND); 	
				}
				
				firstFichier = false; 
			}		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Permet de récupérer les statistiques d'un projet
	 */
	public int[] getStatistiques(String titreLivre, TextFile livre) {		
		int nbMots = livre.getNbrMots();
		int lastCompile = 0;
		int newCompile = 0;
		System.out.println("Le livre " + titreLivre + " contient " + nbMots + " mots.");
		
		int idCompile = DBCompilation.getSingleton().getIdCompile(titreLivre); 
		if (idCompile == 0) {
			System.out.println("Nouveau projet, première compilation de " + nbMots + " mots.");
		}
		else {
			lastCompile = DBCompilation.getSingleton().getNbMotsCompile(idCompile);
			newCompile = nbMots - (DBCompilation.getSingleton().getNbMotsLivre(idCompile));
			System.out.println("Dernière compilation de " + lastCompile + " mots. Nouvelle compilation de " + newCompile + " mots.");
		}
		
		return new int[] {nbMots, newCompile};
	}
	
	/*
	 * Permet de sauvegarder la compilation d'un projet dans la base de données
	 */
	public void sauvegardeLivre(String titreLivre, int nbMotsLivre, int nbMotsCompile) {
		int idCompile = DBCompilation.getSingleton().getIdCompile(titreLivre);
		
		Date date = new Date(); // Date du jour
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Format de la date
		String today = dateFormat.format(date);
		String dteLastCompile = DBCompilation.getSingleton().getDateCompile(idCompile);
		
		// Si nouveau livre
		if (idCompile == 0) {
			DBCompilation.getSingleton().insertNewCompile(titreLivre, nbMotsLivre, nbMotsLivre);
		}
		else {
			// Si compilation du livre en date d'aujourd'hui
			if (dteLastCompile.equals(today)) {
				DBCompilation.getSingleton().updateCompile(idCompile, nbMotsLivre, nbMotsCompile);
			}
			else {				
				DBCompilation.getSingleton().insertNewCompile(titreLivre, nbMotsLivre, nbMotsCompile);
			}
		}
	}
}



