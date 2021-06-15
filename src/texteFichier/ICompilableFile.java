package texteFichier;

import java.nio.file.Path;

import gestionFichier.WorkFolder;

/**
 * Interface devant être implémentée par tous les fichiers compilables.
 * @author Ludwig
 *
 */
public interface ICompilableFile {

	/**
	 * Chemin physique du fichier sur le disque dur.
	 * @return Chemin
	 */
	public Path getChemin();
	
	/**
	 * Permets d'obtenir le nom du projet qui est trouvé à partir du nom du fichier ou du nom du répertoire de travail.
	 * @return Le nom du projet
	 * @throws Exception 
	 */
	public default String getNomDuProjet() {
		
		String nomDuProjet;
		String nomDuFichier = getChemin().getFileName().toString();
		
		// Récupère l'index du dernier underscore si présent dans le nom du fichier
		int indexUnder = nomDuFichier.lastIndexOf("_");
		
		// Si underscore non présent dans le nom du fichier
		if(indexUnder == -1) {
			
			try {
				nomDuProjet = WorkFolder.getFolder().getNomDuProjetDefaut(); // Récupère le nom par défaut du projet se trouvant dans WorkFolder
			} catch (Exception e) {
				nomDuProjet = "erreur_nom_projet";
			}
			
		} else {
			
			nomDuProjet = nomDuFichier.substring(0, indexUnder) + "_compile"; // Récupère toute la partie gauche du nom du fichier jusqu'au dernier underscore
			
		}
		
		return nomDuProjet;
	}
	
}
