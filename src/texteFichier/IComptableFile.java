package texteFichier;

import java.nio.file.Path;
import java.util.List;

/**
 * Interface devant être implémentée par tous les fichiers comptable.
 * @author nicolas
 *
 */
public interface IComptableFile {

	/**
	 * Chemin physique du fichier sur le disque dur.
	 * @return Chemin
	 */
	public Path getChemin();
	
	/**
	 * Permets d'obtenir toutes les lignes qui sont présentes dans le fichier.
	 * @return Lignes
	 */
	public List<String> getlignes();
	
	/**
	 * Permets d'obtenir le nombre de mots contenus dans le fichier.
	 * @return Le nombre de mots contenus dans le fichier.
	 */
	public int getNbrMots();
	
	/**
	 * Permets d'obtenir le nombre de caractères contenus dans le fichier.
	 * @return Le nombre de caractères contenus dans le fichier.
	 */
	public int getNbrCaracteres();
	
}
