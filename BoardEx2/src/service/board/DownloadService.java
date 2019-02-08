package service.board;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;

public class DownloadService implements CommonAction{

	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		int seq = Integer.parseInt(req.getParameter("seq"));
		
		if(req.getMethod().equals("GET")) {
			String fname = req.getParameter("fname");
			String ofname = req.getParameter("ofname");
			//경로
			String path = req.getServletContext().getRealPath("/upload");
			File file = new File(path + "/" + fname);
			String name = null;
			try {
				name = new String(ofname.getBytes("UTF-8"), "8859_1");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			
			//file download response 객체 준비 (header)
			resp.setContentType("application/octet-stream;charset=utf-8");
			resp.setHeader("Content-Disposition", "attachment; filename="+name);
			resp.setHeader("Content-Transfer-Encoding", "binary");
			resp.setHeader("Pragma", "no-cache");
			resp.setHeader("Cache-Control", "private");
			
			//스트림 연결
			OutputStream fos = null;
			BufferedOutputStream bos = null;
			try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
				fos = resp.getOutputStream();
				bos = new BufferedOutputStream(fos);
				
				byte b[] = new byte[1024];
				
				int read = 0;
				while(true) {
					read = bis.read(b);
					if(read == -1) {
						break;
					}
					bos.write(b, 0, read);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bos.flush();
					bos.close();
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		} else {
			updateDownload(seq);
			
			JsonObject json = new JsonObject(); 
			json.addProperty("download", selectDownload(seq));
			System.out.println(json.toString());
			return json.toString();
		}
	}
	
	private void updateDownload(int seq) {
		PreparedStatement pstmt = null;
		try(Connection conn = DBConfig.getConnection()) {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(SQL.UPT_DOWNLOADCNT);
			pstmt.setInt(1, seq);
			pstmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
		}
	}
	
	private String selectDownload(int seq) {
		String download = "0";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection conn = DBConfig.getConnection()) {
			pstmt = conn.prepareStatement(SQL.SEL_DOWNLOADCNT);
			pstmt.setInt(1, seq);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				download = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(rs != null) rs.close();
			} catch (Exception e) {}
		}
		
		return download;
	}
}
