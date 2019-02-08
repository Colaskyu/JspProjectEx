package service.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;
import vo.member.MemberVO;

public class RegisterService implements CommonAction {

	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		
		if(req.getMethod().equals("GET")) {
			//회원가입 뷰
			return "/register.jsp";
		} else {
			//회원가입 처리
			PreparedStatement pstmt = null;
			MemberVO member = new MemberVO();
			member.setUid(req.getParameter("id"));
			member.setPass(req.getParameter("pw1"));
			member.setName(req.getParameter("name"));
			member.setNick(req.getParameter("nick"));
			member.setEmail(req.getParameter("email"));
			member.setHp(req.getParameter("hp"));
			member.setZip(req.getParameter("zip"));
			member.setAddr1(req.getParameter("addr1"));
			member.setAddr2(req.getParameter("addr2"));
			member.setRegip(req.getRemoteAddr());
			
			try(Connection conn = DBConfig.getConnection()) {
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
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				try {
					pstmt.close();
				} catch (SQLException e) {}
			}
			
			return "redirect:/BoardEx2/member/login.do";
		}
	}
	
	

}
