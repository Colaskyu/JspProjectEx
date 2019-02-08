package service.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;
import vo.member.MemberVO;

public class LoginService implements CommonAction {
	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();;
		if(req.getMethod().equals("POST")) {
			//로그인 정보 확인
			MemberVO member = null;
			String uid = req.getParameter("id");
			String pass = req.getParameter("pw");
			String url = "";
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
					session.setAttribute("member",member);
					url = "/BoardEx2/list.do";
				} else {
					session.invalidate();
					url = "/BoardEx2/member/login.do?result=fail";
				}
			} catch (Exception e) {
				session.invalidate();
				e.printStackTrace();
			} finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
				} catch(Exception e) {}
			}
			
			return "redirect:"+url;
		} else {
			//로그인 뷰
			if(session.getAttribute("member") != null) {
				return "redirect:/BoardEx2/list.do";
			} else {
				return "/login.jsp";
			}
			
		}
	}
}
