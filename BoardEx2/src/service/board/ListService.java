package service.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.DBConfig;
import JDBC.SQL;
import controller.CommonAction;
import vo.board.BoardPageVO;
import vo.board.BoardVO;

public class ListService implements CommonAction{
	@Override
	public String processProc(HttpServletRequest req, HttpServletResponse resp) {
		//로그인 여부 확인
		HttpSession session = req.getSession();;
		if(session.getAttribute("member") != null) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			int totalCnt = 0;
			int pageNum = 1;
			int startNum = 0;
			int pageSize = 10;	//강제지정 10
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
			String pageString = req.getParameter("pageNum");
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
			
			BoardPageVO pageVo = new BoardPageVO();
			pageVo.setPageNum(pageNum);
			pageVo.setStartNum(startNum);
			pageVo.setStartPage(startPage);
			pageVo.setEndPage(endPage);
			pageVo.setTotalPage(totalPage);
			pageVo.setPageSize(pageSize);
			
			//리스트 출력용 data
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
					vo.setRdate(rs.getString(11).substring(2, 10));
					vo.setNick(rs.getString(12));
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
			
			req.setAttribute("blist", blist);
			req.setAttribute("pageVo", pageVo);
			req.setAttribute("tc",totalCnt);
			
			return "/list.jsp";
		} else {
			return "redirect:/BoardEx2/member/login.do";
		}
	}
}
