/**
 * 
 */
package gestionFichier;

import java.nio.file.Path;
/**
 * @author Loud
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

