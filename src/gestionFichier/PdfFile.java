package gestionFichier;

import java.nio.file.Path;

public class PdfFile extends BaseFile implements IDeplacable {

	public PdfFile(Path cheminDuFichier) {
		super(cheminDuFichier);
	}

	@Override
	public void action() {
		if(this.getIsDeplacable()) {
			System.out.println("PdfFile : " + this.nomDuFichier + " ok");
			this.deplacer();
		} else {
			System.out.println("PdfFile : " + this.nomDuFichier + " ko");
		}
	}

}
