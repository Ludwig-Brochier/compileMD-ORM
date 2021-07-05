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
				String requete = "CREATE TABLE IF NOT EXISTS Chemin (\n"
						+ " id INTEGER PRIMARY KEY,\n"
						+ " path TEXT NOT NULL,\n"
						+ " estActif INTEGER NOT NULL\n"
						+ ");";
				
				try(Statement stmt = _connect.createStatement()) {
					stmt.execute(requete);
					
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
}
