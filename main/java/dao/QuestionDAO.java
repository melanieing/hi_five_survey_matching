package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import vo.QuestionVO;

public class QuestionDAO {
	
	private JdbcTemplate jdbcTemplate;
	private MemberDAO mdao;
	
	public QuestionDAO() {
		jdbcTemplate = JdbcTemplate.getInstance();
	}
	
	// QUESTION 테이블에 레코드 삽입
	// INSERT INTO "QUESTION" VALUES ("SQ_QUESTION".NEXTVAL, "MEM_ID", "QUEST_TYPE", "QUEST_ANS")
	public boolean insertQuest(QuestionVO vo) {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "INSERT INTO \"QUESTION\" VALUES (\"SQ_QUESTION\".NEXTVAL, ?, ?, ?)";
		try {
			conn = jdbcTemplate.getConnection();
			
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, vo.getMemId()); 
			pstmt.setInt(2,  vo.getQuestType());
			pstmt.setInt(3, vo.getQuestAns());
			
			int result = pstmt.executeUpdate();
			System.out.printf("설문응답 %d개가 등록되었습니다^_^\n\n", result);
			flag = true; // INSERT 성공 시 true 반환
		} catch (SQLException e) {
			updateQuest(vo.getMemId(), vo.getQuestAns(), vo.getQuestType());
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
	
	// QUESTION 테이블에서 모든 레코드 검색
	// SELECT "QUEST_ID", "MEM_ID", "QUEST_TYPE", "QUEST_ANS" FROM "QUESTION"
	public List<QuestionVO> selectQuestAll() {
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		List<QuestionVO> list = new ArrayList<>();
		String sql = "SELECT \"QUEST_ID\", \"MEM_ID\", \"QUEST_TYPE\", \"QUEST_ANS\" FROM \"QUESTION\"";
		
		try {
			conn = jdbcTemplate.getConnection();			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(); 
			while (rs.next()) {
				QuestionVO tmp = new QuestionVO(
						rs.getLong("QUEST_ID"), 
						rs.getLong("MEM_ID"),
						rs.getInt("QUEST_TYPE"),
						rs.getInt("QUEST_ANS"));
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
	
	// QUESTION 테이블에서 설문고유번호로 레코드 하나 검색
	// SELECT "QUEST_ID", "MEM_ID", "QUEST_TYPE", "QUEST_ANS" WHERE "QUEST_ID"=?
	public QuestionVO searchQuestById(long questId) {
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		QuestionVO questInfo = null;
		String sql = "SELECT \"QUEST_ID\", \"MEM_ID\", \"QUEST_TYPE\", \"QUEST_ANS\" FROM \"QUESTION\" WHERE \"QUEST_ID\"=?";
		
		try {
			conn = jdbcTemplate.getConnection();			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, questId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				questInfo = new QuestionVO(rs.getLong("QUEST_ID"), rs.getLong("MEM_ID"), 
						rs.getInt("QUEST_TYPE"), rs.getInt("QUEST_ANS"));
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
		return (questInfo == null) ? null : questInfo;
	}

	// QUESTION 테이블에서 설문유형으로 레코드 하나 검색
	// SELECT "QUEST_ID", "MEM_ID", "QUEST_TYPE", "QUEST_ANS" WHERE "QUEST_TYPE"=?
	public List<QuestionVO> searchQuestByQuestType(int questType) {
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		List<QuestionVO> list = new ArrayList<>();
		String sql = "SELECT \"QUEST_ID\", \"MEM_ID\", \"QUEST_TYPE\", \"QUEST_ANS\" FROM \"QUESTION\" WHERE \"QUEST_TYPE\"=?";
		
		try {
			conn = jdbcTemplate.getConnection();			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, questType);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				QuestionVO tmp = new QuestionVO(
						rs.getLong("QUEST_ID"), 
						rs.getLong("MEM_ID"), 
						rs.getInt("QUEST_TYPE"),
						rs.getInt("QUEST_ANS"));
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
	
	
	// QUESTION 테이블에서 (회원고유번호, 설문유형)으로 설문응답 검색
	// SELECT "QUEST_ANS" FROM "QUESTION" WHERE "MEM_ID"=? AND "QUEST_TYPE"=?
	public int searchQuestAnswer(long memId, int questType) {
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		String sql = "SELECT \"QUEST_ANS\" FROM \"QUESTION\" WHERE \"MEM_ID\"=? AND \"QUEST_TYPE\"=?";
		int questAns = 0;
		
		try {
			conn = jdbcTemplate.getConnection();			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, memId);
			pstmt.setInt(2, questType);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				questAns = rs.getInt(1);
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
		return questAns;
	}

	// QUESTION 테이블에서 (설문유형, 설문응답)에 일치하는 회원고유번호  검색
	// SELECT "MEM_ID" FROM "QUESTION" WHERE "QUEST_TYPE"=? AND "QUEST_ANS"=?
	public ArrayList<Long> searchMemId(int questType, int questAns) {
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		ArrayList<Long> list = new ArrayList<>();
		String sql = "SELECT \"MEM_ID\" FROM \"QUESTION\" WHERE \"QUEST_TYPE\"=? AND \"QUEST_ANS\"=?";
		
		try {
			conn = jdbcTemplate.getConnection();			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,  questType);
			pstmt.setInt(2, questAns);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getLong("MEM_ID"));
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
	
	// QUESTION 테이블의 모든 레코드 수 반환
	// SELECT COUNT(*) FROM "QUESTION"
	public int countQuest() {
		Connection conn = null;
		Statement stmt = null; 
		ResultSet rs = null;
		int rowCount = 0;
		
		try {
			conn = jdbcTemplate.getConnection();			
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*) FROM \"QUESTION\"");
			if (rs.next()) {
				rowCount = rs.getInt(1);
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
		return rowCount;
	}	
	
	// QUESTION 테이블에서 (설문유형, 설문응답)에 일치하는 레코드 수 반환 
	// SELECT COUNT(*) FROM "QUESTION" WHERE "QUEST_TYPE"=? AND "QUEST_ANS"=?
	public int countQuest(int questType, int questAns) {
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(*) FROM \"QUESTION\" WHERE \"QUEST_TYPE\"=? AND \"QUEST_ANS\"=?";
		int rowCount = 0;
		
		try {
			conn = jdbcTemplate.getConnection();			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,  questType);
			pstmt.setInt(2, questAns);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rowCount = rs.getInt(1);
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
		return rowCount;
	}
	
	// QUESTION 테이블에서 설문유형에 일치하는 레코드 수 반환 
	// SELECT COUNT(*) FROM "QUESTION" WHERE "QUEST_TYPE"=?
	public int countQuest(int questType) {
		Connection conn = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(*) FROM \"QUESTION\" WHERE \"QUEST_TYPE\"=?";
		int rowCount = 0;
		
		try {
			conn = jdbcTemplate.getConnection();			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,  questType);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				rowCount = rs.getInt(1);
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
		return rowCount;
	}
	
	// QUESTION 테이블에서 설문고유번호가 일치하는 레코드 하나 삭제
	// DELETE FROM "QUESTION" WHERE "QUEST_ID"=?
	public boolean deleteByQuestId(long questId) {
		boolean flag = false;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "DELETE FROM \"QUESTION\" WHERE \"QUEST_ID\"=?";
		
		try {
			conn = jdbcTemplate.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, questId);
			
			int result = pstmt.executeUpdate();
			System.out.println(result + "개의 설문정보가 삭제되었습니다.");
			flag = true; // DELETE 성공 시 true 반환
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
	
	// QUESTION 테이블에서 회원고유번호와 일치하는 레코드 삭제
	// DELETE FROM "QUESTION" WHERE "MEM_ID"=?
	public boolean deleteByMemId(long memId) {
		boolean flag = false;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "DELETE FROM \"QUESTION\" WHERE \"MEM_ID\"=?";
		
		try {
			conn = jdbcTemplate.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, memId);
			
			int result = pstmt.executeUpdate();
			System.out.println(result + "개의 설문정보가 삭제되었습니다.");
			flag = true; // DELETE 성공 시 true 반환
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
	
	// QUESTION 테이블에서 (회원고유번호, 설문유형)이 일치하는 레코드의 설문응답 수정
	// UPDATE "QUESTION" SET "QUEST_ANS"=? WHERE "MEM_ID"=? AND "QUEST_TYPE"=?
	public boolean updateQuest(long memId, int questAns, int questType) {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "UPDATE \"QUESTION\" SET \"QUEST_ANS\"=? WHERE \"MEM_ID\"=? AND \"QUEST_TYPE\"=?";
			
		try {
			conn = jdbcTemplate.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,questAns);
			pstmt.setLong(2, memId);
			pstmt.setInt(3,  questType);
			
			int result = pstmt.executeUpdate();
			System.out.printf("%s님, %d명의 정보가 수정되었습니다^_^", mdao.searchByMemId(memId).getMemName(), result);
			flag = true; // UPDATE 성공 시 true 반환
		} catch (SQLException | NullPointerException e) {
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
