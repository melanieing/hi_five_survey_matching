package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Singleton pattern
 * 메모리 상에 객체를 반드시 하나만 생성되게끔 하는 디자인 패턴
 */
public class JdbcTemplate {
	
	private static JdbcTemplate instance;
	
	// 기본 Oracle DB 커넥션 정보
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String user = "c##hifive";
	private String pwd = "1234";
	
	private JdbcTemplate() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static JdbcTemplate getInstance() {
		//synchronized (JdbcTemplate.class) {
			if (instance == null) {
				instance = new JdbcTemplate();
			}			
		//}
		return instance;
		
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, pwd);
	}
}
