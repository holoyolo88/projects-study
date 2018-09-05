import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO {
	Connection connect = null;
	PreparedStatement pstmt =null;
	
	static final String JDBCDRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DBNAME = "  ";
	static final String DBURL = "jdbc:mysql://127.0.0.1:3306/" + DBNAME + "?serverTimezone=UTC" +"&allowPublicKeyRetrieval=true"+"&useSSL=false";
	
	void connect () {
		try {
			Class.forName(JDBCDRIVER);
			connect = DriverManager.getConnection(DBURL, "id", "password");
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	void disconnect () {
		try {
			if(pstmt!= null && !pstmt.isClosed())
				pstmt.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(connect!=null && !connect.isClosed())
				connect.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean insert(Post post) {
		connect();
		
		String SQL = "inset into user(id, password) values(?,?)";
		try {
			pstmt = connect.prepareStatement(SQL);
			pstmt.setString(1,post.getId());
			pstmt.setString(2, post.getPassword());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			disconnect();
		}
		return true;
	}
	
	public boolean delete(Post post) {
		connect();
		
		String SQL = "delete from user(id,password) where id = (?) and password =(?)";
		
		try {
			pstmt = connect.prepareStatement(SQL);
			pstmt.setString(1, "'"+post.getId()+"'");
			pstmt.setString(2, "'"+post.getPassword()+"'");
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			disconnect();
		}
		return true;
	}
	
	public Post get(int id) {
		connect();
		
String SQL = "select * from user where id=(?)";
		
		try {
			pstmt = connect.prepareStatement(SQL);
			pstmt.setString(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			Post.
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
	}
	public static void main(String[] args) {

	}

}
