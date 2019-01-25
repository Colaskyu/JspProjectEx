package kr.co.boardEx.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.boardEx.DBConfig;
import kr.co.boardEx.SQL;
import kr.co.boardEx.vo.MemberVO;
import kr.co.boardEx.vo.TermsVO;

public class MemberService {
	// ΩÃ±€≈Ê ∞¥√º
	private static MemberService service = new MemberService();
	
	public static MemberService getInstance() {
		return service;
	}
	
	private MemberService() {}
	
	public boolean login(HttpServletRequest request, HttpSession session) {
		MemberVO member = null;
		String uid = request.getParameter("id");
		String pass = request.getParameter("pw");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try (Connection conn = DBConfig.getConnection()){
			pstmt = conn.prepareStatement(SQL.SEL_LOGIN);
			pstmt.setString(1, uid);
			pstmt.setString(2, pass);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				member = new MemberVO();
				member.setSeq(rs.getInt(1));
				member.setUid(rs.getString(2));
				member.setPass(rs.getString(3));
				member.setName(rs.getString(4));
				member.setNick(rs.getString(5));
				member.setEmail(rs.getString(6));
				member.setHp(rs.getString(7));
				member.setGrade(rs.getInt(8));
				member.setZip(rs.getString(9));
				member.setAddr1(rs.getString(10));
				member.setAddr2(rs.getString(11));
				member.setRegip(rs.getString(12));
				member.setRdate(rs.getString(13));
			}
			session.setAttribute("member",member);
			return true;
		} catch (Exception e) {
			session.invalidate();
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch(Exception e) {}
		}
	}
	
	public void logout(HttpServletResponse response, HttpSession session) {
		session.invalidate();
		try {
			response.sendRedirect("../list.jsp");
		} catch(Exception e) {}
		
	}
	
	public TermsVO terms() {
		Statement stmt = null;
		ResultSet rs = null;
		TermsVO vo = null;
		try (Connection conn = DBConfig.getConnection()) {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM `JSP_TERMS`");
			if(rs.next()) {
				vo = new TermsVO();
				vo.setTerms(rs.getString(1));
				vo.setPrivacy(rs.getString(2));
			}
			return vo;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(stmt != null) stmt.close();
				if(rs != null) rs.close();
			} catch(Exception e) {}
		}
	}
	public Boolean register(HttpServletRequest request) {
		PreparedStatement pstmt = null;
		MemberVO member = new MemberVO();
		member.setUid(request.getParameter("id"));
		member.setPass(request.getParameter("pw1"));
		member.setName(request.getParameter("name"));
		member.setNick(request.getParameter("nick"));
		member.setEmail(request.getParameter("email"));
		member.setHp(request.getParameter("hp"));
		member.setZip(request.getParameter("zip"));
		member.setAddr1(request.getParameter("addr1"));
		member.setAddr2(request.getParameter("addr2"));
		member.setRegip(request.getRemoteAddr());
		
		try (Connection conn = DBConfig.getConnection()){
			pstmt = conn.prepareStatement(SQL.INS_REGISTER);
			pstmt.setString(1,member.getUid());
			pstmt.setString(2,member.getPass());
			pstmt.setString(3,member.getName());
			pstmt.setString(4,member.getNick());
			pstmt.setString(5,member.getEmail());
			pstmt.setString(6,member.getHp());
			pstmt.setString(7,member.getZip());
			pstmt.setString(8,member.getAddr1());
			pstmt.setString(9,member.getAddr2());
			pstmt.setString(10,member.getRegip());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
		}
	}

}
