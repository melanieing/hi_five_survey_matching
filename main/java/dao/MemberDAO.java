package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import vo.MemberVO;

public class MemberDAO {

	private JdbcTemplate jdbcTemplate;
	
	public MemberDAO() {
		jdbcTemplate = JdbcTemplate.getInstance();
	}
	
	// MEMBER 테이블에 레코드 삽입
	// INSERT INTO "MEMBER" VALUES ("SQ_MEMBER".NEXTVAL, ?, ?, ?)
	public boolean insertMember(MemberVO vo) {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null; 
		
		String sql = "INSERT INTO \"MEMBER\" VALUES (\"SQ_MEMBER\".NEXTVAL, ?, ?, ?)";

		try {
			conn = jdbcTemplate.getConnection();
			
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,  vo.getMemName()); 
			pstmt.setInt(2, vo.getMemAge());
			pstmt.setString(3,  vo.getMemGender());
			
			int result = pstmt.executeUpdate(); 
			System.out.printf("%s님, 반갑습니다!^_^\n", vo.getMemName());
			// System.out.printf("%s님, %d분의 소중한 정보가 등록되었습니다^_^\n", vo.getMemName(), result);
			flag = true; // INSERT 성공 시 true 반환
		} catch (SQLException e) {
			System.out.printf("%s님, 다시 오셨군요! 반갑습니다^_^\n", vo.getMemName());
			// e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return flag; // INSERT 실패 시 그대로 false 반환
	}
		
	// MEMBER 테이블의 전체 레코드 리스트로 받아오기
	// SELECT "MEM_ID", "MEM_NAME", "MEM_AGE", "MEM_GENDER" FROM "MEMBER"
	public List<MemberVO> selectMemAll() {
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		List<MemberVO> list = new ArrayList<>();
		String sql = "SELECT \"MEM_ID\", \"MEM_NAME\", \"MEM_AGE\", \"MEM_GENDER\" FROM \"MEMBER\"";
		
		try {
			conn = jdbcTemplate.getConnection();			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberVO tmp = new MemberVO(
						rs.getLong("MEM_ID"), 
						rs.getString("MEM_NAME"), 
						rs.getInt("MEM_AGE"), 
						rs.getString("MEM_GENDER"));
				list.add(tmp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if (pstmt != null) {
				try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return (list.size() == 0) ? null : list;
	}
	
	// MEMBER 테이블에서 특정 회원고유번호로 회원정보 받아오기
	// SELECT "MEM_ID", "MEM_NAME", "MEM_AGE", "MEM_GENDER" FROM "MEMBER" WHERE "MEM_ID"=?
	public MemberVO searchByMemId(long memId) {
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		MemberVO memInfo = null;
		String sql = "SELECT \"MEM_ID\", \"MEM_NAME\", \"MEM_AGE\", \"MEM_GENDER\" FROM \"MEMBER\" WHERE \"MEM_ID\"=?";
		
		try {
			conn = jdbcTemplate.getConnection();			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, memId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				memInfo = new MemberVO(rs.getLong("MEM_ID"), rs.getString("MEM_NAME"), 
						rs.getInt("MEM_AGE"), rs.getString("MEM_GENDER"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if (pstmt != null) {
				try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		} 
		return (memInfo == null) ? null : memInfo;
	}
	
	// MEMBER 테이블에서 회원별명으로 회원고유번호를 받아오기
	// SELECT "MEM_ID" FROM "MEMBER" WHERE "MEM_NAME"=?
	public long searchMemId(String memName) {
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		long memId = 0L;
		String sql = "SELECT \"MEM_ID\" FROM \"MEMBER\" WHERE \"MEM_NAME\"=?";
		
		try {
			conn = jdbcTemplate.getConnection();			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memName);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				memId = rs.getLong("MEM_ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if (pstmt != null) {
				try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return memId;
	}

	// MEMBER 테이블에서 모든 레코드의 개수 반환
	// SELECT COUNT(*) FROM "MEMBER"
	public int countMemId() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int count = 0;
		
		try {
			conn = jdbcTemplate.getConnection();			
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*) FROM \"MEMBER\"");
	
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return count;
	}	
	
	// MEMBER 테이블에서 회원고유번호와 일치하는 레코드 삭제
	// DELETE FROM "MEMBER" WHERE "MEM_ID"=?
	public boolean deleteByMemId(long memId) {
		boolean flag = false;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "DELETE FROM \"MEMBER\" WHERE \"MEM_ID\"=?";
		
		try {
			conn = jdbcTemplate.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, memId);
			
			int result = pstmt.executeUpdate(); 
			if (result != 0) { // DELETE 성공
				System.out.println(result + "명의 정보가 삭제되었습니다.");
				flag = true; // DELETE 성공 시 true 반환
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return flag; // DELETE 실패 시 그대로 false 반환

	}

	
	// MEMBER 테이블에서 특정 회원고유번호와 일치하는 레코드의 전체 정보 수정하기
	// UPDATE "MEMBER" SET "MEM_NAME"=?, "MEM_AGE"=?, "MEM_GENDER"=? WHERE "MEM_ID=?
	public boolean updateByMemId(long targetMemId, String memName, int memAge, String memGender) {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "UPDATE \"MEMBER\" SET \"MEM_NAME\"=?, \"MEM_AGE\"=?, \"MEM_GENDER\"=? WHERE \"MEM_ID\"=?";
			
		try {
			conn = jdbcTemplate.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,memName);
			pstmt.setInt(2, memAge);
			pstmt.setString(3, memGender);
			pstmt.setLong(4, targetMemId);
			
			int result = pstmt.executeUpdate();
			System.out.println(result + "명의 정보가 수정되었습니다.");
			flag = true; // UPDATE 성공 시 true 반환
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return flag; // UPDATE 실패 시 그대로 false 반환
	}
	
	
}
