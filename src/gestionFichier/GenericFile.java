package gestionFichier;

import java.nio.file.Path;

/**
 * Classe gérant les fichiers non gérés. Ces fichiers doivent être ni comptés ni compilés ni déplacé.
 * @author Ludwig
 *
 */
public class GenericFile extends BaseFile {

	/**
	 * Constructeur explicite de la classe. Déplacable
	 * @param cheminDuFichier
	 */
	protected GenericFile(Path cheminDuFichier) {
		super(cheminDuFichier);
	}

	/**
	 * Point d'entrée principal de la classe qui lance les actions devant être effectuées.
	 */
	@Override
	public void action() {
		System.out.println("GenericFile : " + this.nomDuFichier);
	}

}
