package service.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;
import vo.board.BoardVO;

public class ViewService implements CommonAction{
	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		//로그인 여부 확인
		HttpSession session = req.getSession();
		if(session.getAttribute("member") != null) {
			String seq = req.getParameter("seq");
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			BoardVO vo = new BoardVO();
			try(Connection conn = DBConfig.getConnection()) {
				pstmt = conn.prepareStatement(SQL.SEL_BOARD);
				pstmt.setString(1, seq);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					vo.setSeq(rs.getInt(1));
					vo.setParent(rs.getInt(2));
					vo.setComment(rs.getInt(3));
					vo.setCate(rs.getString(4));
					vo.setTitle(rs.getString(5));
					vo.setContent(rs.getString(6));
					vo.setFile(rs.getInt(7));
					vo.setHit(rs.getInt(8));
					vo.setUid(rs.getString(9));
					vo.setRegip(rs.getString(10));
					vo.setRdate(rs.getString(11));
					vo.setNick(rs.getString(12));
					if(rs.getInt(7) == 1) {
						vo.setOldFileName(rs.getString(13));
						vo.setNewFileName(rs.getString(14));
						vo.setDownload(rs.getInt(15));
					}
				}
				req.setAttribute("vo", vo);
				return "/view.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
				} catch (Exception e) {}
			}
		} else {
			return "redirect:/BoardEx2/member/login.do";
		}
		
		
		
	}
}
