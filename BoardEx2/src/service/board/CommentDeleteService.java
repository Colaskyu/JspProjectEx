package service.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;
import vo.member.MemberVO;

public class CommentDeleteService implements CommonAction{

	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		int seq = Integer.parseInt(req.getParameter("seq"));
		int cmtSeq = Integer.parseInt(req.getParameter("cmtSeq"));
		JsonObject json = new JsonObject(); 
		MemberVO member = (MemberVO)session.getAttribute("member");
		String uid = member.getUid();
		String cUid = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try(Connection conn = DBConfig.getConnection()) {
			pstmt = conn.prepareStatement(SQL.SEL_CMTUSER);
			pstmt.setInt(1, cmtSeq);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cUid = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(rs != null) rs.close();
			} catch (Exception e) {}
		}
		
		if(cUid.equals(uid)) {
			try(Connection conn = DBConfig.getConnection()) {
				pstmt = conn.prepareStatement(SQL.DEL_BOARD);
				pstmt.setInt(1, cmtSeq);
				pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(pstmt != null) pstmt.close();
				} catch (Exception e) {}
			}
			
			try(Connection conn = DBConfig.getConnection()) {
				pstmt = conn.prepareStatement(SQL.UPT_CMTCNT);
				pstmt.setInt(1, seq);
				pstmt.setInt(2, seq);
				pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(pstmt != null) pstmt.close();
				} catch (Exception e) {}
			}
			json.addProperty("result", "success");
			
		} else {
			json.addProperty("result", "작성자만 삭제 가능합니다.");
		}
		
		return json.toString();
	}

}
