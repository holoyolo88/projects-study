package source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Logic {
	
	Connection connect = null;
	PreparedStatement pstmt = null;
	
	final String JDBCDRIVER = "com.mysql.jdbc.Driver";
	final String JDBCURL = "jdbc:mysql://127.0.0.1:3306/Board";
	
	void connect() {
		
		// change
		try {
			Class.forName(JDBCDRIVER);
			connect = DriverManager.getConnection(JDBCURL,"root","1234");
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	void disconnect() {
		try {
			if(pstmt!=null && !pstmt.isClosed())
				pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(connect!=null && !connect.isClosed())
					connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean updateDB(Post post) {
		connect();
		String sql = "update Board set post=?";
		try {
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, post.getPost());
			pstmt.executeUpdate();
		}
		catch(SQLException se) {
			se.printStackTrace();
			return false;
		}finally {
			disconnect();
		}
		return true;
				
	}
	public boolean deleteDB(Post post) {
		connect();
		String sql = "delete from Board where id=?";
		try {
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, post.getId());
			pstmt.executeUpdate();
		}
		catch(SQLException se) {
			se.printStackTrace();
			return false;
		}finally {
			disconnect();
		}
		return true;
				
	}
	
	public boolean insertDB(Post post) {
		connect();
		String sql = "inset into Board(id, password, post) values (?,?,?)";
		try {
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, post.getId());
			pstmt.setString(2, post.getId());
			pstmt.setString(3, post.getId());
			pstmt.executeUpdate();
		}
		catch(SQLException se) {
			se.printStackTrace();
			return false;
		}finally {
			disconnect();
		}
		return true;
				
	}
	public Post getDB(String id) {
		connect();
		String sql = "select * from Board where id = (?)";
		Post post = new Post();
		try {
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			rs.next();
			post.setId(rs.getString("id"));
			post.setPassword(rs.getString("password"));
			post.setPost(rs.getString("post"));
			
		}
		catch(SQLException se) {
			se.printStackTrace();
		}finally {
			disconnect();
		}
		return post;
				
	}
	
	public ArrayList<Post> getDBList(){
		connect();
		ArrayList<Post> posts = new ArrayList<Post>();
		String sql = "select * from Board order by idx desc";
		try {
			pstmt = connect.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Post post = new Post();
				
				post.setId(rs.getString("id"));
				post.setPassword(rs.getString("password"));
				post.setPost(rs.getString("post"));
			}
			rs.close();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			disconnect();
		}
		return posts;
	}
}
