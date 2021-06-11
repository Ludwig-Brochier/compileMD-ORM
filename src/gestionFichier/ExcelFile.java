/**
 * 
 */
package gestionFichier;

import java.nio.file.Path;

/**
 * Classe gérant les fichiers excel. Ces fichiers doivent être déplacé, mais pas compilé ni comptés.
 * @author Ludwig
 *
 */
public class ExcelFile extends BaseFile implements IDeplacable{

	protected ExcelFile(Path cheminDuFichier) {
		super(cheminDuFichier);
	}
	
	@Override
	public void action() {
		if (this.getIsDeplacable()) {
			System.out.println("ExcelFile : " + this.nomDuFichier + " ok");
			this.deplacer();
		} else {
			System.out.println("ExcelFile : " + this.nomDuFichier + " ko");
		}	
	}
}

