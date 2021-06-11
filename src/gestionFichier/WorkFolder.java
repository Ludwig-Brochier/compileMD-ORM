package gestionFichier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe métier pour le répertoire de travail.
 * @author Ludwig
 */
public class WorkFolder {
	
	/**
	 * Singleton qui permet de toujours travailler sur le même répertoire de travail.
	 */
	private static WorkFolder folder;
	public static WorkFolder getFolder() throws Exception {
		
		if(WorkFolder.folder == null) {
			throw new Exception("Le répertoire de travail n'a pas été initialisé.");
		}
		
		return folder;
	}
	
	/**
	 * Liste des fichiers qui se trouvent dans le répertoire.
	 */
	private ArrayList<BaseFile> fichiers ;
	public ArrayList<BaseFile> getFichiers() {
		return fichiers;
	}
	
	/**
	 * Nom du projet par défaut correspondant au répertoire.
	 */
	private String nomDuProjetDefaut;
	public String getNomDuProjetDefaut() {
		return nomDuProjetDefaut;
	}
	
	/**
	 * Path du répertoire de travail.
	 */
	private Path repertoire = null;
	public Path getRepertoire() {
		return repertoire;
	}
	
	/**
	 * Path du répertoire old.
	 */
	private Path repertoireOld;
	public Path getRepertoireOld() {
		return repertoireOld;
	}
	
	/**
	 * Nombre de fichier présent dans le répertoire old
	 */
	private int nbFichierRepertoireOld;
	public int getNbFichierRepertoireOld() {
		return nbFichierRepertoireOld;
	}
	
	/**
	 * Nombre de fichier déplacé
	 */
	private int nbFichierDeplace;
	public int getNbFichierDeplace() {
		return nbFichierDeplace;
	}
	public int incrementNbFichierDeplace() {
		return nbFichierDeplace++;
	}
	
	
	/**
	 * Constructeur de la classe qui initialise la liste des fichiers.
	 * @param cheminDuRepertoire
	 */
	private WorkFolder(String cheminDuRepertoire) {
		
		// Initialise la liste des fichiers BaseFile présents dans le répertoire de travail
		fichiers = new ArrayList<BaseFile>();
		
    	try {
    		
			Files.walk(Paths.get(cheminDuRepertoire)) // Parcours récursivement les fichiers et dossiers du répertoire de travail
				.filter(Files::isRegularFile) // Filtre les fichiers des dossiers
				.forEach(x -> fichiers.add(BaseFile.Fabrik(x))); // Ajoute chaque fichier à la liste en utilisant la fabrique dans BaseFile
			Collections.sort(fichiers); // Tri le nom des fichiers de manière croissante
			
			this.repertoire = Paths.get(cheminDuRepertoire); // Initialise le chemin du répertoire de travail
			nomDuProjetDefaut = this.getRepertoire().getFileName().toString(); // Initialise le nom du projet par défault avec le nom du répertoire de travail
			
			
			File oldRep = new File(this.getRepertoire().toFile(), "old"); // Instancie un fichier old dans le répertoire de travail
			this.repertoireOld = oldRep.toPath(); // Initialise le chemin du répertoire old
			// Si le fichier old n'éxiste pas
			if(!Files.exists(this.getRepertoireOld())) {
				Files.createDirectory(this.getRepertoireOld()); // Créé le dossier old
			}
			
			Files.walk(repertoireOld)
				.filter(Files::isRegularFile)
				.forEach(x -> nbFichierRepertoireOld++);
			
			System.out.println("--> Le répertoire old contient au lancement " + nbFichierRepertoireOld + " fichier(s).");
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	}

	/**
	 * Méthode qui initialise le singleton.
	 * @param cheminDuRepertoire chemin physique du répertoire de travail.
	 * @throws Exception
	 */
	public static void initialise(String cheminDuRepertoire) throws Exception {
		
		if(WorkFolder.folder != null) {
			throw new Exception("Le répertoire de travail a déjà été initialisé.");
		}
		
		WorkFolder.folder = new WorkFolder(cheminDuRepertoire);
		
	}

	

	
	
}
