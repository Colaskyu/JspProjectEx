package service.member;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;
import vo.member.TermsVO;

public class TermsService implements CommonAction {
	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		
		TermsVO vo = new TermsVO();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try(Connection conn = DBConfig.getConnection()) {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL.SEL_TERMS);
			if(rs.next()) {
				vo.setTerms(rs.getString(1));
				vo.setPrivacy(rs.getString(2));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
			} catch (SQLException e) {}
		}
		
		req.setAttribute("vo", vo);
		
		return "/terms.jsp";
	}
}
