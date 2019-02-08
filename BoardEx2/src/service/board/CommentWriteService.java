package service.board;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;
import vo.member.MemberVO;

public class CommentWriteService implements CommonAction{

	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		MemberVO member = (MemberVO)session.getAttribute("member");
		String seq = req.getParameter("seq");
		String content = req.getParameter("comment");
		String uid =member.getUid();;
		String pageNum = req.getParameter("pageNum");
		String regip = req.getRemoteAddr();
		
		CallableStatement cstmt = null;
		try (Connection conn = DBConfig.getConnection()){
			cstmt = conn.prepareCall(SQL.INS_COMMENT);
			cstmt.setInt(1,Integer.parseInt(seq));
			cstmt.setString(2,content);
			cstmt.setString(3,uid);
			cstmt.setString(4,regip);
			cstmt.executeUpdate();
			return "redirect:/BoardEx2/view.do?seq="+seq+"&pageNum="+pageNum;
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		} finally {
			try {
				if(cstmt != null) cstmt.close();
			} catch(Exception e) {}
		}
	}

}
