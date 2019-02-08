package service.board;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;
import vo.board.BoardVO;
import vo.member.MemberVO;

public class WriteService implements CommonAction {

	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		//로그인 여부 확인
		HttpSession session = req.getSession();;
		if(session.getAttribute("member") != null) {
			if(req.getMethod().equals("POST")) {
				MemberVO member = (MemberVO)session.getAttribute("member");
				BoardVO board = new BoardVO();
				String path = req.getServletContext().getRealPath("/upload");
				
				int maxSize = 1024 * 1024 * 10; 
				String fileName = null;
				String newFileName = null;
				MultipartRequest mr = null;
				try {
					mr = new MultipartRequest(req, path, maxSize, "UTF-8", new DefaultFileRenamePolicy());
					board.setTitle(mr.getParameter("subject"));
					board.setContent(mr.getParameter("content"));
					board.setUid(member.getUid());
					board.setRegip(req.getRemoteAddr());
					fileName= mr.getFilesystemName("file");
					
					if(fileName != null) {
						board.setFile(1);
						//파일명 생성(UUID)
						int idx = fileName.lastIndexOf(".");
						String ext = fileName.substring(idx);
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
						String now = sdf.format(date);
						newFileName = now + member.getUid() + ext;
						//파일명 변경
						byte[] buf = new byte[1024];
						File oldFile = new File(path+"/"+fileName);
						File newFile = new File(path+"/"+newFileName);
						//oldFile.renameTo(newFile);
						
						FileInputStream fis = new FileInputStream(oldFile);
						FileOutputStream fos = new FileOutputStream(newFile);
						//파일 복사
						int read = 0;
						while(true) {
							read = fis.read(buf);
							if(read == -1) {
								break; //더이상 가져올 binary data가 없을경우 break
							}
							fos.write(buf, 0, read);
						}
						fis.close();
						fos.close();
						
						oldFile.delete();
						
					} else {
						board.setFile(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
				try (Connection conn = DBConfig.getConnection()){
					pstmt = conn.prepareStatement(SQL.INS_BOARD, Statement.RETURN_GENERATED_KEYS);
					pstmt.setString(1,board.getTitle());
					pstmt.setString(2,board.getContent());
					pstmt.setString(3,board.getUid());
					pstmt.setInt(4,board.getFile());
					pstmt.setString(5,board.getRegip());
					pstmt.executeUpdate();
					rs = pstmt.getGeneratedKeys();
					board.setSeq((rs.next()) ? rs.getInt(1) : 0);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if(pstmt != null) pstmt.close();
						if(rs != null) rs.close();
					} catch(Exception e) {}
				}
				
				if(board.getSeq() != 0 && board.getFile() == 1) {
					writeFile(board.getSeq(), fileName, newFileName);
				}
				
				return "/list.do";
			} else {
				return "/write.jsp";
			}		
		} else {
			return "redirect:/BoardEx2/member/login.do";
		}
	}
	
	private boolean writeFile(int parent, String oldFileName, String newFileName) {
		PreparedStatement pstmt = null;
		try (Connection conn = DBConfig.getConnection()){
			pstmt = conn.prepareStatement(SQL.INS_FILE);
			pstmt.setInt(1,parent);
			pstmt.setString(2,oldFileName);
			pstmt.setString(3,newFileName);
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
