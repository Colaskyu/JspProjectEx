package kr.co.boardEx.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kr.co.boardEx.DBConfig;
import kr.co.boardEx.SQL;
import kr.co.boardEx.vo.BoardPageVO;
import kr.co.boardEx.vo.BoardVO;
import kr.co.boardEx.vo.MemberVO;

public class BoardService {
	private static BoardService service = new BoardService();
	
	public static BoardService getInstance() {
		return service;
	}
	
	private BoardService() {}
	
	//write 페이지의 write 처리
	public String write(HttpServletRequest request, HttpSession session) throws IOException, ServletException {
		MemberVO member = (MemberVO)session.getAttribute("member");
		BoardVO board = new BoardVO();
		String path = request.getServletContext().getRealPath("/upload");
		int maxSize = 1024 * 1024 * 10; 
		
		MultipartRequest mr = new MultipartRequest(request, path, maxSize, "UTF-8", new DefaultFileRenamePolicy());
		board.setTitle(mr.getParameter("subject"));
		board.setContent(mr.getParameter("content"));
		board.setUid(member.getUid());
		board.setRegip(request.getRemoteAddr());
		String fileName= mr.getFilesystemName("file");
		String newFileName = null;
		
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
				read = fis.read();
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
		
		String url = null;
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
			url = "../list.jsp?result=success";
		} catch (Exception e) {
			e.printStackTrace();
			url = "../write.jsp?result=fail";
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(rs != null) rs.close();
			} catch(Exception e) {}
		}
		
		if(board.getSeq() != 0 && board.getFile() == 1) {
			writeFile(board.getSeq(), fileName, newFileName);
		}
		
		return url;
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
	
	public void updateDownload(int seq) {
		PreparedStatement pstmt = null;
		try(Connection conn = DBConfig.getConnection()) {
			pstmt = conn.prepareStatement(SQL.UPT_DOWNLOADCNT);
			pstmt.setInt(1, seq);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
		}
	}
	
	//list 페이지의 페이지 설정
	public BoardPageVO listPage(HttpServletRequest request) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int totalCnt = 0;
		int pageNum = 1;
		int startNum = 0;
		int pageSize = 10;	//강제지정 5
		int startPage = 1;
		int endPage = 1;
		int totalPage = 0;
		
		try(Connection conn = DBConfig.getConnection()) {
			pstmt = conn.prepareStatement(SQL.SEL_LISTCNT);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				totalCnt = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch(Exception e) {}
		}

		//현재 페이지 설정(해당 param 이 없을 경우 1페이지로 지정)
		String pageString = request.getParameter("pageNum");
		if(pageString == null || pageString.equals("") || pageString.equals("0")) {
			pageNum = 1;
		} else if(Integer.parseInt(pageString) < 1){
			pageNum = 1;
		} else {
			pageNum = Integer.parseInt(pageString);
		}
		
		totalPage = ((totalCnt - 1) / pageSize) + 1;
		//다음을 눌렀을 경우 현재 페이지가 최대 페이지수를 넘어갈 경우 : pageNum = 최대페이지
		if(pageNum > totalPage) {
			pageNum = totalPage;
		}
		startPage = ((pageNum - 1) / 10) * 10 + 1;
		endPage = startPage + 9;
		//마지막 페이지가 최대 페이지수를 넘어갈 경우 : endPage = 최대페이지
		if(endPage > totalPage) {
			endPage = totalPage;
		}
		startNum = (pageNum - 1) * pageSize;
		
		BoardPageVO vo = new BoardPageVO();
		vo.setPageNum(pageNum);
		vo.setStartNum(startNum);
		vo.setStartPage(startPage);
		vo.setEndPage(endPage);
		vo.setTotalPage(totalPage);
		vo.setPageSize(pageSize);
		
		return vo;
	}
	
	//list 페이지의 list 내용
	public List<BoardVO> list(HttpServletRequest request, int startNum, int pageSize) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> blist = new ArrayList<>();
		try(Connection conn = DBConfig.getConnection()) {
			pstmt = conn.prepareStatement(SQL.SEL_LIST);
			pstmt.setInt(1, startNum);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardVO vo = new BoardVO();
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
				blist.add(vo);
			}
			return blist;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
		}
	}
	
	public BoardVO view(HttpServletRequest request) {
		int seq = Integer.parseInt(request.getParameter("seq"));
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVO vo = new BoardVO();
		try(Connection conn = DBConfig.getConnection()) {
			pstmt = conn.prepareStatement(SQL.SEL_BOARD);
			pstmt.setInt(1, seq);
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
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
		}
		
		return vo;
	}
	
	public void updateHit(int seq) {
		PreparedStatement pstmt = null;
		try(Connection conn = DBConfig.getConnection()) {
			pstmt = conn.prepareStatement(SQL.UPT_BOARDHIT);
			pstmt.setInt(1, seq);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
		}
	}
	
	public int deleteBoard(HttpServletRequest request) {
		int seq = Integer.parseInt(request.getParameter("seq"));
		String path = request.getServletContext().getRealPath("/upload");
		
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
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		} finally {
			try {
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
		}
	}
	
	public String modifyBoard(HttpServletRequest request, HttpSession session) throws IOException {
		String path = request.getServletContext().getRealPath("/upload");
		int maxSize = 1024 * 1024 * 10; 
		MultipartRequest mr = new MultipartRequest(request, path, maxSize, "UTF-8", new DefaultFileRenamePolicy());
		
		int seq = Integer.parseInt(mr.getParameter("seq"));
		String url = "../list.jsp?seq="+mr.getParameter("seq") + "&pageNum="+mr.getParameter("pageNum");
		int setFile = 0;
		
		//신규파일 지정시 명칭
		String fileName= mr.getFilesystemName("file");
		//기존 저장 파일명
		String bfname = selectFile(seq);
		if(fileName != null && bfname != null) {
			File bfFile = new File(path + "/" + bfname);
			if(bfFile.exists()) {
				bfFile.delete();
			}
		}
		//db에서 업로드 파일 데이터 제거
		deleteFile(seq);
		
		//서버 파일 이름변경
		MemberVO member = (MemberVO)session.getAttribute("member");
		String newFileName = null;
		
		if(fileName != null) {
			setFile = 1;
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
			
			FileInputStream fis = new FileInputStream(oldFile);
			FileOutputStream fos = new FileOutputStream(newFile);
			//파일 복사
			int read = 0;
			while(true) {
				read = fis.read();
				if(read == -1) {
					break; //더이상 가져올 binary data가 없을경우 break
				}
				fos.write(buf, 0, read);
			}
			fis.close();
			fos.close();
			
			oldFile.delete();
		}
		
		PreparedStatement pstmt = null;
		try(Connection conn = DBConfig.getConnection()) {
			pstmt = conn.prepareStatement(SQL.UPT_BOARD);
			pstmt.setString(1, mr.getParameter("subject"));
			pstmt.setString(2, mr.getParameter("content"));
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
		
		if(setFile == 1) {
			writeFile(seq, fileName, newFileName);
		}
		
		return url;
	}
	
	public int checkBoardId(HttpSession session, int seq) {
		MemberVO member = (MemberVO)session.getAttribute("member"); 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection conn = DBConfig.getConnection()) {
			pstmt = conn.prepareStatement(SQL.SEL_CHKBOARDID);
			pstmt.setInt(1, seq);
			pstmt.setString(2, member.getUid());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return 0;
			} else {
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
		}
	}
	
	//commentView 페이지의 write 처리
	public String commentWrite(HttpServletRequest request, HttpSession session) {
		MemberVO member = (MemberVO)session.getAttribute("member");
		String seq = request.getParameter("seq");
		String content = request.getParameter("comment");
		String uid =member.getUid();;
		String regip = request.getRemoteAddr();
		
		String url = "../view.jsp?seq="+seq;
		CallableStatement cstmt = null;
		try (Connection conn = DBConfig.getConnection()){
			cstmt = conn.prepareCall(SQL.INS_COMMENT);
			cstmt.setInt(1,Integer.parseInt(seq));
			cstmt.setString(2,content);
			cstmt.setString(3,uid);
			cstmt.setString(4,regip);
			cstmt.executeUpdate();
			return url;
		} catch (Exception e) {
			e.printStackTrace();
			return url;
		} finally {
			try {
				if(cstmt != null) cstmt.close();
			} catch(Exception e) {}
		}
	}
	
	//list 페이지의 list 내용
	public ArrayList<BoardVO> viewComment(HttpServletRequest request) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BoardVO> blist = new ArrayList<>();
		int seq = Integer.parseInt(request.getParameter("seq")); 
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
			return blist;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (Exception e) {}
		}
	}
	
	public void commentDelete(int cmtSeq) {
		PreparedStatement pstmt = null;
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
	}
	
	public void commentCnt(int seq) {
		PreparedStatement pstmt = null;
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
	}
}
