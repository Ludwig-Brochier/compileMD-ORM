package compilation;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCompilation {
	
	/*
	 * Méthode de connexion
	 */
	private Connection _connect;
	
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
			_connect = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		createTables(url); // Méthode pour créer les tables
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Permet de créer des tables dans la base de données
	 */
	private void createTables(String url) {		
		
		try {
			if (_connect != null) {
				String requeteTableChemin = """
						CREATE TABLE IF NOT EXISTS Chemin
						(
						id INTEGER PRIMARY KEY,
						path TEXT NOT NULL,
						estACtif INTEGER NOT NULL
						)
						""";	
				String requeteTableCompile = """ 
						CREATE TABLE IF NOT EXISTS Compile
						(
						id INTEGER PRIMARY KEY,
						titreLivre TEXT NOT NULL,
						dteCompile TEXT NOT NULL,
						nbMotsLivre INTEGER DEFAULT 0,
						nbMotsCompile INTEGER DEFAULT 0
						)
						""";
				
				try(Statement stmt = _connect.createStatement()) {
					stmt.execute(requeteTableChemin);
					stmt.execute(requeteTableCompile);
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Permet de récupérer le chemin actif
	 */
	public String getPathActif() {
		String chemin = "";
		String requete = "SELECT * FROM Chemin WHERE estActif = 1";
		
		try(Statement stmt = _connect.createStatement();
			// Mappage
			ResultSet rs = stmt.executeQuery(requete)){
			
				while(rs.next()) {
					chemin = rs.getString("path");					
				}	
			// Fin Mappage
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return chemin;
	}
	
	/*
	 * Permet de récupérer l'identifiant d'un chemin précis
	 */
	public int getIdPath(String path) {
		int idPath = 0;
		
		String requete = "SELECT id FROM Chemin WHERE path = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setString(1, path);
				// Mappage
				ResultSet rs = pstmt.executeQuery();				
				while (rs.next()) {
					idPath = rs.getInt("id");					
				}
				// Fin Mappage
				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return idPath;
	}
	
	/*
	 * Permet d'ajouter un nouveau chemin à la base de données
	 */
	public void insertNewPath(String path) {
		String requeteUpdate = "UPDATE Chemin SET estActif = 0 WHERE estActif = 1";
		String requeteInsert = "INSERT INTO Chemin(path,estActif) VALUES(?,1)";
		
		try(Statement stmt = _connect.createStatement();
			PreparedStatement pstmt = _connect.prepareStatement(requeteInsert)) {
				stmt.executeUpdate(requeteUpdate);
				pstmt.setString(1, path);
				pstmt.executeUpdate();				
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Permet de mettre à jour un chemin de la base de données
	 */
	public void updatePath(String path) {
		String requeteUpdateActif = "UPDATE Chemin SET estActif = 0 WHERE estActif = 1";
		String requeteUpdatePath = "UPDATE Chemin SET estActif = 1 WHERE id = ?";
		
		int idPath = getIdPath(path);
		
		try(Statement stmt = _connect.createStatement();
			PreparedStatement pstmt = _connect.prepareStatement(requeteUpdatePath)) {
				stmt.executeUpdate(requeteUpdateActif);
				pstmt.setInt(1, idPath);
				pstmt.executeUpdate();
			
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
	 * Permet de récupérer le nombre de mots de la dernière compilation du livre
	 */
	public int getNbMotsCompile(int id) {
		int nbMotsCompile = 0;
		String requete = "SELECT nbMotsCompile FROM Compile WHERE id = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setInt(1, id);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					nbMotsCompile = rs.getInt("nbMotsCompile");
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return nbMotsCompile;
	}
	
	/*
	 * Permet d'ajouter une nouvelle compilation
	 */
	public void insertNewCompile(String titreLivre, int nbMotsLivre, int nbMotsCompile) {
		String requete = "INSERT INTO Compile(titreLivre, dteCompile, nbMotsLivre, nbMotsCompile) VALUES(?,?,?,?)";
		Date date = new Date(); // Date du jour
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Format de la date
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setString(1, titreLivre);
				pstmt.setString(2, dateFormat.format(date));
				pstmt.setInt(3, nbMotsLivre);
				pstmt.setInt(4, nbMotsCompile);
				pstmt.executeUpdate();
				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Permet de mettre à jour une compilation
	 */
	public void updateCompile(int id, int nbMotsLivre, int nbMotsCompile) {
		String requete = "UPDATE Compile SET nbMotsLivre = ?, nbMotsCompile = ? WHERE id = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setInt(1, nbMotsLivre);
				pstmt.setInt(2, nbMotsCompile);
				pstmt.setInt(3, id);
				pstmt.executeUpdate();
				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
