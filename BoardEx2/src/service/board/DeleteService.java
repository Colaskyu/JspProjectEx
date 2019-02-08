package service.board;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;

public class DeleteService implements CommonAction{

	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		//로그인 여부 확인
		HttpSession session = req.getSession();;
		if(session.getAttribute("member") != null) {
			int seq = Integer.parseInt(req.getParameter("seq"));
			String path = req.getServletContext().getRealPath("/upload");
			
			//업로드 파일 삭제
			String bfname = selectFile(seq);
			if(bfname != null) {
				File bfFile = new File(path + "/" + bfname);
				if(bfFile.exists()) {
					bfFile.delete();
				}
			}
			//db에서 업로드 파일데이터 삭제
			deleteFile(seq);
					
			PreparedStatement pstmt = null;
			try(Connection conn = DBConfig.getConnection()) {
				pstmt = conn.prepareStatement(SQL.DEL_BOARD);
				pstmt.setInt(1, seq);
				pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(pstmt != null) pstmt.close();
				} catch (Exception e) {}
			}
			return "/list.do";
		} else {
			return "redirect:/BoardEx2/member/login.do";
		}
	}
	
	private String selectFile(int parent) {
		String fname = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection conn = DBConfig.getConnection()){
			pstmt = conn.prepareStatement(SQL.SEL_FILE);
			pstmt.setInt(1,parent);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				fname = rs.getString(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch(Exception e) {}
		}
		
		return fname;
	}
	
	private boolean deleteFile(int parent) {
		PreparedStatement pstmt = null;
		try (Connection conn = DBConfig.getConnection()){
			pstmt = conn.prepareStatement(SQL.DEL_FILE);
			pstmt.setInt(1,parent);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(pstmt != null) pstmt.close();
			} catch(Exception e) {}
		}
		
		return true;
	}

}
