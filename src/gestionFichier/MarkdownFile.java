package gestionFichier;

import java.nio.file.Path;
import texteFichier.ICompilableFile;

/**
 * Classe gérant les fichiers markdown. Ces fichiers doivent être comptés, compilés, mais pas déplacé.
 * @author Ludwig
 *
 */
public class MarkdownFile extends TextFile implements ICompilableFile {

	/**
	 * Constructeur explicite de la classe.
	 * @param cheminDuFichier
	 */
	protected MarkdownFile(Path cheminDuFichier) {
		super(cheminDuFichier);
	}
	
	/**
	 * Point d'entrée principal de la classe qui lance les actions devant être effectuées.
	 */
	@Override
	public void action() {
		
		System.out.println("MarkdownFile : " + this.nomDuFichier +
				" NbrMots : " + this.getNbrMots() + 
				" NbrCaracteres : " + this.getNbrCaracteres() +
				" projet : " + this.getNomDuProjet());
		
	}
	
}
