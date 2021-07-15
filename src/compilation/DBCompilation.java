package compilation;

import java.sql.Statement;
import java.util.List;
import outil.OutilDate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import orm.Chemin;
import orm.Compile;

public class DBCompilation {
	
	/*
	 * Méthode de connexion
	 */
	private ConnectionSource _connect;
	private Dao<Chemin, Integer> cheminDao;
	private Dao<Compile, Integer> compileDao;
	
	/*
	 * Singleton
	 */
	private static DBCompilation singleton;	
	public static DBCompilation getSingleton() {
		if (singleton == null) {
			singleton = new DBCompilation();
		}
		return singleton;
	}
	
	/*
	 * Constructeur singleton de la classe
	 * Permet de se connecter ou créer le cas échéant une base de données
	 */
	private DBCompilation() {
		String url = "jdbc:sqlite:C:/Users/CBL/Desktop/PAPJava/BDD/compilation.db"; // Chaine de connexion brute
		
		try {
			_connect = new JdbcConnectionSource(url);
			createTables(url); // Méthode pour créer les tables
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Permet de mettre un terme à la connexion
	 * NE PAS APPELER
	 * Seul le garbage collector en a l'utilité
	 */
	@Override
	public void finalize() {
		try {
			_connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Permet de créer des tables dans la base de données
	 */
	private void createTables(String url) {	
		try {
			cheminDao = DaoManager.createDao(_connect, Chemin.class);
			compileDao = DaoManager.createDao(_connect, Compile.class);
			TableUtils.createTableIfNotExists(_connect, Chemin.class);
			TableUtils.createTableIfNotExists(_connect, Compile.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}
	
	/*
	 * Permet de récupérer le chemin actif
	 */
	public String getPathActif() {
		Chemin chemin = null;
		try {			
			QueryBuilder<Chemin, Integer> qb = cheminDao.queryBuilder();
			qb.where().eq("estActif", 1);
			List<Chemin> chemins = cheminDao.query(qb.prepare());
			chemin = chemins.get(0);
		} catch (Exception e) {
			System.out.println(e.getMessage());			
		}
		
		return chemin.path;
	}
	
	/*
	 * Permet de récupérer l'identifiant d'un chemin précis
	 */
	public int getIdPath(String path) {
		int idPath = 0;
		
		try {
			QueryBuilder<Chemin, Integer> qb = cheminDao.queryBuilder();
			qb.where().eq("path", path);
			List<Chemin> chemins = cheminDao.query(qb.prepare());
			idPath = chemins.get(0).id;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		
		return idPath;
	}
	
	/*
	 * Permet d'ajouter un nouveau chemin à la base de données
	 */
	public void insertNewPath(String path) {
		Chemin newChemin = new Chemin(0, path, 1);
		try {
			updatePathActif();
			cheminDao.create(newChemin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updatePathActif() {
		try {
			UpdateBuilder<Chemin, Integer> ub = cheminDao.updateBuilder();
			ub.updateColumnValue("estActif", 0);
			ub.where().eq("estActif", 1);
			ub.update();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Permet de mettre à jour un chemin de la base de données
	 */
	public void updatePath(int id) {		
		try {
			updatePathActif();
			UpdateBuilder<Chemin, Integer> ub = cheminDao.updateBuilder();
			ub.updateColumnValue("estActif", 1);
			ub.where().idEq(id);
			ub.update();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Permet de récupérer l'identifiant d'une compilation en fonction du titre du livre
	 */
	public int getIdCompile(String titreLivre) {
		int id = 0;		
		String requete = "SELECT id FROM Compile WHERE titreLivre = ? ORDER BY dteCompile DESC";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setString(1, titreLivre);
				ResultSet rs = pstmt.executeQuery();				
				while (rs.next()) {
					id = rs.getInt("id");
					break;
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return id;
	}
	
	/*
	 * Permet de récupérer la date de la compilation d'un livre
	 */
	public String getDateCompile(int id) {
		String date = "";
		String requete = "SELECT dteCompile FROM Compile WHERE id = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setInt(1, id);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					date = rs.getString("dteCompile");
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return date;
	}
	
	/*
	 * Permet de récupérer le nombre de mots total d'un livre précis
	 */
	public int getNbMotsLivre(int id) {
		int nbMotsLivre = 0;
		String requete = "SELECT nbMotsLivre FROM Compile WHERE id = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setInt(1, id);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					nbMotsLivre = rs.getInt("nbMotsLivre");
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return nbMotsLivre;
	}
	
	/*
	 * Permet de récupérer le nombre de caractères total d'un livre précis
	 */
	public int getNbCaracteresLivre(int id) {
		int nbCaracteresLivre = 0;
		String requete = "SELECT nbCaracteresLivre FROM Compile WHERE id = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setInt(1, id);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					nbCaracteresLivre = rs.getInt("nbCaracteresLivre");
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		return nbCaracteresLivre;
	}
	
	/*
	 * Permet d'ajouter une nouvelle compilation
	 */
	public void insertNewCompile(String titreLivre, int nbMotsLivre, int nbCaracteresLivre) {
		String requete = "INSERT INTO Compile(titreLivre, dteCompile, nbMotsLivre, nbCaracteresLivre) VALUES(?,?,?,?)";
		String today = OutilDate.getTodayDate();
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setString(1, titreLivre);
				pstmt.setString(2, today);
				pstmt.setInt(3, nbMotsLivre);
				pstmt.setInt(4, nbCaracteresLivre);
				pstmt.executeUpdate();
				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Permet de mettre à jour une compilation
	 */
	public void updateCompile(int id, int nbMotsLivre, int nbCaracteresLivre) {
		String requete = "UPDATE Compile SET nbMotsLivre = ?, nbCaracteresLivre = ? WHERE id = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setInt(1, nbMotsLivre);
				pstmt.setInt(2, nbCaracteresLivre);
				pstmt.setInt(3, id);
				pstmt.executeUpdate();
				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
