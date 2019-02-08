package service.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;
import vo.board.BoardVO;

public class CommentViewService implements CommonAction {

	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BoardVO> blist = new ArrayList<>();
		int seq = Integer.parseInt(req.getParameter("seq")); 
		try(Connection conn = DBConfig.getConnection()) {
			pstmt = conn.prepareStatement(SQL.SEL_COMMENT);
			pstmt.setInt(1, seq);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardVO vo = new BoardVO();
				vo.setSeq(rs.getInt(1));
				vo.setContent(rs.getString(2));
				vo.setRdate(rs.getString(3));
				vo.setNick(rs.getString(4));
				blist.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
		}
		
		Gson gson = new Gson();
		String jsonl = gson.toJson(blist);
		
		
		return jsonl;
	}

}
