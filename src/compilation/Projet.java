/**
 * Classe m�tier permettant de compiler un fichier compilable
 */
package compilation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import gestionFichier.BaseFile;
import gestionFichier.TextFile;
import gestionFichier.WorkFolder;
import outil.OutilDate;
import texteFichier.ICompilableFile;

/**
 * Repr�sente un projet
 * Classe finale, non-h�ritable
 * @author Ludwig 
 */
public final class Projet {
	
	/**
	 * Liste statique des diff�rents projets
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
	 * M�thode qui permet d'ajouter un fichier � la compilation
	 */
	public static void ajouterACompilation(BaseFile fichierX) {
		
		// Initialise la liste des projets si n'�xiste pas encore
		if (Projet.projets == null) {
			Projet.projets = new ArrayList<Projet>();
		}
		
		// Test si le fichierX en argument de la m�thode impl�mente l'interface ICompilable
		if (fichierX instanceof ICompilableFile) {
			
			ICompilableFile fichierAAjouter = (ICompilableFile)fichierX; // fichierX est compilable
				
			Projet projetDuFichier = Projet.projets.stream() // Passe la liste des projets en Stream
					.filter(p -> p.nom.equals(fichierAAjouter.getNomDuProjet())) // Boucle le stream et cherche le nom du projet 
					.findFirst() // Retourne le premier projet correspondant
					.orElse(null); // Retourne null si ne trouve pas le projet dans la liste
			
			// Test si le projet �xiste
			if (projetDuFichier == null) {
				projetDuFichier = new Projet(fichierAAjouter.getNomDuProjet()); // Initialise un nouveau projet
				Projet.projets.add(projetDuFichier); // Ajoute le projet � la liste des projets
				System.out.println("--> Cr�ation du projet : " + projetDuFichier.nom);
			}
			
			// Ajoute le fichierX � la liste des fichiers du projet
			projetDuFichier.fichiers.add(fichierAAjouter);
			
			System.out.println("----> Un fichier a �t� ajout� au projet : " + projetDuFichier.nom +
					" Nombre total de fichiers du projet : " + projetDuFichier.fichiers.size());
		}
	}
	
	public static void compiler() {
		
		// Variable correspondant au retour chariot pour la mise en page du fichier texte compil�
		String retourCharriot = System.getProperty("line.separator");
		
		// V�rifie si la liste des projets �xiste
		if (projets != null) {
			
			// Pour chaque projet
			for (Projet projet : projets) {
				
				String titreLivre = projet.getNom().replace('_', ' '); // Titre du livre
				int numChapitre = 0; // Instanciation du num�ro de chapitre
				boolean firstFichier = false;
				
				try {					
					
					// Chemin du projet fictif en sp�cifiant le suffixe du fichier
					Path newProjet = (new File(WorkFolder.getFolder().getRepertoire().toFile(), projet.getNom() + "_compile.txt")).toPath(); 
					
					// Si le fichier du projet n'�xiste pas
					if(!Files.exists(newProjet)) {
						Files.createFile(newProjet); // Cr�� le fichier correspondant au projet			
						// Ecriture du titre du livre dans le fichier texte compil�
						Files.writeString(newProjet, titreLivre + retourCharriot + retourCharriot, StandardOpenOption.APPEND);
						firstFichier = true;
					}	
					
					projet.ecriturelignes(projet, newProjet, numChapitre, firstFichier);
										
					// Instancie le nouveau fichier compil� en fichier Texte via la fabrique de BaseFile
					TextFile newLivre = (TextFile)BaseFile.Fabrik(newProjet);
					
					int[] compilation = projet.getStatistiques(titreLivre, newLivre);
					
					projet.sauvegardeLivre(titreLivre, compilation[0], compilation[1]);
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
		} else {
			System.out.println("Impossible de compiler, aucun projet trouv�."); // Erreur
		}		
	}
	
	/*
	 * M�thode pour ins�rer les fichiers d'un projet dans un livre
	 * Gestion des chapitres et des s�parateurs
	 */
	public void ecriturelignes(Projet projet, Path newProjet, int numChapitre, boolean firstFichier) {
		String retourCharriot = System.getProperty("line.separator");		
		try {
			// Pour chaque fichiers du projet
			for (ICompilableFile fichier : projet.fichiers) {
				// Si fichier repr�sente un nouveau chapitre
				if (fichier.getChemin().toString().endsWith("-.md")) {	
					// Ecriture du num�ro de chapitre
					numChapitre++;
					Files.writeString(newProjet,retourCharriot + "Chapitre " + numChapitre + retourCharriot + retourCharriot, StandardOpenOption.APPEND);							
				}				
				else {
					if (!firstFichier) {
						// Ecriture du s�parateur entre fichiers intra chapitre
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
	 * Permet de r�cup�rer les statistiques d'un projet
	 */
	public int[] getStatistiques(String titreLivre, TextFile livre) {
		int nbMots = livre.getNbrMots();
		int nbCaracteres = livre.getNbrCaracteres();
		int compileMots = 0;
		int compileCaracteres = 0;
		System.out.println("Le livre " + titreLivre + " contient " + nbMots + " mots.");
		
		int idCompile = DBCompilation.getSingleton().getIdCompile(titreLivre); 
		if (idCompile == 0) {
			System.out.println("Nouveau projet, premi�re compilation de " + nbMots + " mots et " + nbCaracteres + " caract�res.");
		}
		else {
			compileMots = nbMots - (DBCompilation.getSingleton().getNbMotsLivre(idCompile));
			compileCaracteres = nbCaracteres - (DBCompilation.getSingleton().getNbCaracteresLivre(idCompile));
			System.out.println("Compilation de " + compileMots + " mots et " + compileCaracteres + " caract�res.");
		}
		
		return new int[] {nbMots, nbCaracteres};
	}
	
	/*
	 * Permet de sauvegarder la compilation d'un projet dans la base de donn�es
	 */
	public void sauvegardeLivre(String titreLivre, int nbMotsLivre, int nbCaracteresLivre) {
		int idCompile = DBCompilation.getSingleton().getIdCompile(titreLivre);
		String today = OutilDate.getTodayDate();
		String dteLastCompile = DBCompilation.getSingleton().getDateCompile(idCompile);
		
		// Si nouveau livre
		if (idCompile == 0) {
			DBCompilation.getSingleton().insertNewCompile(titreLivre, nbMotsLivre, nbCaracteresLivre);
		}
		else {
			// Si compilation du livre en date d'aujourd'hui
			if (dteLastCompile.equals(today)) {
				DBCompilation.getSingleton().updateCompile(idCompile, nbMotsLivre, nbCaracteresLivre);
			}
			else {				
				DBCompilation.getSingleton().insertNewCompile(titreLivre, nbMotsLivre, nbCaracteresLivre);
			}
		}
	}
}



