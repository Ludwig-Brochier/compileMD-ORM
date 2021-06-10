package gestionFichier;

import java.nio.file.Path;

/**
 * Classe abstraite utilisée par tous les fichiers.
 * @author nicolas
 *
 */
public abstract class BaseFile implements Comparable<BaseFile> {
	
	/**
	 * Chemin physique du fichier sur le disque dur.
	 */
	protected Path chemin;
	public Path getChemin() {
		return chemin;
	}
	
	/**
	 * Nom du fichier.
	 */
	protected String nomDuFichier;
	public String getNomDuFichier() {
		return nomDuFichier;
	}
	
	/**
	 * Constructeur explicite de la classe.
	 * @param cheminDuFichier
	 */
	protected BaseFile(Path cheminDuFichier) {
		
		this.chemin = cheminDuFichier;
		this.nomDuFichier = cheminDuFichier.getFileName().toString();
		
	}
	
	/**
	 * Fabrique pour les sous-classes de BaseFile.
	 * @param cheminDuFichier
	 * @return Une instance d'une des sous-classes conforme en fonction de la terminaison du fichier.
	 */
	public static BaseFile Fabrik(Path cheminDuFichier) {
		
		if(cheminDuFichier.getFileName().toString().endsWith(".md")) {
			return new MarkdownFile(cheminDuFichier);
		}
		
		if(cheminDuFichier.getFileName().toString().endsWith(".txt")) {
			return new TextFile(cheminDuFichier);
		}
		
		if(cheminDuFichier.getFileName().toString().endsWith(".docx")) {
			return new WordFile(cheminDuFichier);
		}
		
		if(cheminDuFichier.getFileName().toString().endsWith(".xlsx")) {
			return new ExcelFile(cheminDuFichier);
		}
		
		//if(cheminDuFichier.getFileName().toString().endsWith(".pdf")) {
		//	return new PdfFile(cheminDuFichier);
		//}
		
		return new GenericFile(cheminDuFichier);
		
	}
	
	/**
	 * Permets de comparer deux instances de BaseFile afin de trier par ordre alphabétique de nom.
	 * @param Une autre instance de BaseFile.
	 * @return -1, 0 ou 1 en fonction du positionnement du nom du fichier.
	 */
	@Override
	public int compareTo(BaseFile secondFichier) {
		
		return this.nomDuFichier.compareTo(secondFichier.getNomDuFichier().toString());
		
	}
	
	/**
	 * Point d'entrée principal de la classe qui lance les actions devant être effectuées.
	 */
	public abstract void action();
}
