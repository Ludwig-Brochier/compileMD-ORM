package orm;

import com.j256.ormlite.field.DatabaseField;

public class Chemin {
	@DatabaseField(generatedId = true)
	public int id;
	
	@DatabaseField(canBeNull = false)
	public String path; 
	
	@DatabaseField(canBeNull = false)
	public int estActif;
	
	public Chemin() { }
	
	public Chemin(int id, String path, int estActif) {
		this.id = id;
		this.path = path;
		this.estActif = estActif;
	}
}
