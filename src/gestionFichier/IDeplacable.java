package gestionFichier;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Interface devant être implémentée par tous les fichiers déplacables.
 * @author Ludwig
 *
 */
public interface IDeplacable {

	/**
	 * Chemin physique du fichier sur le disque dur.
	 * @return Chemin
	 */
	public Path getChemin();
	
	/**
	 * Permets de savoir si le fichier doit être déplacé.
	 * @return Vrai si le fichier doit être déplacé.
	 */
	public default boolean getIsDeplacable() {
		
		// Récupère le nom du répertoire parent du fichier appelant
		String repParent = this.getChemin().getParent().getFileName().toString();
		
		if(repParent.equals("old")) return false; // Test si répertoire égal à "old"
		
		try {
			
			File fichierDansOld = new File(WorkFolder.getFolder().getRepertoireOld().toFile(), this.getChemin().getFileName().toString());
			if(Files.exists(fichierDansOld.toPath())) return false;	//Ce fichier existe déjà  dans "old".
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * Lance le déplacement du fichier.
	 */
	public default void deplacer() {
		
		Path source = getChemin();
		Path cible;
		try {
			
			cible = (new File(WorkFolder.getFolder().getRepertoireOld().toFile(), this.getChemin().getFileName().toString())).toPath();
			Files.move(source, cible);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
