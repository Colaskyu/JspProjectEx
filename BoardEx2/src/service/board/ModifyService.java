package service.board;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class ModifyService implements CommonAction {

	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		//�α��� ���� Ȯ��
		HttpSession session = req.getSession();;
		if(session.getAttribute("member") != null) {			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			BoardVO vo = new BoardVO();
			if(req.getMethod().equals("GET")) {
				// ��� get ����
				String seq = req.getParameter("seq");
				String pageNum = req.getParameter("pageNum");
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
					req.setAttribute("pageNum",pageNum);
					return "/modify.jsp";
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
				// ���� post ����
				// ���� ���ε� ���� Ȯ�� / ���� �� ������Ʈ 
				MemberVO member = (MemberVO)session.getAttribute("member");
				String path = req.getServletContext().getRealPath("/upload");
				int maxSize = 1024 * 1024 * 10;
				String seqS = null;
				int seq = 0;
				int setFile = 0; //���� ���翩��
				int writeFile = 0; //���� �ٽþ��⿩��
				String fileName = null;
				String newFileName = null;
				String pageNum = null;
				try {
					//���� �� enctype�� multipart �̹Ƿ� multipart ��ü ����
					MultipartRequest mr = new MultipartRequest(req, path, maxSize, "UTF-8", new DefaultFileRenamePolicy());
					seqS = mr.getParameter("seq");
					seq = Integer.parseInt(mr.getParameter("seq")); //�ۼ���
					vo.setTitle(mr.getParameter("subject"));
					vo.setContent(mr.getParameter("content"));
					vo.setUid(member.getUid());
					vo.setRegip(req.getRemoteAddr());
					fileName= mr.getFilesystemName("file"); //�ű� ���� ���ϸ�
					pageNum = mr.getParameter("pageNum"); 
					//���� ���� ���ϸ�
					String bfname = selectFile(seq);
					//���� ���� ���� ���ε� ��ü�� �ְ� �ű� ���� ���ε带 �������� ��� ���� ���ε� ���� ����
					if(fileName != null && bfname != null) {
						File bfFile = new File(path + "/" + bfname);
						if(bfFile.exists()) {
							bfFile.delete();
						}
						//db upload ������ ����
						deleteFile(seq);
					}
					
					if(fileName != null || bfname != null ) {
						setFile = 1;
					}
					
					//������ ������ ��� ������ ���� ����
					if(fileName != null) {
						writeFile = 1;
						//���ϸ� ����(UUID)
						int idx = fileName.lastIndexOf(".");
						String ext = fileName.substring(idx);
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
						String now = sdf.format(date);
						newFileName = now + member.getUid() + ext;
						//���ϸ� ����
						byte[] buf = new byte[1024];
						File oldFile = new File(path+"/"+fileName);
						File newFile = new File(path+"/"+newFileName);
						
						FileInputStream fis = new FileInputStream(oldFile);
						FileOutputStream fos = new FileOutputStream(newFile);
						//���� ����
						int read = 0;
						while(true) {
							read = fis.read();
							if(read == -1) {
								break; //���̻� ������ binary data�� ������� break
							}
							fos.write(buf, 0, read);
						}
						fis.close();
						fos.close();
						
						oldFile.delete();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//�Խñ� �������� ����
				try(Connection conn = DBConfig.getConnection()) {
					pstmt = conn.prepareStatement(SQL.UPT_BOARD);
					pstmt.setString(1, vo.getTitle());
					pstmt.setString(2, vo.getContent());
					pstmt.setInt(3, setFile);
					pstmt.setInt(4, seq);
					pstmt.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if(pstmt != null) pstmt.close();
					} catch (Exception e) {}
				}
				//������ ������ ��� db�� ���� ���� ����
				if(writeFile == 1) {
					writeFile(seq, fileName, newFileName);
				}
				return "redirect:/BoardEx2/view.do?seq="+ seqS + "&pageNum="+pageNum;
			}
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
