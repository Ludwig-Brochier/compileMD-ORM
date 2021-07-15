package orm;

import com.j256.ormlite.field.DatabaseField;

public class Compile {
	@DatabaseField(generatedId = true)
	public int id;
	
	@DatabaseField(canBeNull = false)
	public String titreLivre;
	
	@DatabaseField(canBeNull = false)
	public String dteCompile;
	
	@DatabaseField(canBeNull = false)
	public int nbMotsLivre;
	
	@DatabaseField(canBeNull = false)
	public int nbCaracteresLivre;
	
	public Compile() { }
	
	public Compile(int id, String titreLivre, String dteCompile, int nbMots, int nbCara) {
		this.id = id;
		this.titreLivre = titreLivre;
		this.dteCompile = dteCompile;
		this.nbMotsLivre = nbMots;
		this.nbCaracteresLivre = nbCara;
	}
}
