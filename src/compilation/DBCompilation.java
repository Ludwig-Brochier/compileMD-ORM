package compilation;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBCompilation {
	
	private Connection _connect;
	
	public DBCompilation() {
		String url = "jdbc:sqlite:C:/Users/CBL/Desktop/PAPJava/BDD/compilation.db";
		
		try {
			_connect = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		createNewDatabase(url);
	}
	
	@Override
	public void finalize() {
		_connect = null;
	}
	
	private void createNewDatabase(String url) {		
		
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
				String requeteTableLivre = """ 
						CREATE TABLE IF NOT EXISTS Livre
						(
						id INTEGER PRIMARY KEY,
						titre TEXT NOT NULL,
						nbTotalMots INTEGER DEFAULT 0,
						nbMots INTEGER DEFAULT 0
						)
						""";
				
				try(Statement stmt = _connect.createStatement()) {
					stmt.execute(requeteTableChemin);
					stmt.execute(requeteTableLivre);
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public String getPathActif() {
		String chemin = "";
		String requete = "SELECT * FROM Chemin WHERE estActif = 1";
		
		try(Statement stmt = _connect.createStatement();
			ResultSet rs = stmt.executeQuery(requete)){
			
				while(rs.next()) {
					chemin = rs.getString("path");					
				}				
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return chemin;
	}
	
	public int getIdPath(String path) {
		int idPath = 0;
		
		String requete = "SELECT id FROM Chemin WHERE path = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setString(1, path);
				ResultSet rs = pstmt.executeQuery();
				
				while (rs.next()) {
					idPath = rs.getInt("id");					
				}
				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return idPath;
	}
	
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
	
	public int getIdLivre(String titre) {
		int idLivre = 0;
		String requete = "SELECT id FROM Livre WHERE titre = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setString(1, titre);
				ResultSet rs = pstmt.executeQuery();
				
				while (rs.next()) {
					idLivre = rs.getInt("id");
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return idLivre;
	}	
	
	public int getNbTotalMots (int idLivre) {
		int nbTotalMots = 0;
		
		String requete = "SELECT nbTotalMots FROM Livre WHERE id = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setInt(1, idLivre);
				ResultSet rs = pstmt.executeQuery();
				
				while (rs.next()) {
					nbTotalMots = rs.getInt("nbTotalMots");
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return nbTotalMots;
	}
	
	public int getNbMots(int idLivre) {
		int nbMots = 0;
		
		String requete = "SELECT nbMots FROM Livre WHERE id = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setInt(1, idLivre);
				ResultSet rs = pstmt.executeQuery();
				
				while (rs.next()) {
					nbMots = rs.getInt("nbMots");
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return nbMots;
	}
	
	public void insertLivre(String titre, int nbMots) {
		String requete = "INSERT INTO Livre(titre, nbTotalMots, nbMots) VALUES(?, ?, ?)";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setString(1, titre);
				pstmt.setInt(2, nbMots);
				pstmt.setInt(3, nbMots);
				pstmt.executeUpdate();
				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateLivre(int IdLivre, int nbTotalMots, int nbMots) {
		String requete = "UPDATE Livre SET nbTotalMots = ?, nbMots = ? WHERE id = ?";
		
		try(PreparedStatement pstmt = _connect.prepareStatement(requete)) {
				pstmt.setInt(1, nbTotalMots);
				pstmt.setInt(2, nbMots);
				pstmt.setInt(3, IdLivre);
				pstmt.executeUpdate();
				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
