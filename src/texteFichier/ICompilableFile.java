package texteFichier;

import java.nio.file.Path;

import gestionFichier.WorkFolder;

/**
 * Interface devant être implémentée par tous les fichiers compilables.
 * @author nicolas
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
		
		int indexUnder = nomDuFichier.lastIndexOf("_");
		
		if(indexUnder == -1) {
			
			try {
				nomDuProjet = WorkFolder.getFolder().getNomDuProjetDefaut();
			} catch (Exception e) {
				nomDuProjet = "erreur_nom_projet";
			}
			
		} else {
			
			nomDuProjet = nomDuFichier.substring(0, indexUnder);
			
		}
		
		return nomDuProjet;
	}
	
}
