package DataBaseExample;

import java.sql.*;

public class DataBase {
	// 드라이버 명 지정
	static final String driverName = "com.mysql.cj.jdbc.Driver";
	// 데이터베이스명 지정
	static final String DBName = "test";
	// URL 지정
	// ?serverTimezone=UTC : server time zone value '' is unrecognized or represents
	// more than one time zone
	static final String dbURL = "jdbc:mysql://127.0.0.1:3306/" + DBName + "?serverTimezone=UTC"+"&useSSL=false";

	public static void main(String[] args) {
		Connection connect = null;
		Statement statement = null;

		try {
			// mysql에서 제공하는 Driver클래스를 JVM method area에 로드(객체 반환)
			Class.forName(driverName);
			
			// change id into mysql id
			String userid = "id";
			// change password into mysql password
			String userpassword = "password";
			// DriverManeger로 Connection 객체 생성
			connect = DriverManager.getConnection(dbURL, userid, userpassword);

			System.out.println("MySql connection start");

			// 퀴리를 데이터베이스로 전송해주는 객체
			// Statement : 퀴리 수행 시 매번 쿼리를 컴파일 후 실행
			// PreparedStatement : 첫 쿼리 수행 시 쿼리를 컴파일 후 실행하고 캐시에 저장하여 재사용
			// : 따라서 동일한 쿼리를 반복 수행시 Statement 보다 PreparedStatement를 사용하는 것이 성능 향상에 도움이 됨
			statement = connect.createStatement();

			String sql = "select * from chatterboxuser";
			// 쿼리 실행 후 쿼리 실행 결과 반환
			ResultSet resultSet = statement.executeQuery(sql);

			// 1행부터 다음 행으로 한 행씩 이동하여 모든 행 추출
			while (resultSet.next()) {
				String id = resultSet.getString("id");
				String password = resultSet.getString("password");
				System.out.println("id | " + id + "\n" + "password | " + password);
			}
			resultSet.close();
		}

		catch (SQLException se1) {
			se1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
			}
			try {
				if (connect != null)
					connect.close();
			} catch (SQLException se3) {
				se3.printStackTrace();
			}
		}
		System.out.println("MySql connection stop");
	}

}
